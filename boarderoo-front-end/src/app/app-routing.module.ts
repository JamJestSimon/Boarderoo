import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { MainPageComponent } from './main-page/main-page.component';
import { StartPageComponent } from './start-page/start-page.component';
import { CallbackComponent } from './callback/callback.component';
import { AdminPanelComponent } from './admin-panel/admin-panel.component';
import { PasswordResetComponent } from './password-reset/password-reset.component';
import { AccountVerificationComponent } from './account-verification/account-verification.component';
import { AdminLoginComponent } from './admin-login/admin-login.component';
import { OrdersListComponent } from './orders-list/orders-list.component';
import { GamesListComponent } from './games-list/games-list.component';

export const routes: Routes = [
  { path: "", component: StartPageComponent },
  { path: "gry", component: MainPageComponent },
  { path: "callback", component: CallbackComponent},
  { path: "adminpanel", component: AdminPanelComponent},
  { path: "resethasla", component: PasswordResetComponent},
  { path: "weryfikacja", component: AccountVerificationComponent},
  { path: "admin", component: AdminLoginComponent},
  { path: "listagier", component: GamesListComponent},
  { path: "zamowienia", component: OrdersListComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
