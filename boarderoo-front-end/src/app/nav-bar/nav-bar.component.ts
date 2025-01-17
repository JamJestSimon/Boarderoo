import { Component, CUSTOM_ELEMENTS_SCHEMA, Input } from '@angular/core';
import { AccountDetailComponent } from "../account-details/account-details.component";
import { CommonModule } from '@angular/common';
import { CartComponent } from '../cart/cart.component';
@Component({
  selector: 'app-nav-bar',
  standalone: true,
  imports: [AccountDetailComponent, CommonModule, CartComponent],
  templateUrl: './nav-bar.component.html',
  styleUrl: './nav-bar.component.css',
  schemas: [CUSTOM_ELEMENTS_SCHEMA], // Dodajemy schemat
})
export class NavBarComponent {

  isAccountDetailsVisible = false;
  isCartVisible = false;
  @Input() isAdmin?: boolean;

  toggleAccountDetails() {
    this.isAccountDetailsVisible = !this.isAccountDetailsVisible;
    console.log(this.isAccountDetailsVisible);
    console.log(this.isAdmin);
  }

  toggleCart() {
    this.isCartVisible = !this.isCartVisible;
    console.log("togle" + this.isCartVisible);
  }


}
