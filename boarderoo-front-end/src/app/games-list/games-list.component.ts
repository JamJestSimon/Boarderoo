import { Component, CUSTOM_ELEMENTS_SCHEMA, HostListener } from '@angular/core';
import { NavBarComponent } from "../nav-bar/nav-bar.component";
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { GameEditComponent } from '../game-edit/game-edit.component';
import { GameCard } from '../GameCard';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { CustomResponse } from '../CustomResponse';
import { Router } from '@angular/router';

@Component({
  selector: 'app-games-list',
  standalone: true,
  imports: [NavBarComponent, CommonModule, FormsModule, GameEditComponent, HttpClientModule],
  templateUrl: './games-list.component.html',
  styleUrl: './games-list.component.css',
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class GamesListComponent {
  isGameEditVisible = false;
  selectedCard = {
    id: "",
    title: "",
    publisher: "",
    category: "",
    price: 0,
    year: 2025,
    description: "",
    photos: [],
    ageFrom: 0,
    ageTo: 100,
    playersFrom: 1,
    playersTo: 8,
    action: 'add'
  };
  isAdmin = true;

  toggleGameEdit(card?: any) {
    if (card) {
      this.selectedCard = card;
      this.selectedCard.action = "update";
    }
    else {
      this.selectedCard = {
        id: "",
        title: "",
        publisher: "",
        category: "",
        price: 0,
        year: 2025,
        description: "",
        photos: [],
        ageFrom: 0,
        ageTo: 100,
        playersFrom: 1,
        playersTo: 8,
        action: 'add'
      };
    }

    this.isGameEditVisible = !this.isGameEditVisible;
  }

  onEditGameEnd(edited: boolean) {
    if (edited) {

      this.GetGames();
    }
  }


  constructor(private router: Router, private http: HttpClient) { }

  ngOnInit(): void {
    this.GetGames();
    const sessionToken = localStorage.getItem('session_token_admin');
    if (!sessionToken) {
      this.router.navigate(['/admin']);
    }
  }


  cards: GameCard[] = []
  GetGames() {
    this.cards = [];
    const targetUrl = 'https://boarderoo-928336702407.europe-central2.run.app/game';
    this.http.get<CustomResponse>(targetUrl).subscribe(response => {
      for (let i = 0; i < response.data.length; i++) {
        const item: any = response.data[i];
        const gameCard: GameCard = {
          id: item.id,
          title: item.name || 'Brak tytułu',
          publisher: item.publisher || 'Brak wydawcy',
          category: item.type || 'Brak kategorii',
          price: item.price,
          year: parseInt(item.year.toString(), 10) || 0,
          description: item.description || 'Brak opisu',
          photos: item.image,
          ageFrom: parseInt(item.rating.trim().split('-')[0], 10),
          ageTo: parseInt(item.rating.trim().split('-')[1], 10),
          playersFrom: parseInt(item.players_number.trim().split('-')[0], 10) || 1,
          playersTo: parseInt(item.players_number.trim().split('-')[1], 10) || 2,
          action: ''
        };

        this.cards.push(gameCard);
      }
    });
  }

  handleGameEditClose() {
    this.toggleGameEdit();
  }

  getPhotoUrl(fileName: string): string {
    const baseUrl = 'https://firebasestorage.googleapis.com/v0/b/boarderoo-71469.firebasestorage.app/o/files%2F';
    return `${baseUrl}${encodeURIComponent(fileName)}?alt=media`;
  }

}
