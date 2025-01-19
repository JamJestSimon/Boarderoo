import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastContainerDirective, ToastrService } from 'ngx-toastr';
import { CustomResponse } from '../CustomResponse';

@Component({
  selector: 'app-password-reset',
  standalone: true,
  imports: [HttpClientModule, CommonModule, FormsModule],
  templateUrl: './password-reset.component.html',
  styleUrl: './password-reset.component.css',
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class PasswordResetComponent {
  constructor(private toastr: ToastrService, private router: Router, private route: ActivatedRoute, private http: HttpClient) {}
  info: string = ''
  code =''
  password = ''
  confirmPassword = ''
  toastContainer: ToastContainerDirective | undefined;
  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.code = params['code'];
     
      if (this.code) {
        console.log('Kod autoryzacyjny:', this.code);
      } else {
        console.log('Brak kodu autoryzacyjnego w URL');
      }
    });
  }

    resetPassword() {
      if( this.password == this.confirmPassword || this.password !==''){
        const targetUrl = 'https://boarderoo-928336702407.europe-central2.run.app/password/resetPassword/';
        const fullUrl = targetUrl;
        console.log(fullUrl);
        console.log(this.code)
        this.http.post<CustomResponse>(fullUrl, {password: this.password, token: this.code} ).subscribe(response => {
          console.log(response);
          this.router.navigate(['/']);
          this.successToast(response.message);
          this.info = response.message;
        }, error => {
          console.error('Błąd:', error);
          this.failToast(error.error?.message);
          this.info=error.error?.message;
        });
      }
      else{
        this.failToast("Podano błędne dane");
      }
        
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
}
