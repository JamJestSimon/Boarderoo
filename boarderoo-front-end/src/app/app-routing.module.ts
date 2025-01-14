import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { MainPageComponent } from './main-page/main-page.component';
import { StartPageComponent } from './start-page/start-page.component';
import { AccountDetailsComponent } from './account-details/account-details.component';
import { CallbackComponent } from './callback/callback.component';

export const routes: Routes = [
  { path: "", component: StartPageComponent },
  { path: "gry", component: MainPageComponent },
  { path: "konto", component: AccountDetailsComponent},
  { path: "callback", component: CallbackComponent}
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
