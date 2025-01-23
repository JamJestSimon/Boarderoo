import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-callback',
  templateUrl: './callback.component.html',
  styleUrls: ['./callback.component.css']
})
export class CallbackComponent implements OnInit {
  queryParams: any;

  constructor(private route: ActivatedRoute) { }

  ngOnInit(): void {
    // Nasłuchiwanie na parametry w URL
    this.route.queryParams.subscribe((params) => {
      this.queryParams = params;
      console.log('Received Query Params:', this.queryParams);
      // Logika przekierowania bez klikania przycisku
      if (this.queryParams) {

        this.redirectToCustomUrl();
      }
    });
  }

  // Funkcja do przekierowania na niestandardowy URL
  private redirectToCustomUrl(): void {
    let redirectUrl = 'boarderoo://callback';
    const queryParams = this.queryParams;

    if (queryParams) {
      redirectUrl += '?' + new URLSearchParams(queryParams).toString();
    }

    console.log('Redirecting to', redirectUrl);
    // Przekierowanie na niestandardowy URL
    window.location.href = redirectUrl;
  }
}
