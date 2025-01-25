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
    this.route.queryParams.subscribe((params) => {
      this.queryParams = params;
      if (this.queryParams) {

        this.redirectToCustomUrl();
      }
    });
  }

  private redirectToCustomUrl(): void {
    let redirectUrl = 'boarderoo://callback';
    const queryParams = this.queryParams;

    if (queryParams) {
      redirectUrl += '?' + new URLSearchParams(queryParams).toString();
    }

    window.location.href = redirectUrl;
  }
}
