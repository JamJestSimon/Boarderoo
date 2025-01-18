import { Component, CUSTOM_ELEMENTS_SCHEMA, HostListener } from '@angular/core';
import { NavBarComponent } from "../nav-bar/nav-bar.component";
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { GameDetailsComponent } from '../game-details/game-details.component';
import { HttpClient, HttpClientModule, HttpParams } from '@angular/common/http';
import { CustomResponse } from '../CustomResponse';
import { ToastrService } from 'ngx-toastr';
import { GameCard } from '../GameCard';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-main-page',
  standalone: true,
  imports: [NavBarComponent, CommonModule, FormsModule, GameDetailsComponent, HttpClientModule],
  templateUrl: './main-page.component.html',
  styleUrl: './main-page.component.css',
  schemas: [CUSTOM_ELEMENTS_SCHEMA], // Dodajemy schemat
})
export class MainPageComponent {
[x: string]: any;
  isGameDetailsVisible = false;
  selectedCard: any = null;
  isAdmin = false;

  toggleGameDetails(card?: any) {
    if (card) {
      this.selectedCard = card; // Ustawiamy wybraną kartę
    }
    this.isGameDetailsVisible = !this.isGameDetailsVisible; // Przełączamy widoczność
  }
  
  minRange = 1; // Minimalna wartość suwaka
  maxRange = 8; // Maksymalna wartość suwaka
  step = 1; // Krok przesuwania
  minValue = 2; // Początkowa minimalna wartość
  maxValue = 5; // Początkowa maksymalna wartość
  updateSlider(): void {
    // Zabezpieczenie przed krzyżowaniem uchwytów
    if (this.minValue > this.maxValue) {
      [this.minValue, this.maxValue] = [this.maxValue, this.minValue];
    }
  }

  minRangeYear = 2000; // Minimalna wartość suwaka
  maxRangeYear = new Date().getFullYear(); // Maksymalna wartość suwaka
  stepYear = 1; // Krok przesuwania
  minValueYear = this.minRangeYear; // Początkowa minimalna wartość
  maxValueYear = this.maxRangeYear; // Początkowa maksymalna wartość
  updateSliderYear(): void {
    // Zabezpieczenie przed krzyżowaniem uchwytów
    if (this.minRangeYear > this.maxRangeYear) {
      [this.minRangeYear, this.maxRangeYear] = [this.maxRangeYear, this.minValueYear];
    }
  }

  minRangeAge = 0; // Minimalna wartość suwaka
  maxRangeAge = 100; // Maksymalna wartość suwaka
  stepAge = 1; // Krok przesuwania
  minValueAge = this.minRangeAge; // Początkowa minimalna wartość
  maxValueAge = this.maxRangeAge; // Początkowa maksymalna wartość
  updateSliderAge(): void {
    // Zabezpieczenie przed krzyżowaniem uchwytów
    if (this.minValueAge > this.maxValueAge) {
      [this.minValueAge, this.maxValueAge] = [this.maxValueAge, this.minValueAge];
    }
  }


  cards: GameCard[] = []
  constructor(private toastr: ToastrService, private http: HttpClient,private router: Router) {}
  GetGames() {
    const proxyUrl = 'http://localhost:8080/'; // Lokalny serwer proxy
    const targetUrl = 'https://boarderoo-928336702407.europe-central2.run.app/game';
    const fullUrl = proxyUrl + targetUrl;
    console.log(fullUrl);
    this.http.get<CustomResponse>(fullUrl).subscribe(response => {
      for (let i = 0; i < response.data.length; i++) {
        const item: any = response.data[0];
        console.log(item);
        console.log(typeof item); 
        // Tworzymy obiekt typu GameCard
        const gameCard: GameCard = {
          title: item.name || 'Brak tytułu',               // name -> title
          publisher: item.publisher || 'Brak wydawcy',     // publisher
          category: item.type || 'Brak kategorii',         // type -> category
          price: item.price,// || parseFloat(item.price.toString()),    // price
          year: parseInt(item.year.toString(), 10) || 0,    // year
          description: item.description || 'Brak opisu',    // description
          photos: item.image ? [item.image] : ['discord.png'],  // Jeśli jest image, to użyj go, w przeciwnym razie przypisz domyślne zdjęcie
          ageFrom: parseInt(item.rating.split(' - ')[0], 10),  // players_number -> playersFrom
          ageTo: parseInt(item.rating.split(' - ')[1], 10),     // players_number -> playersTo
          playersFrom: parseInt(item.players_number.split(' - ')[0], 10) || 1,  // players_number -> playersFrom
          playersTo: parseInt(item.players_number.split(' - ')[1], 10) || 2,    // players_number -> playersTo
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

  ngOnInit(): void {
    this.GetGames();
    const sessionToken = localStorage.getItem('session_token');
    if (!sessionToken) {
      // Jeśli token jest pusty, przekierowujemy na stronę główną
      this.router.navigate(['/']);

      
  }

}


  options = ['Opcja 1', 'Opcja 2', 'Opcja 3', 'Opcja 4'];
  
  // Zmienna przechowująca wybrane opcje
  selectedOptions: string[] = [];

  // Zmienna określająca, czy dropdown jest otwarty
  isDropdownOpen = false;

  // Metoda do obsługi kliknięcia na dropdown
  toggleDropdown(event: Event) {
    event.stopPropagation();
    this.isDropdownOpen = !this.isDropdownOpen;
  }

  // Metoda do obsługi zmiany wybranej opcji
  onSelectionChange(option: string) {
    if (this.selectedOptions.includes(option)) {
      this.selectedOptions = this.selectedOptions.filter((item) => item !== option);
    } else {
      this.selectedOptions.push(option);
    }
  }

  isCategoryDropdownOpen = false;
  categories = ['Kategoria 1', 'Kategoria 2', 'Kategoria 3'];
  selectedCategories: string[] = [];

  toggleCategoryDropdown(event: Event) {
    event.stopPropagation();
    this.isCategoryDropdownOpen = !this.isCategoryDropdownOpen;
  }

  onCategorySelectionChange(category: string) {
    if (this.selectedCategories.includes(category)) {
      this.selectedCategories = this.selectedCategories.filter((item) => item !== category);
    } else {
      this.selectedCategories.push(category);
    }
  }

  @HostListener('document:click', ['$event'])
onDocumentClick(event: MouseEvent) {
  const targetElement = event.target as HTMLElement;

  // Sprawdzamy, czy kliknięto wewnątrz dropdowna
  const isInsideDropdown = targetElement.closest('.dropdown-container');
  const isInsideCategoryDropdown = targetElement.closest('.category-dropdown-container');

  // Zamykamy dropdowny tylko jeśli kliknięto poza nimi
  this.isDropdownOpen = !!isInsideDropdown;
  this.isCategoryDropdownOpen = !!isInsideCategoryDropdown;
}

}

