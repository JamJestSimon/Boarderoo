import { Component, EventEmitter, Output, OnInit, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ToastContainerDirective, ToastrService } from 'ngx-toastr';
import { ICreateOrderRequest, IPayPalConfig, NgxPayPalModule } from 'ngx-paypal';
import { GameCard } from '../GameCard';
import { CustomResponse } from '../CustomResponse';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, FormsModule, NgxPayPalModule],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.css',
  schemas: [CUSTOM_ELEMENTS_SCHEMA], // Dodajemy schemat
})
export class CartComponent {
@Output() close = new EventEmitter<void>(); // Definiujemy zdarzenie
  
    public payPalConfig ? : IPayPalConfig;

    toastContainer: ToastContainerDirective | undefined;
    email: string = '';  // Dodajemy zmienną na e-mail
    sumPrice: string = '';
    dateStart: string = ""
    dateStop: string = ""
    days: number = 1;
    formattedDateRange: string = '';
    isDatePickerOpen: boolean = false;
    items: GameCard[] = [];
    minDate: string | undefined;
    constructor(private toastr: ToastrService, private http: HttpClient) {}

    calculateTotalPrice() {
      this.sumPrice = this.items.reduce((sum, item) => sum + (this.days * item.price), 0).toFixed(2);
      console.log(this.sumPrice);
    }
  
    updateCart(){
      const storedCartItems = sessionStorage.getItem('cartItems');
      if (storedCartItems) {
        console.log()
        this.items = JSON.parse(storedCartItems);  // Przypisanie danych do items\
        this.items.forEach(item => {
          // Zamiana przecinka na kropkę i konwersja na float
          item.price = parseFloat(item.price.toString().replace(',', '.'));
        });
        this.calculateTotalPrice();  // Obliczenie ceny po załadowaniu
        console.log(this.items);
      }
    }

    removeItem(item: any) {
      console.log("Usuwam: ", item);
      const index = this.items.indexOf(item);
      if (index > -1) {
        this.items.splice(index, 1);
      }
      this.calculateTotalPrice();
      this.updateSessionStorage();
      console.log("Nowa lista po usunięciu: ", this.items);
    }

    
    updateSessionStorage() {
      console.log("Aktualizowanie sessionStorage");
      sessionStorage.setItem('cartItems', JSON.stringify(this.items));
    }
    // Otwiera kalendarz
  openDatePicker(): void {
    this.isDatePickerOpen = true;
  }

  // Zamknięcie kalendarza
  closeDatePicker(): void {
    this.isDatePickerOpen = false;
  }

  // Aktualizuje wyświetlanie zakresu dat
  updateDateRange(): void {
    if (this.dateStart && this.dateStop) {
      this.formattedDateRange = `${this.dateStart} - ${this.dateStop}`;
      this.calculateDaysDifference();
      this.calculateTotalPrice();
    }
  }


    ngOnInit(): void {
      this.initConfig();
      const today = new Date();
      const year = today.getFullYear();
      const month = (today.getMonth() + 1).toString().padStart(2, '0'); // Dodaje zero na początku, jeśli miesiąc ma tylko 1 cyfrę
      const day = today.getDate().toString().padStart(2, '0'); // Dodaje zero na początku, jeśli dzień ma tylko 1 cyfrę
      this.formattedDateRange = `${year}-${month}-${day}` + " - " + `${year}-${month}-${day}`;
      this.calculateTotalPrice();
      this.dateStart = `${year}-${month}-${day}`;
      this.dateStop = `${year}-${month}-${day}`;

      this.minDate = `${year}-${month}-${day}`
      console.log("MIN: " + this.minDate);

      this.updateCart();

    }

    NewOrder(){
        const proxyUrl = 'http://localhost:8080/'; // Lokalny serwer proxy
        const sessionToken = localStorage.getItem('session_token');
        const targetUrl = 'https://boarderoo-928336702407.europe-central2.run.app/order';
        const fullUrl = proxyUrl + targetUrl;
        console.log(fullUrl);
        const newOrder = {
          "id": "string",
          "start": new Date(this.dateStart).toISOString(),
          "end": new Date(this.dateStop).toISOString(),
          "status": "Zapłacone",
          "user": sessionToken,
          "items": this.items.map(item => item.title),
          "price": this.sumPrice
        }
      this.http.post<CustomResponse>(fullUrl, newOrder).subscribe(response => {
                  console.log(response);
                  this.successToast(response.message);
                }, error => {
                  console.error('Błąd:', error);
                  this.failToast(error.error?.message);
                });
    }

