import { Component, EventEmitter, Output, OnInit, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ToastContainerDirective, ToastrService } from 'ngx-toastr';
import { ICreateOrderRequest, IPayPalConfig, NgxPayPalModule } from 'ngx-paypal';
import { GameCard } from '../GameCard';
import { CustomResponse } from '../CustomResponse';
import { HttpClient } from '@angular/common/http'
import { environment } from '../../environment/environment';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, FormsModule, NgxPayPalModule],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.css',
  schemas: [CUSTOM_ELEMENTS_SCHEMA], 
})
export class CartComponent {
  @Output() close = new EventEmitter<void>(); 

  public payPalConfig?: IPayPalConfig;

  toastContainer: ToastContainerDirective | undefined;
  email: string = ''; 
  sumPrice: string = '';
  dateStart: string = ""
  dateStop: string = ""
  days: number = 1;
  formattedDateRange: string = '';
  isDatePickerOpen: boolean = false;
  items: GameCard[] = [];
  minDate: string | undefined;
  constructor(private toastr: ToastrService, private http: HttpClient) { }

  calculateTotalPrice() {
    this.sumPrice = this.items.reduce((sum, item) => sum + (this.days * item.price), 0).toFixed(2);
  }

  updateCart() {
    this.items = [];
    const storedCartItems = sessionStorage.getItem('cartItems');
    if (storedCartItems) {
      this.items = JSON.parse(storedCartItems); 
      this.items.forEach(item => {
        item.price = parseFloat(item.price.toString().replace(',', '.'));
      });
    }
    this.calculateTotalPrice();  
  }

  removeItem(item: any) {
    const index = this.items.indexOf(item);
    if (index > -1) {
      this.items.splice(index, 1);
    }
    this.calculateTotalPrice();
    this.updateSessionStorage();
    this.successToast("Usunięto grę z koszyka!")
  }


  updateSessionStorage() {
    sessionStorage.setItem('cartItems', JSON.stringify(this.items));
  }
  openDatePicker(): void {
    this.isDatePickerOpen = true;
  }


  closeDatePicker(): void {
    this.isDatePickerOpen = false;
  }

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
    const month = (today.getMonth() + 1).toString().padStart(2, '0'); 
    const day = today.getDate().toString().padStart(2, '0'); 
    this.formattedDateRange = `${year}-${month}-${day}` + " - " + `${year}-${month}-${day}`;
    this.calculateTotalPrice();
    this.dateStart = `${year}-${month}-${day}`;
    this.dateStop = `${year}-${month}-${day}`;

    this.minDate = `${year}-${month}-${day}`

    this.updateCart();

  }

  NewOrder(id: any) {
    const sessionToken = localStorage.getItem('session_token');
    const targetUrl = 'https://boarderoo-928336702407.europe-central2.run.app/order';
    const newOrder = {
      "id": "string",
      "start": new Date(this.dateStart).toISOString(),
      "end": new Date(this.dateStop).toISOString(),
      "status": "Zapłacone",
      "user": sessionToken,
      "paymentNumber": id,
      "items": this.items.map(item => item.title),
      "price": this.sumPrice
    }
    this.http.post<CustomResponse>(targetUrl, newOrder).subscribe(response => {
      this.successToast("Złożono poprawnie zamówienie!");
    }, error => {
      this.failToast("Nie udało się złożyć zamówienia!");
    });
  }

  private initConfig(): void {
    this.payPalConfig = {
      currency: 'PLN',
      clientId: environment.clientId,
      createOrderOnClient: (data) => <ICreateOrderRequest>{
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
            name: 'Wypożyczenie czasowe gier',
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
      onClientAuthorization: (data) => {
        sessionStorage.removeItem('cartItems');
        this.updateCart();
        this.NewOrder((data as any).purchase_units[0].payments.captures[0].id);
        this.onClose();

      }};
  }

  onClose() {
    this.close.emit();
  }

  getPhotoUrl(fileName: string): string {
    const baseUrl = 'https://firebasestorage.googleapis.com/v0/b/boarderoo-71469.firebasestorage.app/o/files%2F';
    return `${baseUrl}${encodeURIComponent(fileName)}?alt=media`;
  }

  showSuccess() {
    this.toastr.overlayContainer = this.toastContainer;
    if (this.email.trim() !== '') {
      this.toastr.success('Mail do resetu hasła został wysłany!', 'Sukces', {
        positionClass: 'toast-top-right',
        timeOut: 3000,
        progressBar: true,
        progressAnimation: 'increasing',
      });
    } else {
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
        const startDate = this.convertToDate(startDateString);
        const endDate = this.convertToDate(endDateString);
        if (startDate && endDate) {
          this.days = this.calculateDateDifference(startDate, endDate);
        }
      }
    }
  }

  convertToDate(dateString: string): Date | null {
    const dateParts = dateString.split('-');
    if (dateParts.length === 3) {
      const day = parseInt(dateParts[0], 10);
      const month = parseInt(dateParts[1], 10) - 1; 
      const year = parseInt(dateParts[2], 10);
      return new Date(day, month, year);
    }
    return null;
  }

  calculateDateDifference(startDate: Date, endDate: Date): number {
    const timeDiff = Math.abs(endDate.getTime() - startDate.getTime());
    return Math.ceil(timeDiff / (1000 * 3600 * 24)) + 1; 
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
