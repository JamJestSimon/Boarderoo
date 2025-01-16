import { Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { MainPageComponent } from './main-page/main-page.component';
import { StartPageComponent } from './start-page/start-page.component';
import { CallbackComponent } from './callback/callback.component';

export const routes: Routes = [
  { path: "", component: StartPageComponent },
  { path: "gry", component: MainPageComponent },
  { path: "callback", component: CallbackComponent}
];
