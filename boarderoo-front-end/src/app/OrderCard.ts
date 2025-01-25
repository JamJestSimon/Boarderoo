export interface OrderCard {
  id: string;
  start: string;          // Data rozpoczęcia (ISO 8601)
  end: string;            // Data zakończenia (ISO 8601)
  status: string;         // Status zamówienia
  user: string;           // ID użytkownika
  items: string[];        // Lista przedmiotów w zamówieniu
  price: number;          // Całkowita cena zamówienia
  paymentNumber: string;
  showDetails: boolean
}