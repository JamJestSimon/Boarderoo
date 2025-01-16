import { Component, EventEmitter, Output, OnInit, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ToastContainerDirective, ToastrService } from 'ngx-toastr';
import { ICreateOrderRequest, IPayPalConfig, NgxPayPalModule } from 'ngx-paypal';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.css',
  schemas: [CUSTOM_ELEMENTS_SCHEMA], // Dodajemy schemat
})
export class CartComponent {
@Output() close = new EventEmitter<void>(); // Definiujemy zdarzenie
  
    public payPalConfig ? : IPayPalConfig;

    toastContainer: ToastContainerDirective | undefined;
    email: string = '';  // Dodajemy zmienną na e-mail
    sumPrice: number = 7.12;
    constructor(private toastr: ToastrService) {}
    
    ngOnInit(): void {
      this.initConfig();
    }

    private initConfig(): void {
      this.payPalConfig = {
          currency: 'EUR',
          clientId: 'sb',
          createOrderOnClient: (data) => < ICreateOrderRequest > {
              intent: 'CAPTURE',
              purchase_units: [{
                  amount: {
                      currency_code: 'EUR',
                      value: '9.99',
                      breakdown: {
                          item_total: {
                              currency_code: 'EUR',
                              value: '9.99'
                          }
                      }
                  },
                  items: [{
                      name: 'Enterprise Subscription',
                      quantity: '1',
                      category: 'DIGITAL_GOODS',
                      unit_amount: {
                          currency_code: 'EUR',
                          value: '9.99',
                      },
                  }]
              }]
          },
          advanced: {
              commit: 'true'
          },
          style: {
              label: 'paypal',
              layout: 'vertical'
          },
          onApprove: (data, actions) => {
              console.log('onApprove - transaction was approved, but not authorized', data, actions);
              actions.order.get().then((details: any) => {
                  console.log('onApprove - you can get full order details inside onApprove: ', details);
              });

          },
          onClientAuthorization: (data) => {
              console.log('onClientAuthorization - you should probably inform your server about completed transaction at this point', data);
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
}
