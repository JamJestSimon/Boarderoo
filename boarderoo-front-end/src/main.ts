import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { MainPageComponent } from './app/main-page/main-page.component';
import { provideAnimations } from '@angular/platform-browser/animations'; // Animacje
import { provideToastr } from 'ngx-toastr';
import { provideRouter } from '@angular/router';
import { StartPageComponent } from './app/start-page/start-page.component';
import { CallbackComponent } from './app/callback/callback.component';
import { NgxPayPalModule } from 'ngx-paypal';
import { AdminPanelComponent } from './app/admin-panel/admin-panel.component';
import { PasswordResetComponent } from './app/password-reset/password-reset.component';
import { AccountVerificationComponent } from './app/account-verification/account-verification.component';
import { AdminLoginComponent } from './app/admin-login/admin-login.component';
import { OrdersListComponent } from './app/orders-list/orders-list.component';
import { GamesListComponent } from './app/games-list/games-list.component';

bootstrapApplication(AppComponent, {
  providers: [
    provideAnimations(), // wymagane animacje
    provideToastr({
      timeOut: 3000,
      positionClass: 'toast-bottom-right',
    }),
    provideRouter([
      { path: "", component: StartPageComponent },
      { path: "gry", component: MainPageComponent },
      { path: "callback", component: CallbackComponent},
      { path: "adminpanel", component: AdminPanelComponent},
      { path: "resethasla", component: PasswordResetComponent},
      { path: "weryfikacja", component: AccountVerificationComponent},
      { path: "admin", component: AdminLoginComponent},
      { path: "listagier", component: GamesListComponent},
      { path: "zamowienia", component: OrdersListComponent},
    ]), NgxPayPalModule
  ]
}).catch((err) => console.error(err));
