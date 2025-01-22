import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { GameCard } from '../GameCard';
import { ToastContainerDirective, ToastrService } from 'ngx-toastr';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { CustomResponse } from '../CustomResponse';
const firebaseConfig = {
  apiKey: "AIzaSyC_G5J-I5R0h_dcAkq8SG93GJjzwHQgLSs",
  authDomain: "boarderoo-71469.firebaseapp.com",
  projectId: "boarderoo-71469",
  storageBucket: "boarderoo-71469.firebasestorage.app",
  messagingSenderId: "928336702407",
  appId: "1:928336702407:web:5afa9c70251d94fece07e2"
};

@Component({
  selector: 'app-game-edit',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './game-edit.component.html',
  styleUrl: './game-edit.component.css'
})
export class GameEditComponent {
    @Output() close = new EventEmitter<void>(); // Definiujemy zdarzenie
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

    constructor(private toastr: ToastrService, private http: HttpClient, private router: Router) {}



    editGame() {
      console.log(this.selectedCard)
        const proxyUrl = "https://cors-anywhere.herokuapp.com/"
        const targetUrl = 'https://boarderoo-928336702407.europe-central2.run.app/game';
        const fullUrl = proxyUrl + targetUrl;
        console.log(fullUrl);
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
      }
      console.log(this.selectedCard.action)
        if(this.selectedCard.action === 'add'){
          this.http.post<CustomResponse>(fullUrl, newGame).subscribe(response => {
            console.log(response);
            this.successToast(response.message);
          }, error => {
            console.error('Błąd:', error);
            this.failToast(error.error?.message);
          });
        }
        else{
          this.http.put<CustomResponse>(fullUrl, newGame).subscribe(response => {
            console.log(response);
            this.successToast(response.message);
          }, error => {
            console.error('Błąd:', error);
            this.failToast(error.error?.message);
          });
        }
        this.onClose();
      }

    onClose() {
      console.log(this.selectedCard);
      this.close.emit(); // Emitowanie zdarzenia
    }

  onSubmit() {
    // Implementacja zapisywania gry
    console.log('Zapisano grę:', this.selectedCard);
  }

  onFileChange(event: any) {
    const files: FileList = event.target.files;  // Typowanie files jako FileList
    if (files.length) {
      // Mapowanie plików na ich nazwy
      const fileNames = Array.from(files).map((file: File) => file.name);
      console.log(fileNames); // Tylko nazwy plików
      this.selectedCard.photos = fileNames; // Zapisujemy nazwy plików w selectedCard
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
