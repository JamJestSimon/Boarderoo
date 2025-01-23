import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { GameCard } from '../GameCard';
import { ToastContainerDirective, ToastrService } from 'ngx-toastr';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { CustomResponse } from '../CustomResponse';


@Component({
  selector: 'app-game-edit',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './game-edit.component.html',
  styleUrl: './game-edit.component.css'
})
export class GameEditComponent {
  @Output() close = new EventEmitter<void>(); // Definiujemy zdarzenie
  @Output() notify = new EventEmitter<boolean>();
  toastContainer: ToastContainerDirective | undefined;

  @Input() selectedCard: GameCard = {
    id: '',
    title: '',
    publisher: '',
    category: '',
    price: 0,
    year: 2015,
    description: '',
    photos: [],
    ageFrom: 0,
    ageTo: 100,
    playersFrom: 1,
    playersTo: 8,
    action: ''
  };

  options = ['Strategiczne', 'Przygodowe', 'Ekonomiczne', 'Logiczne', 'Rodzinne', 'Fantasy', 'Imprezowe', 'Dla dzieci'];
  publishers = ['Fantasy Flight Games', 'Asmodee', 'Rebel', 'Days of Wonder', 'Ravensburger', 'Plaid Hat Games', 'Stonemaier Games', 'Hobby World', 'Matagot', 'Z-Man Games'];

  constructor(private toastr: ToastrService, private http: HttpClient, private router: Router) { }
  imageUrl: string | null = null;



    editGame() {
      console.log(this.selectedCard);
      const proxyUrl = ''; // Lokalny serwer proxy
      const targetUrl = 'https://boarderoo-928336702407.europe-central2.run.app/game';
      const fullUrl = proxyUrl + targetUrl;
  
      const newGame = {
          image: this.selectedCard.photos,
          id: this.selectedCard.id,
          name: this.selectedCard.title,
          type: this.selectedCard.category,
          price: this.selectedCard.price,
          description: this.selectedCard.description,
          publisher: this.selectedCard.publisher,
          players_number: this.selectedCard.playersFrom + " - " + this.selectedCard.playersTo,
          year: this.selectedCard.year.toString(),
          rating: this.selectedCard.ageFrom + " - " + this.selectedCard.ageTo,
          enabled: true,
          availible_copies: 10,
      };
  
      if (this.selectedCard.action === 'add') {
          this.http.post<CustomResponse>(fullUrl, newGame).subscribe(
              response => {
                  console.log(response);
                  this.successToast(response.message);
                  this.notify.emit(true); // Powiadomienie o zakończeniu operacji
                  this.onClose();
              },
              error => {
                  console.error('Błąd:', error);
                  this.failToast(error.error?.message);
              }
          );
      } else {
          this.http.put<CustomResponse>(fullUrl, newGame).subscribe(
              response => {
                  console.log(response);
                  this.successToast(response.message);
                  this.notify.emit(true); // Powiadomienie o zakończeniu operacji
                  this.onClose();
              },
              error => {
                  console.error('Błąd:', error);
                  this.failToast(error.error?.message);
              }
          );
      }
      this.uploadFiles()
  }
  

  onClose() {
    console.log(this.selectedCard);
    this.close.emit(); // Emitowanie zdarzenia
  }

  selectedFiles: File[] = []; // Zmienna do przechowywania plików

  deleteGame() {
    const targetUrl = 'https://boarderoo-928336702407.europe-central2.run.app/game/' + this.selectedCard.id;

    this.http.delete<CustomResponse>(targetUrl).subscribe(
        response => {
            console.log(response);
            this.successToast(response.message);
            this.notify.emit(true); // Powiadomienie o zakończeniu operacji
            this.onClose();
        },
        error => {
            console.error('Błąd:', error);
            this.failToast(error.error?.message || 'Wystąpił błąd.');
        }
    );
}


files: FileList | null = null;

onFileChange(event: Event): void {
  const input = event.target as HTMLInputElement; // Rzutowanie na HTMLInputElement

  if (input.files && input.files.length > 0) {
    this.files = input.files; // Przypisanie do zmiennej
    this.selectedFiles = Array.from(this.files); // Konwersja FileList na tablicę File[]

    // Mapowanie nazw plików
    const fileNames = this.selectedFiles.map((file: File) => file.name);
    console.log(fileNames); // Wyświetla nazwy plików w konsoli

    // Przypisanie nazw plików do `selectedCard.photos`
    this.selectedCard.photos = fileNames;
  } else {
    console.warn('No files selected!');
  }
}

uploadFiles(): void {
  if (this.files && this.files.length > 0) {
    const formData = new FormData();

    // Dodajemy wszystkie pliki z listy `files`
    Array.from(this.files).forEach((file: File) => {
      formData.append('files', file, file.name);  // 'files' to nazwa pola na backendzie
    });
    console
    // Wysyłamy wszystkie pliki w jednym żądaniu
    this.http.post('https://boarderoo-928336702407.europe-central2.run.app/', formData)
      .subscribe(
        response => console.log('Upload success:', response),
        error => console.error('Upload error:', error)
      );
  } else {
    console.error('No files selected');
  }
}

  failToast(communicate: string) {
    this.toastr.overlayContainer = this.toastContainer;

    // Jeśli e-mail nie jest wypełniony, czerwony toast
    this.toastr.error(communicate, 'Błąd', {
      positionClass: 'toast-top-right',
      timeOut: 3000,
      progressBar: true,
      progressAnimation: 'increasing',
    });
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
