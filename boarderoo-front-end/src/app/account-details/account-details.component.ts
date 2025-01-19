import { Component, EventEmitter, Output, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ToastContainerDirective, ToastrService } from 'ngx-toastr';
import { HttpClient } from '@angular/common/http';
import { CustomResponse } from '../CustomResponse';
import { request } from 'http';

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
    currentTab: string = 'data';
    tmpCurrentTab: string = ''
    openedTab: string = ''
    newPassword = '';
    retypePassword = '';
    oldPassword = '';
    userName: string | undefined;
    constructor(private toastr: ToastrService, private http: HttpClient) {}
      GetUser() {
        const proxyUrl = 'http://localhost:8080/'; // Lokalny serwer proxy
        const targetUrl = 'https://boarderoo-928336702407.europe-central2.run.app/user/' + this.email;
        const fullUrl = proxyUrl + targetUrl;
        console.log(fullUrl);
        this.http.get<CustomResponse>(fullUrl).subscribe(response => {
            console.log("user: ", response.data);
            const item: any = response.data;

            this.userName = item.name || '';
            this.user = item.name || '';
            this.address = item.address || '';
            this.surname = item.surname || '';
            //this.address

        }, error => {
          console.error('Błąd:', error);
          //this.failToast(error.error?.message);
        });
      }

      UpdateUser() {
        const proxyUrl = 'http://localhost:8080/'; // Lokalny serwer proxy
        const sessionToken = localStorage.getItem('session_token');
        const targetUrl = 'https://boarderoo-928336702407.europe-central2.run.app/user/?email=' + sessionToken + '&name=' + this.userName + '&surname=' + this.surname + '&address=' + this.address;
        const fullUrl = proxyUrl + targetUrl;
  
        this.http.put<CustomResponse>(fullUrl, null).subscribe(
          response => {
            console.log('User updated successfully:', response);
            this.successToast(response.message);
            // Możesz dodać powiadomienie o sukcesie, np. Toast
            // this.successToast('User updated successfully.');
          },
          error => {
            console.error('Error updating user:', error);
            this.failToast("Zmiana nie powiodła się");
            // Możesz dodać powiadomienie o błędzie, np. Toast
            // this.failToast('Failed to update user.');
          }
        );
        this.user = this.userName || '';
      }

    ngOnInit(): void {
      this.email = localStorage.getItem('session_token') || '';
      this.openedTab = 'data';
      this.GetUser()
      
    }

    GetOrder() {
      const proxyUrl = 'http://localhost:8080/'; // Lokalny serwer proxy
      const targetUrl = 'https://boarderoo-928336702407.europe-central2.run.app/order/user/' + this.email;
      const fullUrl = proxyUrl + targetUrl;
      console.log(fullUrl);
      this.http.get<CustomResponse>(fullUrl).subscribe(response => {
        console.log("Zamówienia:", response.data);
      }, error => {
        console.error('Błąd:', error);
        //this.failToast(error.error?.message);
      });
    }

    changePassword(){
      if(this.newPassword === '' || this.retypePassword === '' || this.oldPassword === ''){
        this.failToast("Minimum jedno z pól jest puste!");
      }
      else {
        if(this.newPassword !== this.retypePassword ){
          this.failToast("Nowe hasła nie są identyczne");
        }
        else{
          this.PasswordUpdate();
        }
      }
      
    }

    PasswordUpdate() {
      const proxyUrl = 'http://localhost:8080/'; // Lokalny serwer proxy
      const sessionToken = localStorage.getItem('session_token');
      const targetUrl = 'https://boarderoo-928336702407.europe-central2.run.app/user/password';
      const fullUrl = proxyUrl + targetUrl;

      this.http.put<CustomResponse>(fullUrl, {email: sessionToken, oldPassword: this.oldPassword, newPassword: this.newPassword}).subscribe(
        response => {
          console.log('User updated successfully:', response);
          this.successToast(response.message);
          // Możesz dodać powiadomienie o sukcesie, np. Toast
          // this.successToast('User updated successfully.');
        },
        error => {
          console.error('Error updating user:', error);
          this.failToast("Zmiana nie powiodła się");
          // Możesz dodać powiadomienie o błędzie, np. Toast
          // this.failToast('Failed to update user.');
        }
      );
      this.user = this.userName || '';
    }

    
    onClose() {
      this.close.emit(); // Emitowanie zdarzenia
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
  
    UpdatePassword() {
      const proxyUrl = 'http://localhost:8080/'; // Lokalny serwer proxy
      const sessionToken = localStorage.getItem('session_token');
      const targetUrl = 'https://boarderoo-928336702407.europe-central2.run.app/user/?email=' + sessionToken;
      const fullUrl = proxyUrl + targetUrl;
         
      this.http.put<CustomResponse>(fullUrl, { password: "Adrian"}).subscribe(
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

    orders = [
      {
        number: 'DSJNSADJKN',
        date: '2025-01-15',
        price: 120.50,
        items: [
          { name: 'Produkt A', quantity: 2, price: 40 },
          { name: 'Produkt B', quantity: 1, price: 40.50 }
        ],
        status: 'Anulowane',
        showDetails: false
      },
      {
        number: 'DSJNSADJKN',
        date: '2025-01-15',
        price: 120.50,
        items: [
          { name: 'Produkt A', quantity: 2, price: 40 },
          { name: 'Produkt B', quantity: 1, price: 40.50 }
        ],
        status: 'Zwrócone',
        showDetails: false
      },
      {
        number: 'DSJNSADJKN',
        date: '2025-01-15',
        price: 120.50,
        items: [
          { name: 'Produkt A', quantity: 2, price: 40 },
          { name: 'Produkt B', quantity: 1, price: 40.50 }
        ],
        status: 'Odebrane',
        showDetails: false
      },
      {
        number: 'DSJNSADJKN',
        date: '2025-01-15',
        price: 120.50,
        items: [
          { name: 'Produkt A', quantity: 2, price: 40 },
          { name: 'Produkt B', quantity: 1, price: 40.50 }
        ],
        status: 'Oczekujące na odbiór',
        showDetails: false
      },
      // Kolejne zamówienia
    ];
  
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
