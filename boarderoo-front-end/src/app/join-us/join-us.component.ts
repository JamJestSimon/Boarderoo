import { Component, EventEmitter, Output } from '@angular/core';
import { ToastContainerDirective, ToastrService } from 'ngx-toastr';
import { FormsModule } from '@angular/forms'; // Importowanie FormsModule

@Component({
  selector: 'app-join-us',
  standalone: true,
  imports: [FormsModule],  // Dodajemy FormsModule w imports
  templateUrl: './join-us.component.html',
  styleUrls: ['./join-us.component.css'],
})
export class JoinUsComponent {
  @Output() close = new EventEmitter<void>(); // Definiujemy zdarzenie

  toastContainer: ToastContainerDirective | undefined;
  email: string = '';  // Dodajemy zmienną na e-mail

  constructor(private toastr: ToastrService) {}

  onClose() {
    this.close.emit(); // Emitowanie zdarzenia
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
