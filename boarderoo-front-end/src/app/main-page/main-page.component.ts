import { Component, CUSTOM_ELEMENTS_SCHEMA, HostListener } from '@angular/core';
import { NavBarComponent } from "../nav-bar/nav-bar.component";
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-main-page',
  standalone: true,
  imports: [NavBarComponent, CommonModule, FormsModule],
  templateUrl: './main-page.component.html',
  styleUrl: './main-page.component.css',
  schemas: [CUSTOM_ELEMENTS_SCHEMA], // Dodajemy schemat
})
export class MainPageComponent {

  cards = [
    {
      name: "Gra 1",
      producer: "Producent 1",
      genre: "Przygodowa",
      players: "2-4",
      age: "12",
      price: "15",
      image: "discord.png"
    },
    {
      name: "Gra 2",
      producer: "Producent 2",
      genre: "Strategiczna",
      players: "3-6",
      age: "10",
      price: "20",
      image: "path/to/image2.jpg"
    }
  ];
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

