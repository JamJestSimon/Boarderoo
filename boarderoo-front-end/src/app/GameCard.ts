export interface GameCard {
    title: string;
    publisher: string;
    category: string;
    price: number;
    year: number;
    description: string;
    photos: string[];
    ageFrom: number;
    ageTo: number;
    playersFrom: number;
    playersTo: number;
    action: string;
  }