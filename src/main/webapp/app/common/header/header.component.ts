import { Component, inject } from '@angular/core';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AuthenticationService } from 'app/security/authentication.service';


@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, NgOptimizedImage, RouterLink],
  templateUrl: './header.component.html'
})
export class HeaderComponent {

  authenticationService = inject(AuthenticationService);

}
