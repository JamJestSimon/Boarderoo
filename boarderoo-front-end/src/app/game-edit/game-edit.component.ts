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
  @Output() close = new EventEmitter<void>();
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
      const targetUrl = 'https://boarderoo-928336702407.europe-central2.run.app/game';
  
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
          this.http.post<CustomResponse>(targetUrl, newGame).subscribe(
              response => {
                  this.successToast("Pomyślnie dodano grę!");
                  this.notify.emit(true); 
                  this.onClose();
              },
              error => {
                  this.failToast("Wystąpił błąd podczas dodawania gry!");
              }
          );
      } else {
          this.http.put<CustomResponse>(targetUrl, newGame).subscribe(
              response => {
                  this.successToast("Pomyślnie zedytowano grę!");
                  this.notify.emit(true); 
                  this.onClose();
              },
              error => {
                  this.failToast("Wystąpił błąd podczas edycji gry!");
              }
          );
      }
      this.uploadFiles()
  }
  

  onClose() {
    this.close.emit(); 
  }

  selectedFiles: File[] = []; 

  deleteGame() {
    const targetUrl = 'https://boarderoo-928336702407.europe-central2.run.app/game/' + this.selectedCard.id;

    this.http.delete<CustomResponse>(targetUrl).subscribe(
        response => {
            this.successToast("Gra usunięta pomyślnie!");
            this.notify.emit(true); 
            this.onClose();
        },
        error => {
            this.failToast('Wystąpił błąd podczas usuwania!');
        }
    );
}


files: FileList | null = null;

onFileChange(event: Event): void {
  const input = event.target as HTMLInputElement; 

  if (input.files && input.files.length > 0) {
    this.files = input.files; 
    this.selectedFiles = Array.from(this.files); 

    const fileNames = this.selectedFiles.map((file: File) => file.name);


    this.selectedCard.photos = fileNames;
  }
}

uploadFiles(): void {
  if (this.files && this.files.length > 0) {
    Array.from(this.files).forEach((file: File) => {
      const formData = new FormData();
      formData.append('file', file, file.name);

      this.http.post('https://boarderoo-928336702407.europe-central2.run.app/FileUpload/fileupload', formData)
        .subscribe();
    });
  }
}

  failToast(communicate: string) {
    this.toastr.overlayContainer = this.toastContainer;

    this.toastr.error(communicate, 'Błąd', {
      positionClass: 'toast-top-right',
      timeOut: 3000,
      progressBar: true,
      progressAnimation: 'increasing',
    });
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
