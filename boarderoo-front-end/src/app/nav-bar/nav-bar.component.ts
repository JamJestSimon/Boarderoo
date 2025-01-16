import { Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { AccountDetailComponent } from "../account-details/account-details.component";
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-nav-bar',
  standalone: true,
  imports: [AccountDetailComponent, CommonModule],
  templateUrl: './nav-bar.component.html',
  styleUrl: './nav-bar.component.css',
  schemas: [CUSTOM_ELEMENTS_SCHEMA], // Dodajemy schemat
})
export class NavBarComponent {

  isAccountDetailsVisible = false;

  toggleAccountDetails() {
    this.isAccountDetailsVisible = !this.isAccountDetailsVisible;
    console.log(this.isAccountDetailsVisible);
  }


}
