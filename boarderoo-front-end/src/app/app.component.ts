import { Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { JoinUsComponent } from './join-us/join-us.component';
import { OurLocationComponent } from './our-location/our-location.component';
import { CommonModule } from '@angular/common';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [JoinUsComponent, OurLocationComponent, CommonModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  schemas: [CUSTOM_ELEMENTS_SCHEMA], // Dodajemy schemat
})
export class AppComponent {
  title = 'boarderoo-front-end';
  isJoinUsVisible = false;
  ourLocationVisible = false

  toggleJoinUs() {
    this.isJoinUsVisible = !this.isJoinUsVisible;
    console.log(this.isJoinUsVisible);
  }

  toggleOurLocation() {
    this.ourLocationVisible = !this.ourLocationVisible;
    console.log(this.ourLocationVisible);
  }
}