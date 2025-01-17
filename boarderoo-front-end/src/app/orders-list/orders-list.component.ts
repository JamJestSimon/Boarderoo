import { Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { NavBarComponent } from '../nav-bar/nav-bar.component';
import { CommonModule } from '@angular/common';
import { OrderDetailsComponent } from '../order-details/order-details.component';

@Component({
  selector: 'app-orders-list',
  standalone: true,
  imports: [NavBarComponent, CommonModule, OrderDetailsComponent],
  templateUrl: './orders-list.component.html',
  styleUrl: './orders-list.component.css',
  schemas: [CUSTOM_ELEMENTS_SCHEMA], // Dodajemy schemat
})
export class OrdersListComponent {
  isAdmin = true;
  isOrderDetailsVisible = false;
  selectedOrder = "";
  orders = [
    { number: '#12345', date: '2025-01-15', status: 'Oczekujące', amount: '250,00 PLN', statusClass: 'status-pending' },
    { number: '#12346', date: '2025-01-14', status: 'Zrealizowane', amount: '300,00 PLN', statusClass: 'status-completed' },
    { number: '#12347', date: '2025-01-15', status: 'Anulowane', amount: '150,00 PLN', statusClass: 'status-canceled' },
    { number: '#12345', date: '2025-01-15', status: 'Oczekujące', amount: '250,00 PLN', statusClass: 'status-pending' },
    { number: '#12346', date: '2025-01-14', status: 'Zrealizowane', amount: '300,00 PLN', statusClass: 'status-completed' },
    { number: '#12347', date: '2025-01-15', status: 'Anulowane', amount: '150,00 PLN', statusClass: 'status-canceled' },
    { number: '#12345', date: '2025-01-15', status: 'Oczekujące', amount: '250,00 PLN', statusClass: 'status-pending' },
    { number: '#12346', date: '2025-01-14', status: 'Zrealizowane', amount: '300,00 PLN', statusClass: 'status-completed' },
    { number: '#12347', date: '2025-01-15', status: 'Anulowane', amount: '150,00 PLN', statusClass: 'status-canceled' },
    { number: '#12345', date: '2025-01-15', status: 'Oczekujące', amount: '250,00 PLN', statusClass: 'status-pending' },
    { number: '#12346', date: '2025-01-14', status: 'Zrealizowane', amount: '300,00 PLN', statusClass: 'status-completed' },
    { number: '#12347', date: '2025-01-15', status: 'Anulowane', amount: '150,00 PLN', statusClass: 'status-canceled' },
    { number: '#12345', date: '2025-01-15', status: 'Oczekujące', amount: '250,00 PLN', statusClass: 'status-pending' },
    { number: '#12346', date: '2025-01-14', status: 'Zrealizowane', amount: '300,00 PLN', statusClass: 'status-completed' },
    { number: '#12347', date: '2025-01-15', status: 'Anulowane', amount: '150,00 PLN', statusClass: 'status-canceled' },
    { number: '#12345', date: '2025-01-15', status: 'Oczekujące', amount: '250,00 PLN', statusClass: 'status-pending' },
    { number: '#12346', date: '2025-01-14', status: 'Zrealizowane', amount: '300,00 PLN', statusClass: 'status-completed' },
    { number: '#12347', date: '2025-01-15', status: 'Anulowane', amount: '150,00 PLN', statusClass: 'status-canceled' },
  ];


  toggleOrderDetails(order?: any) {
    if (order) {
      this.selectedOrder = order; // Ustawiamy wybraną kartę
    }
    this.isOrderDetailsVisible = !this.isOrderDetailsVisible;
    console.log("togle" + this.isOrderDetailsVisible);
  }

}
