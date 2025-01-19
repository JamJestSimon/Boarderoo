import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastContainerDirective, ToastrService } from 'ngx-toastr';
import { CustomResponse } from '../CustomResponse';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-admin-login',
  standalone: true,
  imports: [HttpClientModule, CommonModule, FormsModule],
  templateUrl: './admin-login.component.html',
  styleUrl: './admin-login.component.css',
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class AdminLoginComponent {
  toastContainer: ToastContainerDirective | undefined;
  login: string = ''; 
  passwordLogin: string = ''; 
  constructor(private toastr: ToastrService, private http: HttpClient, private router: Router, private route: ActivatedRoute) {}

  ngOnInit(): void {
    const sessionToken = localStorage.getItem('session_token_admin');
    if (sessionToken) {
      // Jeśli token jest pusty, przekierowujemy na stronę główną
      this.router.navigate(['/adminpanel']);
  }}

  LogIn(){
       if(this.login === '' || this.passwordLogin === ''){
        this.failToast('Jedno z wymaganych pól nie jest uzupełnione');
       }
       else{
        this.LogInPost();
       }
    }
  
    LogInPost() {
      const targetUrl = 'https://boarderoo-928336702407.europe-central2.run.app/login/admin';
      const fullUrl = targetUrl;
      console.log(fullUrl);
      this.http.post<CustomResponse>(fullUrl, { login: this.login, password: this.passwordLogin }).subscribe(response => {
        localStorage.setItem('session_token_admin', this.login);
        this.router.navigate(['/adminpanel']);
        this.successToast(response.message);
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

}
