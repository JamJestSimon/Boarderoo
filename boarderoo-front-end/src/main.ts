import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { provideAnimations } from '@angular/platform-browser/animations'; // Animacje
import { provideToastr } from 'ngx-toastr';

bootstrapApplication(AppComponent, {
  providers: [
    provideAnimations(), // wymagane animacje
    provideToastr({
      timeOut: 3000,
      positionClass: 'toast-bottom-right',
    }),
  ]
}).catch((err) => console.error(err));
