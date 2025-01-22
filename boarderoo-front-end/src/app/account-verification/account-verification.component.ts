import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CustomResponse } from '../CustomResponse';
import { ToastContainerDirective, ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-account-verification',
  standalone: true,
  imports: [FormsModule, CommonModule,HttpClientModule],
  templateUrl: './account-verification.component.html',
  styleUrl: './account-verification.component.css'
})
export class AccountVerificationComponent {

  constructor(private toastr: ToastrService, private router: Router, private route: ActivatedRoute, private http: HttpClient) {}
    toastContainer: ToastContainerDirective | undefined;
  info: string = ''
  code = ''

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.code = params['code'];
     
      if (this.code) {
        this.activateAccount();
        console.log('Kod autoryzacyjny:', this.code);
      } else {
        console.log('Brak kodu autoryzacyjnego w URL');
      }
    });
  }


  activateAccount() {
      const proxyUrl = "https://cors-anywhere.herokuapp.com/"
      const targetUrl = 'https://boarderoo-928336702407.europe-central2.run.app/verify/'+ this.code;
      const fullUrl = proxyUrl + targetUrl;
      console.log(fullUrl);
      console.log(this.code)
      this.http.post<CustomResponse>(fullUrl, null ).subscribe(response => {
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
