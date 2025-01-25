import { Component, CUSTOM_ELEMENTS_SCHEMA, OnInit } from '@angular/core';
import { JoinUsComponent } from '../join-us/join-us.component';
import { OurLocationComponent } from '../our-location/our-location.component';
import { CommonModule } from '@angular/common';
import { ToastrService } from 'ngx-toastr';
import { RouterOutlet, Router, ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CustomResponse } from '../CustomResponse';

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
  basicRegistration: boolean = true;
  oauthmail = ''
  constructor(private router: Router, private route: ActivatedRoute,private http: HttpClient) { }

  ngOnInit(): void {
    const sessionToken = localStorage.getItem('session_token');
    this.route.queryParams.subscribe(params => {
      const code = params['code'];
      if (code) {
        //if (code.includes('google')) {
          console.log('Kod autoryzacyjny zawiera "google":', code);
          this.getGoogleUser(code);
      //  } else {
       //   console.log('Kod autoryzacyjny:', code);
          //this.getDiscordUser(code);
       // }
      } else {
        console.log('Brak kodu autoryzacyjnego w URL');
      }


    });

    if (sessionToken) {
      this.router.navigate(['/gry']);
    }
  }

  getDiscordUser(token: string): void {
    this.basicRegistration = false;
      console.log("OTWARTO")
      const targetUrl = `https://boarderoo-928336702407.europe-central2.run.app/discorduser`;

      const body = { token: token };

      // Wywołanie HTTP POST z tokenem w ciele zapytania
        this.oauthmail = "testmail@mail.com";  // Dla testów, jeśli to ma być część procesu
      
        this.http.post(targetUrl, body).subscribe(
          (response) => {
            console.log('Response:', response);  // Tutaj odbieramy odpowiedź z serwera
          },
          (error) => {
            console.error('Error:', error);  // Obsługuje błąd, jeśli zapytanie się nie powiedzie
          }
          );
    
  

    console.log("AHA", this.basicRegistration);
    this.toggleJoinUs();
  }

  getGoogleUser(token: string): void {
    this.basicRegistration = false;
    console.log("OTWARTO")
    const url = 'https://boarderoo-928336702407.europe-central2.run.app/googleuser';  // URL endpointu

// Tworzymy obiekt zawierający token, który będziemy wysyłać w body
  const body = { token: token };

// Wywołanie HTTP POST z tokenem w ciele zapytania
  this.oauthmail = "testmail@mail.com";  // Dla testów, jeśli to ma być część procesu

  this.http.post(url, body).subscribe(
    (response) => {
      console.log('Response:', response);  // Tutaj odbieramy odpowiedź z serwera
    },
    (error) => {
      console.error('Error:', error);  // Obsługuje błąd, jeśli zapytanie się nie powiedzie
    }
    );
    
    console.log("AHA",this.basicRegistration)
    this.toggleJoinUs()
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
