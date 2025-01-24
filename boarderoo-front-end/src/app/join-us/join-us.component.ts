import { Component, EventEmitter, Output, OnInit, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ToastContainerDirective, ToastrService } from 'ngx-toastr';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule, HttpParams } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { CustomResponse } from '../CustomResponse';
import { Router } from '@angular/router';

@Component({
  selector: 'app-join-us',
  standalone: true,
  imports: [FormsModule, HttpClientModule, CommonModule],
  templateUrl: './join-us.component.html',
  styleUrls: ['./join-us.component.css'],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class JoinUsComponent {
  @Output() close = new EventEmitter<void>();

  toastContainer: ToastContainerDirective | undefined;
  emailLogin: string = '';
  passwordLogin: string = '';
  firstName: string = '';
  lastName: string = '';
  address: string = '';
  emailRegistration: string = '';
  passwordRegistration: string = '';
  confirmPassword: string = '';

  constructor(private toastr: ToastrService, private http: HttpClient, private router: Router) { }

  onClose() {
    this.close.emit();
  }

  LogIn() {
    if (this.emailLogin === '' || this.passwordLogin === '') {
      this.failToast('Jedno z wymaganych pól nie jest uzupełnione');
    }
    else {
      this.LogInPost();
    }
  }

  LogInPost() {
    const targetUrl = 'https://boarderoo-928336702407.europe-central2.run.app/login';

    this.http.post<CustomResponse>(targetUrl, { email: this.emailLogin, password: this.passwordLogin }, { observe: 'response' })
      .subscribe(response => {

        if (response.status === 200) {
          localStorage.setItem('session_token', this.emailLogin);
          this.router.navigate(['/gry']);
          this.successToast('Zalogowano pomyślnie!');
        } else if (response.status === 202) {
          this.successToast('Wysłano link aktywacyjny. Sprawdź swoją skrzynkę e-mail.');
        } else if (response.status === 204) {
          this.successToast('Link aktywacyjny został już wcześniej wysłany. Sprawdź swoją skrzynkę e-mail.');
        } else {
          this.failToast('Nieznana odpowiedź serwera.');
        }
      }, error => {
        this.failToast(error.error?.message || 'Wystąpił błąd podczas logowania.');
      });
  }


  register() {
    if (this.firstName === '' || this.lastName === '' || this.address === '' || this.emailRegistration === '' || this.passwordRegistration === '' || this.confirmPassword === '') {
      this.failToast('Jedno z wymaganych pól nie jest uzupełnione');
    }
    else if (this.passwordRegistration !== this.confirmPassword) {
      this.failToast("Podane hasła nie są identyczne");
    }
    else {
      this.registerPost();
    }
  }

  signInWithDiscord(): void {

    const discordAuthUrl = 'https://discord.com/oauth2/authorize?client_id=1303087880503296182&response_type=code&redirect_uri=https%3A%2F%2Fboarderoo-71469.firebaseapp.com%2F&scope=email+identify';
    window.location.href = discordAuthUrl;
  }

  signInWithGoogle(): void {
    const clientId = '928336702407-bdifeaptq727tsor03bcbaqkvunbg7h1.apps.googleusercontent.com';
    const redirectUri = 'https://boarderoo-71469.firebaseapp.com';

    const googleAuthUrl = `https://accounts.google.com/o/oauth2/v2/auth?response_type=code&client_id=${clientId}&redirect_uri=${redirectUri}&scope=openid profile email`;
    window.location.href = googleAuthUrl;
  }

  registerPost() {
    const targetUrl = 'https://boarderoo-928336702407.europe-central2.run.app/register';
    const regisUser = {
      "id": "",
      "email": this.emailRegistration,
      "isVerified": true,
      "address": this.address,
      "name": this.firstName,
      "password": this.passwordRegistration,
      "authorization": 'local',
      "surname": this.lastName,
      "token": "string",
      "tokenCreationDate": "2025-01-18T23:47:59.353Z"
    }
    this.http.post<CustomResponse>(targetUrl, regisUser).subscribe(response => {
      this.successToast(response.message);
      this.onClose();

    }, error => {
      this.failToast(error.error?.message);
    });
  }

  failToast(communicate: string) {
    this.toastr.overlayContainer = this.toastContainer;

    this.toastr.error(communicate, 'Błąd', {
      positionClass: 'toast-top-right',
      timeOut: 3000,
      progressBar: true,
      progressAnimation: 'increasing',
    });
  }

  successToast(communicate: string) {
    this.toastr.overlayContainer = this.toastContainer;

    this.toastr.success(communicate, 'Sukces', {
      positionClass: 'toast-top-right',
      timeOut: 3000,
      progressBar: true,
      progressAnimation: 'increasing',
    });
  }


  forgotPasswordSend() {
    if (this.emailLogin !== '') {
      const proxyUrl = '';
      const targetUrl = 'https://boarderoo-928336702407.europe-central2.run.app/password/reset?email=' + this.emailLogin;
      const fullUrl = proxyUrl + targetUrl;

      this.http.get<CustomResponse>(fullUrl).subscribe(
        (response) => {
          this.successToast("Wysłano link do resetu hasła");
        },
        (error) => {
          this.failToast(error.error?.message);
        }
      );
    }
    else {
      this.failToast("Uzupełnij pole email w logowaniu!")
    }
  }
}
