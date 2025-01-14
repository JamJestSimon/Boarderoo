import { Component, EventEmitter, Output} from '@angular/core';

@Component({
  selector: 'app-join-us',
  standalone: true,
  templateUrl: './join-us.component.html',
  styleUrls: ['./join-us.component.css']
})
export class JoinUsComponent {
  @Output() close = new EventEmitter<void>(); // Definiujemy zdarzenie

  onClose() {
    this.close.emit(); // Emitowanie zdarzenia
  }
}