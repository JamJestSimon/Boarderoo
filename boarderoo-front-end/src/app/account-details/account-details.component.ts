import { Component, EventEmitter, Output, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ToastContainerDirective, ToastrService } from 'ngx-toastr';
import { HttpClient } from '@angular/common/http';
import { CustomResponse } from '../CustomResponse';

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
            this.email = item.email || '';
            this.surname = item.surname || '';
            //this.address

        }, error => {
          console.error('Błąd:', error);
          //this.failToast(error.error?.message);
        });
      }

      UpdateUser() {
        const proxyUrl = 'http://localhost:8080/'; // Lokalny serwer proxy
        const targetUrl = 'https://boarderoo-928336702407.europe-central2.run.app/user/' + this.email;
        const fullUrl = proxyUrl + targetUrl;
      
        // Dane do zaktualizowania
        const updateData = {
          id: "2N9Wybuw879CZOD4pMLC",
          email: "string",
          isVerified: true,
          location: {
            latitude: 0,
            longitude: 0
          },
          name: this.userName,
          password: "string",
          surname: this.surname,
          token: "string",
          tokenCreationDate: {}
        }
        
        console.log('Request URL:', fullUrl);
        console.log('Request Data:', updateData);
      
        this.http.put(fullUrl, updateData).subscribe(
          response => {
            console.log('User updated successfully:', response);
            // Możesz dodać powiadomienie o sukcesie, np. Toast
            // this.successToast('User updated successfully.');
          },
          error => {
            console.error('Error updating user:', error);
            // Możesz dodać powiadomienie o błędzie, np. Toast
            // this.failToast('Failed to update user.');
          }
        );

        this.GetUser();
      }

    ngOnInit(): void {
      this.email = localStorage.getItem('session_token') || '';
      this.openedTab = 'data';

      
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
  
    showSuccess() {
      this.toastr.overlayContainer = this.toastContainer;
      
      // Sprawdzamy, czy pole email jest wypełnione
      if (this.email.trim() !== '') {
        // Jeśli e-mail jest wypełniony, zielony toast
        this.toastr.success('Mail do resetu hasła został wysłany!', 'Sukces', {
          positionClass: 'toast-top-right',
          timeOut: 3000,
          progressBar: true,
          progressAnimation: 'increasing',
        });
      } else {
        // Jeśli e-mail nie jest wypełniony, czerwony toast
        this.toastr.error('Proszę podać e-mail!', 'Błąd', {
          positionClass: 'toast-top-right',
          timeOut: 3000,
          progressBar: true,
          progressAnimation: 'increasing',
        });
      }
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
}
