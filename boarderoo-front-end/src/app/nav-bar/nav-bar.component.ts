import { Component, CUSTOM_ELEMENTS_SCHEMA, Input, ViewChild } from '@angular/core';
import { AccountDetailComponent } from "../account-details/account-details.component";
import { CommonModule } from '@angular/common';
import { CartComponent } from '../cart/cart.component';
import { ToastContainerDirective, ToastrService } from 'ngx-toastr';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
@Component({
  selector: 'app-nav-bar',
  standalone: true,
  imports: [AccountDetailComponent, CommonModule, CartComponent],
  templateUrl: './nav-bar.component.html',
  styleUrl: './nav-bar.component.css',
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class NavBarComponent {
  toastContainer: ToastContainerDirective | undefined;
  isAccountDetailsVisible = false;
  isCartVisible = false;
  @Input() isAdmin?: boolean;
  @ViewChild(CartComponent) cartComponent!: CartComponent;

  toggleAccountDetails() {
    this.isAccountDetailsVisible = !this.isAccountDetailsVisible;
  }

  toggleCart() {
    this.isCartVisible = !this.isCartVisible;
    if (this.isCartVisible && this.cartComponent) {
      this.cartComponent.updateCart();
    }
  }
  constructor(private toastr: ToastrService, private http: HttpClient, private router: Router) { }
  LogOut() {
    if (this.isAdmin === true) {
      localStorage.removeItem('session_token_admin');
      this.router.navigate(['/admin']);
    }
    else {
      localStorage.removeItem('session_token');
      this.router.navigate(['/']);
    }


    this.successToast('Zostałeś pomyślnie wylogowany');
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
