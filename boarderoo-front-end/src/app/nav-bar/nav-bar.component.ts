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
  schemas: [CUSTOM_ELEMENTS_SCHEMA], // Dodajemy schemat
})
export class NavBarComponent {
  toastContainer: ToastContainerDirective | undefined;
  isAccountDetailsVisible = false;
  isCartVisible = false;
  @Input() isAdmin?: boolean;
  @ViewChild(CartComponent) cartComponent!: CartComponent;

  toggleAccountDetails() {
    this.isAccountDetailsVisible = !this.isAccountDetailsVisible;
    console.log(this.isAccountDetailsVisible);
    console.log(this.isAdmin);
  }

  toggleCart() {
    this.isCartVisible = !this.isCartVisible;
    console.log("togle" + this.isCartVisible);
    if (this.isCartVisible && this.cartComponent) {
      this.cartComponent.updateCart();  // Wywołaj dodatkową metodę w CartComponent
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


    // Ewentualnie wyświetlenie komunikatu o wylogowaniu
    this.successToast('Zostałeś pomyślnie wylogowany');
  }

  successToast(communicate: string) {
    this.toastr.overlayContainer = this.toastContainer;

    // Jeśli e-mail nie jest wypełniony, czerwony toast
    this.toastr.success(communicate, 'Sukces', {
      positionClass: 'toast-top-right',
      timeOut: 3000,
      progressBar: true,
      progressAnimation: 'increasing',
    });
  }
}
