import { Component, EventEmitter, Output, OnInit, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ToastContainerDirective, ToastrService } from 'ngx-toastr';
import { CustomResponse } from '../CustomResponse';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

interface User {
  name: string;
  surname: string;
  address: string;
}
interface CustomResponse2 {
  data: User;
}

@Component({
  selector: 'app-order-details',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './order-details.component.html',
  styleUrl: './order-details.component.css'
})
export class OrderDetailsComponent {

  toastContainer: ToastContainerDirective | undefined;
  @Output() close = new EventEmitter<void>();
  @Input() selectedOrder?: any;
  @Input() orderUser?: any;
  onClose() {
    this.close.emit(); // Emitowanie zdarzenia
    console.log(this.selectedOrder)
  }
  name = ""
  surname = ''
  address = ''

  options = [
    'Zamówione',
    'Zapłacone',
    'Potwierdzone',
    'Anulowane',
    'Zakończone'
  ];
  constructor(private toastr: ToastrService, private http: HttpClient, private router: Router) { }

  changeStatus() {
    const proxyUrl = ''; // Lokalny serwer proxy

    const targetUrl = 'https://boarderoo-928336702407.europe-central2.run.app/order?id=' + this.selectedOrder.id + '&status=' + this.selectedOrder.status;
    const fullUrl = proxyUrl + targetUrl;

    this.http.put<CustomResponse>(fullUrl, null).subscribe(
      response => {
        console.log(response);
        this.successToast(response.message);
        // Możesz dodać powiadomienie o sukcesie, np. Toast
        // this.successToast('User updated successfully.');
      },
      error => {
        console.error('Error updating user:', error);
        this.successToast("Zmiana nie powiodła się");
        // Możesz dodać powiadomienie o błędzie, np. Toast
        // this.failToast('Failed to update user.');
      }
    );

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