    private initConfig(): void {
      this.payPalConfig = {
          currency: 'PLN',
          clientId: 'AZ8pZw6s44qSnIantY7aDEjGZ0mG8oMwKWLTtIpzub6boKxbGSk0OFSEfw5usTA2HfHU7me4daTaw23c',
          createOrderOnClient: (data) => < ICreateOrderRequest > {
              intent: 'CAPTURE',
              purchase_units: [{
                  amount: {
                      currency_code: 'PLN',
                      value: this.sumPrice,
                      breakdown: {
                          item_total: {
                              currency_code: 'PLN',
                              value: this.sumPrice
                          }
                      }
                  },
                  items: [{
                      name: 'Zamówienie HKDSBHBS',
                      quantity: '1',
                      category: 'DIGITAL_GOODS',
                      unit_amount: {
                          currency_code: 'PLN',
                          value: this.sumPrice,
                      },
                  }]
              }]
          },
          advanced: {
              commit: 'true'
          },
          style: {
              label: 'paypal',
              layout: 'horizontal'
          },
          onApprove: (data, actions) => {
              console.log('onApprove - transaction was approved, but not authorized', data, actions);
              actions.order.get().then((details: any) => {
                  console.log('onApprove - you can get full order details inside onApprove: ', details);
              });

          },
          onClientAuthorization: (data) => {
              console.log('onClientAuthorization - you should probably inform your server about completed transaction at this point', data);
              console.log((data as any).purchase_units[0].payments.captures[0].id);//
              sessionStorage.removeItem('cartItems');
              this.NewOrder();
              //utwórz zamówienie ze statusem zapłacone
              this.successToast("Zamówienie zostało opłacone!");
              this.onClose();

          },
          onCancel: (data, actions) => {
              console.log('OnCancel', data, actions);

          },
          onError: err => {
              console.log('OnError', err);
          },
          onClick: (data, actions) => {
              console.log('onClick', data, actions);
          }
      };
  }
    
    onClose() {
      this.close.emit(); // Emitowanie zdarzenia
    }
  
    showSuccess() {
      this.toastr.overlayContainer = this.toastContainer;
      
      // Sprawdzamy, czy pole email jest wypełnione
      if (this.email.trim() !== '') {
        // Jeśli e-mail jest wypełniony, zielony toast
        this.toastr.success('Mail do resetu hasła został wysłany!', 'Sukces', {
          positionClass: 'toast-top-right',
          timeOut: 3000,
          progressBar: true,
          progressAnimation: 'increasing',
        });
      } else {
        // Jeśli e-mail nie jest wypełniony, czerwony toast
        this.toastr.error('Proszę podać e-mail!', 'Błąd', {
          positionClass: 'toast-top-right',
          timeOut: 3000,
          progressBar: true,
          progressAnimation: 'increasing',
        });
      }
    }
  
    toggleDetails(order: any) {
      order.showDetails = !order.showDetails;
    }

    calculateDaysDifference() {
      if (this.formattedDateRange) {
        const dateRangeParts = this.formattedDateRange.split(' - ');
  
        if (dateRangeParts.length === 2) {
          const startDateString = dateRangeParts[0];
          const endDateString = dateRangeParts[1];
          // Konwersja z formatu "dd-MM-yyyy" na obiekt Date
          const startDate = this.convertToDate(startDateString);
          const endDate = this.convertToDate(endDateString);
          console.log(startDate);
          console.log(endDate);
          if (startDate && endDate) {
            // Oblicz różnicę w dniach
            this.days = this.calculateDateDifference(startDate, endDate);
          }
        }
      }
    }

    convertToDate(dateString: string): Date | null {
      const dateParts = dateString.split('-');
      if (dateParts.length === 3) {
        const day = parseInt(dateParts[0], 10);
        const month = parseInt(dateParts[1], 10) - 1; // Miesiące w JavaScript zaczynają się od 0
        const year = parseInt(dateParts[2], 10);
        console.log("Dzień: " + day);
        console.log("Miesiąc: " + month);
        console.log("Rok: " + year);
        console.log(new Date(year, month, day));
        return new Date(day, month, year);
      }
      return null;
    }

    calculateDateDifference(startDate: Date, endDate: Date): number {
      const timeDiff = Math.abs(endDate.getTime() - startDate.getTime());
      console.log(timeDiff);
      return Math.ceil(timeDiff / (1000 * 3600 * 24)) + 1; // Dni
    }

    failToast(communicate: string) {
      this.toastr.overlayContainer = this.toastContainer;
      this.toastr.error(communicate, 'Błąd', {
        positionClass: 'toast-top-right',
        timeOut: 3000,
        progressBar: true,
        progressAnimation: 'increasing',
      });
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
