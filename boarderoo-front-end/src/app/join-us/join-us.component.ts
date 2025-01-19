import { Component, EventEmitter, Output, OnInit, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ToastContainerDirective, ToastrService } from 'ngx-toastr';
import { FormsModule } from '@angular/forms'; // Importowanie FormsModule
import { HttpClient, HttpClientModule, HttpParams } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { CustomResponse } from '../CustomResponse';
import { Router } from '@angular/router';

@Component({
  selector: 'app-join-us',
  standalone: true,
  imports: [FormsModule, HttpClientModule, CommonModule],  // Dodajemy FormsModule w imports
  templateUrl: './join-us.component.html',
  styleUrls: ['./join-us.component.css'],
  schemas: [CUSTOM_ELEMENTS_SCHEMA], // Dodajemy schemat
})
export class JoinUsComponent {
  @Output() close = new EventEmitter<void>(); // Definiujemy zdarzenie

  toastContainer: ToastContainerDirective | undefined;
  emailLogin: string = '';  // Dodajemy zmienną na e-mail
  passwordLogin: string = '';  // Dodajemy zmienną na e-mail
  firstName: string = ''; // Imię z formularza rejestracji
  lastName: string = ''; // Nazwisko z formularza rejestracji
  address: string = ''; // Adres z formularza rejestracji
  emailRegistration: string = ''; // Email z formularza rejestracji
  passwordRegistration: string = ''; // Hasło z formularza rejestracji
  confirmPassword: string = ''; // Potwierdzenie hasła z formularza rejestracji

  constructor(private toastr: ToastrService, private http: HttpClient, private router: Router) {}

  onClose() {
    this.close.emit(); // Emitowanie zdarzenia
  }

  LogIn(){
     if(this.emailLogin === '' || this.passwordLogin === ''){
      this.toastEmptyFields();
     }
     else{
      this.LogInPost();
     }
  }

  LogInPost() {
    const proxyUrl = "https://cors-anywhere.herokuapp.com/"
    const targetUrl = 'https://boarderoo-928336702407.europe-central2.run.app/login';
    const fullUrl = proxyUrl + targetUrl;
    console.log(fullUrl);
    this.http.post<CustomResponse>(fullUrl, { email: this.emailLogin, password: this.passwordLogin }).subscribe(response => {
      console.log(response);
      localStorage.setItem('session_token', this.emailLogin);
      this.router.navigate(['/gry']);
      this.successToast(response.message);
    }, error => {
      console.error('Błąd:', error);
      this.failToast(error.error?.message);
    });
  }

  register() {
    if(this.firstName === '' || this.lastName === '' || this.address === '' || this.emailRegistration === '' || this.passwordRegistration === '' || this.confirmPassword === '') {
      this.toastEmptyFields();
    }
    else if(this.passwordRegistration !== this.confirmPassword) {
      this.failToast("Podane hasła nie są identyczne");
    }
    else {
      this.registerPost();
    }
  }

  signInWithDiscord(): void {

    const discordAuthUrl = 'https://discord.com/oauth2/authorize?client_id=1303087880503296182&response_type=code&redirect_uri=https%3A%2F%2Fboarderoo-71469.firebaseapp.com%2F&scope=email';
    window.location.href = discordAuthUrl;
  }

  signInWithGoogle(): void {
    const clientId = '928336702407-bdifeaptq727tsor03bcbaqkvunbg7h1.apps.googleusercontent.com';
      // Wstaw swój Client ID z Google API Console
    const redirectUri = 'https://boarderoo-71469.firebaseapp.com'; // Lub inny odpowiedni URI

    const googleAuthUrl = `https://accounts.google.com/o/oauth2/v2/auth?response_type=code&client_id=${clientId}&redirect_uri=${redirectUri}&scope=openid profile email`;
    console.log(googleAuthUrl);
    window.location.href = googleAuthUrl;  // Przekierowanie użytkownika do Google
  }

  // Funkcja do wysyłania danych rejestracji na serwer
  registerPost() {
    const proxyUrl = 'http://localhost:8080/'; // Lokalny serwer proxy
    const targetUrl = 'https://boarderoo-928336702407.europe-central2.run.app/register';
    const fullUrl = proxyUrl + targetUrl;
    const regisUser = {
      "id": "",
      "email": this.emailRegistration,
      "isVerified": true,
      "address": this.address,
      "name": this.firstName,
      "password": this.passwordRegistration,
      "surname": this.lastName,
      "token": "string",
      "tokenCreationDate": "2025-01-18T23:47:59.353Z"
    }
    console.log(regisUser);
    this.http.post<CustomResponse>(fullUrl, regisUser ).subscribe(response => {
      console.log(response);
      this.successToast(response.message);
      this.onClose();
      
    }, error => {
      console.error('Błąd:', error);
      this.failToast(error.error?.message);
    });
  }
  
  toastEmptyFields() {
    this.toastr.overlayContainer = this.toastContainer;
    
    // Jeśli e-mail nie jest wypełniony, czerwony toast
    this.toastr.error('Jedno z wymaganych pól nie jest uzupełnione', 'Błąd', {
      positionClass: 'toast-top-right',
      timeOut: 3000,
      progressBar: true,
      progressAnimation: 'increasing',
    });
  }

  failToast(communicate: string) {
    this.toastr.overlayContainer = this.toastContainer;
    
    // Jeśli e-mail nie jest wypełniony, czerwony toast
    this.toastr.error(communicate, 'Błąd', {
      positionClass: 'toast-top-right',
      timeOut: 3000,
      progressBar: true,
      progressAnimation: 'increasing',
    });
  }

  successToast(communicate: string) {
    this.toastr.overlayContainer = this.toastContainer;
    
    // Jeśli e-mail nie jest wypełniony, czerwony toast
    this.toastr.success(communicate, 'Sukces', {
      positionClass: 'toast-top-right',
      timeOut: 3000,
      progressBar: true,
      progressAnimation: 'increasing',
    });
  }


  forgotPasswordSend(){
    if(this.emailLogin !== ''){
      const proxyUrl = 'http://localhost:8080/'; // Lokalny serwer proxy
      const targetUrl = 'https://boarderoo-928336702407.europe-central2.run.app/password/reset?email=' + this.emailLogin;
      const fullUrl = proxyUrl + targetUrl;
      console.log(fullUrl);
      
      this.http.get<CustomResponse>(fullUrl).subscribe(
        (response) => {
          console.log('Sukces:', response);
          this.successToast("Wysłano link do resetu hasła"); // Powiadomienie o sukcesie
        },
        (error) => {
          console.error('Błąd:', error);
          this.failToast(error.error?.message); // Powiadomienie o błędzie
        }
      );
    }
    else{
      this.failToast("Uzupełnij pole email w logowaniu!")
    }
  }
}
