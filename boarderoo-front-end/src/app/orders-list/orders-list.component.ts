import { Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { NavBarComponent } from '../nav-bar/nav-bar.component';
import { CommonModule } from '@angular/common';
import { OrderDetailsComponent } from '../order-details/order-details.component';
import { CustomResponse } from '../CustomResponse';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-orders-list',
  standalone: true,
  imports: [NavBarComponent, CommonModule, OrderDetailsComponent, HttpClientModule,FormsModule],
  templateUrl: './orders-list.component.html',
  styleUrl: './orders-list.component.css',
  schemas: [CUSTOM_ELEMENTS_SCHEMA], // Dodajemy schemat
})
export class OrdersListComponent {
  constructor(private router: Router, private toastr: ToastrService, private http: HttpClient) {}
  isAdmin = true;
  pattern = "";
  status = ""
  isOrderDetailsVisible = false;
  selectedOrder = "";
  orders = [
    { number: '#12345', date: '2025-01-15', status: 'Anulowane', amount: '250,00 PLN', statusClass: 'status-pending' },
    { number: '#12346', date: '2025-01-14', status: 'Zrealizowane', amount: '300,00 PLN', statusClass: 'status-completed' },
    { number: '#12347', date: '2025-01-15', status: 'Anulowane', amount: '150,00 PLN', statusClass: 'status-canceled' },
    { number: '#12345', date: '2025-01-15', status: 'Oczekujące', amount: '250,00 PLN', statusClass: 'status-pending' },
    { number: '#12346', date: '2025-01-14', status: 'Zrealizowane', amount: '300,00 PLN', statusClass: 'status-completed' },
    { number: '#12347', date: '2025-01-15', status: 'Anulowane', amount: '150,00 PLN', statusClass: 'status-canceled' },
    { number: '#12345', date: '2025-01-15', status: 'Oczekujące', amount: '250,00 PLN', statusClass: 'status-pending' },
    { number: '#12346', date: '2025-01-14', status: 'Zrealizowane', amount: '300,00 PLN', statusClass: 'status-completed' },
    { number: '#12347', date: '2025-01-15', status: 'Zrealizowane', amount: '150,00 PLN', statusClass: 'status-canceled' },
    { number: '#12345', date: '2025-01-15', status: 'Oczekujące', amount: '250,00 PLN', statusClass: 'status-pending' },
    { number: '#12346', date: '2025-01-14', status: 'Zrealizowane', amount: '300,00 PLN', statusClass: 'status-completed' },
    { number: '#12347', date: '2025-01-15', status: 'Oczekujące', amount: '150,00 PLN', statusClass: 'status-canceled' },
    { number: '#12345', date: '2025-01-15', status: 'Oczekujące', amount: '250,00 PLN', statusClass: 'status-pending' },
    { number: '#12346', date: '2025-01-14', status: 'Zrealizowane', amount: '300,00 PLN', statusClass: 'status-completed' },
    { number: '#12347', date: '2025-01-15', status: 'Anulowane', amount: '150,00 PLN', statusClass: 'status-canceled' },
    { number: '#12345', date: '2025-01-15', status: 'Oczekujące', amount: '250,00 PLN', statusClass: 'status-pending' },
    { number: '#12346', date: '2025-01-14', status: 'Zrealizowane', amount: '300,00 PLN', statusClass: 'status-completed' },
    { number: '#12347', date: '2025-01-15', status: 'Anulowane', amount: '150,00 PLN', statusClass: 'status-canceled' },
  ];
  ordersInput = this.orders;

  updateOrdersInput(): void {
    console.log("1", this.orders);

    this.ordersInput = this.orders.filter(order => order.number.includes(this.pattern.trim()));
    console.log("2", this.ordersInput);
    this.ordersInput = this.ordersInput.filter(order => order.status.includes(this.status.trim()));
    console.log("3", this.ordersInput);
  }

  toggleOrderDetails(order?: any) {
    if (order) {
      this.selectedOrder = order; // Ustawiamy wybraną kartę
    }
    this.isOrderDetailsVisible = !this.isOrderDetailsVisible;
    console.log("togle" + this.isOrderDetailsVisible);
  }
  
    ngOnInit(): void {
      const sessionToken = localStorage.getItem('session_token_admin');
      this.GetOrder();
      if (!sessionToken) {
        // Jeśli token jest pusty, przekierowujemy na stronę główną
        this.router.navigate(['/admin']);
    }
  }
    
  
      GetOrder() {
        const proxyUrl = 'http://localhost:8080/'; // Lokalny serwer proxy
        const targetUrl = 'https://boarderoo-928336702407.europe-central2.run.app/order';
        const fullUrl = proxyUrl + targetUrl;
        console.log(fullUrl);
        this.http.get<CustomResponse>(fullUrl).subscribe(response => {
          console.log("Zamówienia:", response.data);
        }, error => {
          console.error('Błąd:', error);
          //this.failToast(error.error?.message);
        });
      }
  

}