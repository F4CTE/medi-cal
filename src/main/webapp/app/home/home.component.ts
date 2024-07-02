import { Component, inject } from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import { environment } from 'environments/environment';
import { RouterLink } from '@angular/router';
import { AuthenticationService } from '../security/authentication.service';
import { ImageSliderComponent } from '../image-slider/image-slider.component';
import { AppointmentListComponent } from '../appointment/appointment-list.component'; // Importez AppointmentListComponent

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterLink, ImageSliderComponent, AppointmentListComponent, NgOptimizedImage],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent {
  environment = environment;
  authenticationService = inject(AuthenticationService);

  images: string[] = [
    'post1.jpg',
    'post1.png',
    'post3.jpg'
  ];
}
