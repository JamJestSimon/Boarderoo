import { Component, CUSTOM_ELEMENTS_SCHEMA, OnInit } from '@angular/core';
import { JoinUsComponent } from '../join-us/join-us.component';
import { OurLocationComponent } from '../our-location/our-location.component';
import { CommonModule } from '@angular/common';
import { ToastrService } from 'ngx-toastr';
import { RouterOutlet, Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-start-page',
  standalone: true,
  imports: [JoinUsComponent, OurLocationComponent, CommonModule, RouterOutlet],
  templateUrl: './start-page.component.html',
  styleUrls: ['./start-page.component.css'],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class StartPageComponent implements OnInit {
  title = 'boarderoo-front-end';
  isJoinUsVisible = false;
  ourLocationVisible = false;

  constructor(private router: Router, private route: ActivatedRoute) { }

  ngOnInit(): void {
    const sessionToken = localStorage.getItem('session_token');
    this.route.queryParams.subscribe(params => {
      const code = params['code'];

      if (code) {
        if (code.includes('google')) {
          console.log('Kod autoryzacyjny zawiera "google":', code);

        } else {
          console.log('Kod autoryzacyjny:', code);
        }
      } else {
        console.log('Brak kodu autoryzacyjnego w URL');
      }


    });

    if (sessionToken) {
      this.router.navigate(['/gry']);
    }
  }

  toggleJoinUs() {
    this.isJoinUsVisible = !this.isJoinUsVisible;
    console.log(this.isJoinUsVisible);
  }

  toggleOurLocation() {
    this.ourLocationVisible = !this.ourLocationVisible;
    console.log(this.ourLocationVisible);
  }
}
