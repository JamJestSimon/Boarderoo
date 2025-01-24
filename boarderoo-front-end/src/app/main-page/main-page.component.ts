import { Component, CUSTOM_ELEMENTS_SCHEMA, HostListener } from '@angular/core';
import { NavBarComponent } from "../nav-bar/nav-bar.component";
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { GameDetailsComponent } from '../game-details/game-details.component';
import { HttpClient, HttpClientModule, HttpParams } from '@angular/common/http';
import { CustomResponse } from '../CustomResponse';
import { GameCard } from '../GameCard';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-main-page',
  standalone: true,
  imports: [NavBarComponent, CommonModule, FormsModule, GameDetailsComponent, HttpClientModule],
  templateUrl: './main-page.component.html',
  styleUrl: './main-page.component.css',
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class MainPageComponent {
  [x: string]: any;
  isGameDetailsVisible = false;
  selectedCard: any = null;
  isAdmin = false;
  pattern = ''
  publisher = ''
  category = ''
  toggleGameDetails(card?: any) {
    if (card) {
      this.selectedCard = card;
    }
    this.isGameDetailsVisible = !this.isGameDetailsVisible;
  }

  minRange = 1;
  maxRange = 8;
  step = 1;
  minValue = 2;
  maxValue = 5;
  updateSlider(): void {
    if (this.minValue > this.maxValue) {
      [this.minValue, this.maxValue] = [this.maxValue, this.minValue];
    }
    this.isCategoryDropdownOpen = false;
    this.isDropdownOpen = false;
  }

  minRangeYear = 2000;
  maxRangeYear = new Date().getFullYear();
  stepYear = 1;
  minValueYear = this.minRangeYear;
  maxValueYear = this.maxRangeYear;
  updateSliderYear(): void {
    if (this.minRangeYear > this.maxRangeYear) {
      [this.minRangeYear, this.maxRangeYear] = [this.maxRangeYear, this.minValueYear];
    }
    this.isCategoryDropdownOpen = false;
    this.isDropdownOpen = false;
  }

  minRangeAge = 0;
  maxRangeAge = 100;
  stepAge = 1;
  minValueAge = this.minRangeAge;
  maxValueAge = this.maxRangeAge;
  updateSliderAge(): void {
    if (this.minValueAge > this.maxValueAge) {
      [this.minValueAge, this.maxValueAge] = [this.maxValueAge, this.minValueAge];
    }
    this.isCategoryDropdownOpen = false;
    this.isDropdownOpen = false;
  }


  cards: GameCard[] = [];
  cardsInput: GameCard[] = [];
  constructor(private http: HttpClient, private router: Router) { }
  GetGames() {
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
      this.cardsInput = this.cards;
      this.updateOrdersInput();
    }, error => {
    });
  }

  getPhotoUrl(fileName: string): string {
    const baseUrl = 'https://firebasestorage.googleapis.com/v0/b/boarderoo-71469.firebasestorage.app/o/files%2F';
    return `${baseUrl}${encodeURIComponent(fileName)}?alt=media`;
  }

  updateOrdersInput(): void {
    this.cardsInput = this.cards.filter(card => card.title.includes(this.pattern.trim()));
    if (this.selectedCategories.length !== 0) {
      this.cardsInput = this.cardsInput.filter(card =>
        this.selectedCategories.some(category => card.category.includes(category)) &&
        card.title.includes(this.pattern.trim())
      );
    }

    if (this.selectedOptions.length !== 0) {
      this.cardsInput = this.cardsInput.filter(card =>
        this.selectedOptions.some(publisher => card.publisher.includes(publisher)) &&
        card.title.includes(this.pattern.trim())
      );
    }
    this.cardsInput = this.cardsInput.filter(card => {
      const isWithinRange = (card.ageFrom >= this.minValueAge && card.ageTo <= this.maxValueAge) && (card.year >= this.minValueYear && card.year <= this.maxValueYear) && (card.playersFrom >= this.minValue && card.playersTo <= this.maxValue)
      return isWithinRange;
    });

    this.isCategoryDropdownOpen = false;
    this.isDropdownOpen = false;

  }


  ngOnInit(): void {
    this.GetGames();
    const sessionToken = localStorage.getItem('session_token');
    if (!sessionToken) {
      this.router.navigate(['/']);


    }

  }


  options = ['Fantasy Flight Games', 'Asmodee', 'Rebel', 'Days of Wonder', 'Ravensburger', 'Plaid Hat Games', 'Stonemaier Games', 'Portal Games,', 'Matagot', 'Lucky Duck Games'];

  selectedOptions: string[] = [];

  isDropdownOpen = false;

  toggleDropdown(event: Event) {
    event.stopPropagation();
    this.isDropdownOpen = !this.isDropdownOpen;
    this.isCategoryDropdownOpen = false;
  }

  onSelectionChange(option: string) {
    if (this.selectedOptions.includes(option)) {
      this.selectedOptions = this.selectedOptions.filter((item) => item !== option);
    } else {
      this.selectedOptions.push(option);
    }
  }

  isCategoryDropdownOpen = false;
  categories = ['Strategiczne', 'Przygodowe', 'Ekonomiczne', 'Logiczne', 'Rodzinne', 'Fantasy', 'Imprezowe', 'Dla dzieci'];
  selectedCategories: string[] = [];

  toggleCategoryDropdown(event: Event) {
    event.stopPropagation();
    this.isCategoryDropdownOpen = !this.isCategoryDropdownOpen;
    this.isDropdownOpen = false;
  }

  onCategorySelectionChange(category: string) {
    if (this.selectedCategories.includes(category)) {
      this.selectedCategories = this.selectedCategories.filter((item) => item !== category);
    } else {
      this.selectedCategories.push(category);
    }
  }


}

