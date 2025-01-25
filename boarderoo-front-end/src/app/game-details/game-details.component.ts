import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { GameCard } from '../GameCard';
import { ToastContainerDirective, ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-game-details',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './game-details.component.html',
  styleUrl: './game-details.component.css'
})
export class GameDetailsComponent {

  @Output() close = new EventEmitter<void>();
  toastContainer: ToastContainerDirective | undefined;
  @Input() selectedCard?: any;

  constructor(private toastr: ToastrService) { }
  photos: string[] = []
  showZoomedImage: boolean = false;

  ngOnChanges(): void {
    if (this.selectedCard?.photos) {
      this.photos = this.selectedCard.photos;
    }
  }

  currentPhotoIndex = 0;

  zoomImage() {
    this.showZoomedImage = true;
  }

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
    this.close.emit();
  }

  getPhotoUrl(fileName: string): string {
    const baseUrl = 'https://firebasestorage.googleapis.com/v0/b/boarderoo-71469.firebasestorage.app/o/files%2F';
    return `${baseUrl}${encodeURIComponent(fileName)}?alt=media`;
  }

  onRent() {

    this.selectedCard.action = "Wypożyczono";

    let cartItems: GameCard[] = [];

    const storedCartItems = sessionStorage.getItem('cartItems');
    if (storedCartItems) {
      cartItems = JSON.parse(storedCartItems);
    }

    cartItems.push(this.selectedCard);

    sessionStorage.setItem('cartItems', JSON.stringify(cartItems));
    this.successToast("Gra dodana do koszyka!")
    this.onClose();
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
