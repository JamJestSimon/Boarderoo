import { Component, CUSTOM_ELEMENTS_SCHEMA, ViewChild } from '@angular/core';
import { NavBarComponent } from '../nav-bar/nav-bar.component';
import { CommonModule } from '@angular/common';
import { OrderDetailsComponent } from '../order-details/order-details.component';
import { CustomResponse } from '../CustomResponse';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { OrderCard } from '../OrderCard';
interface User {
  name: string;
  surname: string;
  address: string;
}
interface CustomResponse2 {
  data: User;
}
@Component({
  selector: 'app-orders-list',
  standalone: true,
  imports: [NavBarComponent, CommonModule, OrderDetailsComponent, HttpClientModule, FormsModule],
  templateUrl: './orders-list.component.html',
  styleUrl: './orders-list.component.css',
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class OrdersListComponent {
  constructor(private router: Router, private http: HttpClient) { }
  isAdmin = true;
  pattern = "";
  status = ""
  isOrderDetailsVisible = false;
  orders: OrderCard[] = [];
  selectedOrder = "";
  ordersInput = this.orders;
  @ViewChild(OrderDetailsComponent) child!: OrderDetailsComponent;

  updateOrdersInput(): void {
    this.ordersInput = this.orders.filter(order => order.id.includes(this.pattern.trim()));
    this.ordersInput = this.ordersInput.filter(order => order.status.includes(this.status.trim()));
  }

  orderUser: any
  toggleOrderDetails(order?: any) {
    if (order) {
      this.selectedOrder = order;
      const targetUrl = 'https://boarderoo-928336702407.europe-central2.run.app/user/' + order.user;
      this.http.get<CustomResponse2>(targetUrl).subscribe(
        (response) => {
          const user = response.data
          this.orderUser = {
            name: user.name,
            surname: user.surname,
            address: user.address,
          }
        }
      );
    }
    this.isOrderDetailsVisible = !this.isOrderDetailsVisible;
    if (this.isOrderDetailsVisible) {
    }
  }

  ngOnInit(): void {
    const sessionToken = localStorage.getItem('session_token_admin');
    this.GetOrder();
    if (!sessionToken) {
      this.router.navigate(['/admin']);
    }
  }


  GetOrder() {
    const targetUrl = 'https://boarderoo-928336702407.europe-central2.run.app/order';
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
        this.orders.push(order);
      }

    });
  }

  options = [
    '',
    'Zamówione',
    'Zapłacone',
    'Potwierdzone',
    'Anulowane',
    'Zakończone'
  ];

}
