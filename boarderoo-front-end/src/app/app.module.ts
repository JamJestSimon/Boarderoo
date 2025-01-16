import { NgModule } from '@angular/core';
import { ToastrModule } from 'ngx-toastr';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';
import { routes } from './app-routing.module';
import { AppComponent } from './app.component';
import { MainPageComponent } from './main-page/main-page.component';
import { NgxPayPalModule } from 'ngx-paypal';

@NgModule({
  declarations: [],
  imports: [BrowserAnimationsModule, RouterModule.forRoot(routes),NgxPayPalModule],
  providers: [],
  bootstrap: []
})
export class AppModule { }