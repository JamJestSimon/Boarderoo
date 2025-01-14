import { Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { StartPageComponent } from "./start-page/start-page.component";
import { MainPageComponent } from './main-page/main-page.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet],
  styleUrls: ['./app.component.css'],
  template: `<main>
    <router-outlet />
</main>`
})
export class AppComponent {
  title = 'boarderoo-front-end';

}