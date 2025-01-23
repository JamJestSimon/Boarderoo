import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { GameCard } from '../GameCard';

@Component({
  selector: 'app-game-details',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './game-details.component.html',
  styleUrl: './game-details.component.css'
})
export class GameDetailsComponent {

  @Output() close = new EventEmitter<void>(); // Definiujemy zdarzenie

  @Input() selectedCard?: any;

  photos: string[] = []
  showZoomedImage: boolean = false;

  ngOnChanges(): void {
    // Jeśli `selectedCard` się zmienia, aktualizujemy tablicę `photos`
    if (this.selectedCard?.photos) {
      this.photos = this.selectedCard.photos;
    }
    console.log("Zdjęcia: " + this.photos);
  }

  currentPhotoIndex = 0;

  zoomImage() {
    this.showZoomedImage = true;
  }

  // Funkcja do zamknięcia powiększonego obrazu
  closeZoomedImage() {
    this.showZoomedImage = false;
  }

  prevPhoto() {
    this.currentPhotoIndex =
      (this.currentPhotoIndex - 1 + this.photos.length) % this.photos.length;
  }

  nextPhoto() {
    this.currentPhotoIndex =
      (this.currentPhotoIndex + 1) % this.photos.length;
  }

  onClose() {
    console.log(this.selectedCard);
    this.close.emit(); // Emitowanie zdarzenia
  }

  getPhotoUrl(fileName: string): string {
    const baseUrl = 'https://firebasestorage.googleapis.com/v0/b/boarderoo-71469.firebasestorage.app/o/';
    return `${baseUrl}${encodeURIComponent(fileName)}?alt=media`;
  }

  onRent() {
    console.log("Wypożyczono");

    this.selectedCard.action = "Wypożyczono";

    // Typowanie tablicy 'cartItems'
    let cartItems: GameCard[] = [];

    // Pobieramy istniejący koszyk z sessionStorage, jeśli istnieje
    const storedCartItems = sessionStorage.getItem('cartItems');
    if (storedCartItems) {
      // Jeśli koszyk już istnieje, to odczytujemy i parsujemy go
      cartItems = JSON.parse(storedCartItems);
    }

    // Dodajemy nowy przedmiot do tablicy
    cartItems.push(this.selectedCard);

    // Zapisujemy tablicę z powrotem do sessionStorage
    sessionStorage.setItem('cartItems', JSON.stringify(cartItems));

    // Potwierdzenie, że dane zostały zapisane
    console.log('Przedmiot zapisany do koszyka:', this.selectedCard);
    this.onClose();
  }

}
