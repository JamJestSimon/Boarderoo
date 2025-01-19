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


    GetOrder() {
        this.orders = []
        const proxyUrl = 'http://localhost:8080/'; // Lokalny serwer proxy
        const targetUrl = 'https://boarderoo-928336702407.europe-central2.run.app/order/user/' + this.email;
        const fullUrl = proxyUrl + targetUrl;
        console.log(fullUrl);
        this.http.get<CustomResponse>(fullUrl).subscribe(response => {
          // Przechodzimy przez każdy element w odpowiedzi
          for (let i = 0; i < response.data.length; i++) {
            const item: any = response.data[i];
            console.log(item); // Logujemy item do konsoli
    
            // Tworzymy obiekt Order
            const order: OrderCard = {
              id: item.id,
              start: new Date(item.start).toISOString().split('T')[0] || '2025-01-01T00:00:00.000Z', // Domyślna data
              end: new Date(item.end).toISOString().split('T')[0] || '2025-01-01T00:00:00.000Z',    // Domyślna data
              status: item.status || 'Brak statusu',
              user: item.user || 'Brak użytkownika',
              items: item.items || [], // Jeżeli brak przedmiotów, pusty array
              price: item.price || 0,   // Jeżeli brak ceny, 0
              showDetails: false,
            };
    
            console.log(order); // Logujemy stworzony obiekt zamówienia
    
            // Dodajemy obiekt order do tablicy orders
            this.orders.push(order);
          }
    
        }, error => {
          console.error('Błąd:', error);
          //this.failToast(error.error?.message);
        });
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
         
      this.http.put<CustomResponse>(fullUrl, { password: this.newPassword}).subscribe(
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

    changeStatus(order:any){
      const proxyUrl = 'http://localhost:8080/'; // Lokalny serwer proxy
  
            const targetUrl = 'https://boarderoo-928336702407.europe-central2.run.app/order?id=' + order.id + '&status=Anulowane';
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
