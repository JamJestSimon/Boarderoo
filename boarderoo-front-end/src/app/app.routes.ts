import { Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { MainPageComponent } from './main-page/main-page.component';
import { StartPageComponent } from './start-page/start-page.component';
import { CallbackComponent } from './callback/callback.component';
import { AdminPanelComponent } from './admin-panel/admin-panel.component';
import { PasswordResetComponent } from './password-reset/password-reset.component';
import { AccountDetailComponent } from './account-details/account-details.component';
import { AccountVerificationComponent } from './account-verification/account-verification.component';
import { AdminLoginComponent } from './admin-login/admin-login.component';
import { OrdersListComponent } from './orders-list/orders-list.component';

export const routes: Routes = [
  { path: "", component: StartPageComponent },
  { path: "gry", component: MainPageComponent },
  { path: "callback", component: CallbackComponent},
  { path: "adminPanel", component: AdminPanelComponent},
  { path: "resetHasla", component: PasswordResetComponent},
  { path: "weryfikacja", component: AccountVerificationComponent},
  { path: "admin", component: AdminLoginComponent},
  { path: "zamowienia", component: OrdersListComponent},
];
