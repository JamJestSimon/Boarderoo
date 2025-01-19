import { Component, CUSTOM_ELEMENTS_SCHEMA, HostListener } from '@angular/core';
import { NavBarComponent } from "../nav-bar/nav-bar.component";
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { GameEditComponent } from '../game-edit/game-edit.component';
import { GameCard } from '../GameCard';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { CustomResponse } from '../CustomResponse';
import { Router } from '@angular/router';

@Component({
  selector: 'app-games-list',
  standalone: true,
  imports: [NavBarComponent, CommonModule, FormsModule, GameEditComponent,HttpClientModule],
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
      // Tworzymy nowy obiekt, który rozbija dane z card
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
  
    // Przełączamy widoczność formularza
    this.isGameEditVisible = !this.isGameEditVisible;
    if(this.isGameEditVisible === false){
     // window.location.reload();
    }
  }


  constructor(private router: Router, private toastr: ToastrService, private http: HttpClient) {}
    
      ngOnInit(): void {
        this.GetGames();
        const sessionToken = localStorage.getItem('session_token_admin');
        if (!sessionToken) {
          // Jeśli token jest pusty, przekierowujemy na stronę główną
          this.router.navigate(['/admin']);
      }
    }


  cards: GameCard[] = []
    GetGames() {
      this.cards = [];
      const proxyUrl = 'http://localhost:8080/'; // Lokalny serwer proxy
      const targetUrl = 'https://boarderoo-928336702407.europe-central2.run.app/game';
      const fullUrl = proxyUrl + targetUrl;
      console.log(fullUrl);
      this.http.get<CustomResponse>(fullUrl).subscribe(response => {
        for (let i = 0; i < response.data.length; i++) {
          const item: any = response.data[i];
          console.log(item);
          console.log(typeof item); 
          // Tworzymy obiekt typu GameCard
          const gameCard: GameCard = {
            id: item.id,
            title: item.name || 'Brak tytułu',               // name -> title
            publisher: item.publisher || 'Brak wydawcy',     // publisher
            category: item.type || 'Brak kategorii',         // type -> category
            price: item.price,// || parseFloat(item.price.toString()),    // price
            year: parseInt(item.year.toString(), 10) || 0,    // year
            description: item.description || 'Brak opisu',    // description
            photos: item.image,
            ageFrom: parseInt(item.rating.trim().split('-')[0], 10),  // players_number -> playersFrom
            ageTo: parseInt(item.rating.trim().split('-')[1], 10),     // players_number -> playersTo
            playersFrom: parseInt(item.players_number.trim().split('-')[0], 10) || 1,  // players_number -> playersFrom
            playersTo: parseInt(item.players_number.trim().split('-')[1], 10) || 2,    // players_number -> playersTo
            action: ''                                        // Akcja (możesz dodać logikę, jeśli są dane)
          };
          
  
          console.log(gameCard);
      
          // Dodajemy gameCard do listy
          this.cards.push(gameCard);
        }
      
        console.log(this.cards); // Zobacz całą listę cards
      }, error => {
        console.error('Błąd:', error);
        //this.failToast(error.error?.message);
      });
    }

}
