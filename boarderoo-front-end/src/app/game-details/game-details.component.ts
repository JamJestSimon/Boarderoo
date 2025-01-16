import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-game-details',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './game-details.component.html',
  styleUrl: './game-details.component.css'
})
export class GameDetailsComponent {

    @Output() close = new EventEmitter<void>(); // Definiujemy zdarzenie

  
    onClose() {
      this.close.emit(); // Emitowanie zdarzenia
    }
}
