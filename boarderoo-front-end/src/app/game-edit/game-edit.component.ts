import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { GameCard } from '../GameCard';

@Component({
  selector: 'app-game-edit',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './game-edit.component.html',
  styleUrl: './game-edit.component.css'
})
export class GameEditComponent {
    @Output() close = new EventEmitter<void>(); // Definiujemy zdarzenie

    @Input() selectedCard: GameCard = {
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

    options: string[] = ['Strategia', 'Przygodowa', 'Akcja', 'Rodzinna', 'Karcianka'];  // Możesz dodać swoje kategorie


    onClose() {
      console.log(this.selectedCard);
      this.close.emit(); // Emitowanie zdarzenia
    }

  onSubmit() {
    // Implementacja zapisywania gry
    console.log('Zapisano grę:', this.selectedCard);
  }

  onFileChange(event: any) {
    const files = event.target.files;
    if (files.length) {
      // Przechowywanie zdjęć w selectedCard
      this.selectedCard.photos = Array.from(files);
    }
  }
}
