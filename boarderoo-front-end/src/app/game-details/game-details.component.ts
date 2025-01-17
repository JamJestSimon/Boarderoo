import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';

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
          this.photos = this.selectedCard.photos.split(',');
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
  
    onRent() {

      console.log("Wypożyczono");
    }
}
