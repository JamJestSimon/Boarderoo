import { Component, EventEmitter, Output, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ToastContainerDirective, ToastrService } from 'ngx-toastr';

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
    currentTab: string = 'data';
    tmpCurrentTab: string = ''
    userName: string | undefined;
    constructor(private toastr: ToastrService) {}

    ngOnInit(): void {
      this.userName = "Adrian"
    }
    
    onClose() {
      this.close.emit(); // Emitowanie zdarzenia
    }
  
    showData() {
      this.currentTab = 'data';
      this.tmpCurrentTab = 'data';
      console.log(this.currentTab);
    }
  
    showPassword() {
      this.currentTab = 'password';
      this.tmpCurrentTab = 'password';
      console.log(this.currentTab);
    }
  
    showOrderHistory() {
      this.currentTab = 'orderHistory';
      this.tmpCurrentTab = 'orderHistory';
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
}
