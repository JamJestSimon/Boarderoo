import { Component, CUSTOM_ELEMENTS_SCHEMA, ViewChild } from '@angular/core';
import { NavBarComponent } from '../nav-bar/nav-bar.component';
import { CommonModule } from '@angular/common';
import { OrderDetailsComponent } from '../order-details/order-details.component';
import { CustomResponse } from '../CustomResponse';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { OrderCard } from '../OrderCard';

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
  orders: OrderCard[] = [];
  selectedOrder = "";
  ordersInput = this.orders;
  @ViewChild(OrderDetailsComponent) child!: OrderDetailsComponent;

  updateOrdersInput(): void {
    console.log("1", this.orders);

    this.ordersInput = this.orders.filter(order => order.id.includes(this.pattern.trim()));
    console.log("2", this.ordersInput);
    this.ordersInput = this.ordersInput.filter(order => order.status.includes(this.status.trim()));
    console.log("3", this.ordersInput);
  }

  toggleOrderDetails(order?: any) {
    if (order) {
      this.selectedOrder = order; // Ustawiamy wybraną kartę
    }
    this.isOrderDetailsVisible = !this.isOrderDetailsVisible;
    if(this.isOrderDetailsVisible){
      this.child.getUser();
    }
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
        const targetUrl = 'https://boarderoo-928336702407.europe-central2.run.app/order';
        const fullUrl = targetUrl;
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
  

}
