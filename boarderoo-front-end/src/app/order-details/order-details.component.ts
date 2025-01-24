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
    this.close.emit();
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
    const targetUrl = 'https://boarderoo-928336702407.europe-central2.run.app/order?id=' + this.selectedOrder.id + '&status=' + this.selectedOrder.status;
    this.http.put<CustomResponse>(targetUrl, null).subscribe(
      response => {
        this.successToast("Zmieniono status zamówienia");
        this.onClose();
      },
      error => {
        this.successToast("Zmiana nie powiodła się");
      }
    );

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
