import { Component, EventEmitter, Output, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ToastContainerDirective, ToastrService } from 'ngx-toastr';
import { HttpClient } from '@angular/common/http';
import { CustomResponse } from '../CustomResponse';
import { request } from 'http';
import { OrderCard } from '../OrderCard';

@Component({
  selector: 'app-account-details',
  standalone: true,
  templateUrl: './account-details.component.html',
  styleUrls: ['./account-details.component.css'],
  imports: [CommonModule, FormsModule],
})
export class AccountDetailComponent {
  @Output() close = new EventEmitter<void>(); // Definiujemy zdarzenie

  toastContainer: ToastContainerDirective | undefined;
  email: string = '';  // Dodajemy zmienną na e-mail
  user: string = '';  // Dodajemy zmienną na e-mail
  surname: string = '';  // Dodajemy zmienną na e-mail
  address: string = '';  // Dodajemy zmienną na e-mail
  orders: OrderCard[] = [];
  currentTab: string = 'data';
  tmpCurrentTab: string = ''
  openedTab: string = ''
  newPassword = '';
  retypePassword = '';
  oldPassword = '';
  userName: string | undefined;
  constructor(private toastr: ToastrService, private http: HttpClient) { }
  GetUser() {
    const targetUrl = 'https://boarderoo-928336702407.europe-central2.run.app/user/' + this.email;
    this.http.get<CustomResponse>(targetUrl).subscribe(response => {
      const item: any = response.data;

      this.userName = item.name || '';
      this.user = item.name || '';
      this.address = item.address || '';
      this.surname = item.surname || '';

    });
  }

  UpdateUser() {
    const sessionToken = localStorage.getItem('session_token');
    const targetUrl = 'https://boarderoo-928336702407.europe-central2.run.app/user/?email=' + sessionToken + '&name=' + this.userName + '&surname=' + this.surname + '&address=' + this.address;

    this.http.put<CustomResponse>(targetUrl, null).subscribe(
      response => {
        this.successToast("Pomyślnie zaktualizowano usera!");
      },
      error => {
        this.failToast("Zmiana nie powiodła się");
      }
    );
    this.user = this.userName || '';
  }

  ngOnInit(): void {
    this.email = localStorage.getItem('session_token') || '';
    this.openedTab = 'data';
    this.GetUser()

  }



  PasswordUpdate() {
    const sessionToken = localStorage.getItem('session_token');
    const targetUrl = 'https://boarderoo-928336702407.europe-central2.run.app/user/password';

    this.http.put<CustomResponse>(targetUrl, { email: sessionToken, oldPassword: this.oldPassword, newPassword: this.newPassword }).subscribe(
      response => {
        this.successToast("Hasło zostało zmienione!");
      },
      error => {
        this.failToast("Wystąpił błąd!");
      }
    );
    this.user = this.userName || '';
  }


  GetOrder() {
    this.orders = []
    const targetUrl = 'https://boarderoo-928336702407.europe-central2.run.app/order/user/' + this.email;
    this.http.get<CustomResponse>(targetUrl).subscribe(response => {
      for (let i = 0; i < response.data.length; i++) {
        const item: any = response.data[i];
        const order: OrderCard = {
          id: item.id,
          start: new Date(item.start).toISOString().split('T')[0] || '2025-01-01T00:00:00.000Z',
          end: new Date(item.end).toISOString().split('T')[0] || '2025-01-01T00:00:00.000Z',
          status: item.status || 'Brak statusu',
          user: item.user || 'Brak użytkownika',
          paymentNumber: item.paymentNumber,
          items: item.items || [],
          price: item.price || 0,
          showDetails: false,
        };

        console.log(order);

        this.orders.push(order);
      }

    });
  }


  onClose() {
    this.close.emit();
  }

  showData() {
    this.currentTab = 'data';
    this.tmpCurrentTab = 'data';
    this.openedTab = 'data';
    console.log(this.currentTab);
    this.GetUser();
  }

  showPassword() {
    this.currentTab = 'password';
    this.tmpCurrentTab = 'password';
    this.openedTab = 'password';
    console.log(this.currentTab);
  }

  showOrderHistory() {
    this.currentTab = 'orderHistory';
    this.tmpCurrentTab = 'orderHistory';
    this.openedTab = 'orderHistory';
    this.GetOrder();
    console.log(this.currentTab);
  }

  setHovered() {
    this.tmpCurrentTab = this.currentTab;
    this.currentTab = '';
  }

  resetHovered() {
    this.currentTab = this.tmpCurrentTab;
    this.tmpCurrentTab = '';
    console.log("rehover: " + this.currentTab)
  }

  changeStatus(order: any) {
    const targetUrl = 'https://boarderoo-928336702407.europe-central2.run.app/order?id=' + order.id + '&status=Anulowane';
    this.http.put<CustomResponse>(targetUrl, null).subscribe(
      response => {
        console.log(response);
        this.successToast("Pomyślnie zmieniono status?");
      },
      error => {
        this.failToast("Zmiana nie powiodła się");
      }
    );
    this.GetOrder();
  }



  toggleDetails(order: any) {
    order.showDetails = !order.showDetails;
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
