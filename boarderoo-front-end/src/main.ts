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
import { initializeApp, provideFirebaseApp } from '@angular/fire/app';
import { getStorage, provideStorage } from '@angular/fire/storage';

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
    ]), NgxPayPalModule, provideFirebaseApp(() => initializeApp({"projectId":"boarderoo-71469","appId":"1:928336702407:web:5afa9c70251d94fece07e2","storageBucket":"boarderoo-71469.firebasestorage.app","apiKey":"AIzaSyC_G5J-I5R0h_dcAkq8SG93GJjzwHQgLSs","authDomain":"boarderoo-71469.firebaseapp.com","messagingSenderId":"928336702407"})), provideStorage(() => getStorage())
  ]
}).catch((err) => console.error(err));
