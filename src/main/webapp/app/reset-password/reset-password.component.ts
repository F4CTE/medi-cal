import { Component } from '@angular/core';
import { UserService } from 'app/user/user.service';
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [
    FormsModule
  ],
  templateUrl: './reset-password.component.html',
  styleUrl: './reset-password.component.scss'
})
export class ResetPasswordComponent {
  userId: number | null = null;
  newPassword: string = '';

  constructor(private userService: UserService) {}

  resetPassword() {
    if (this.userId !== null && this.newPassword !== '') {
      this.userService.resetPassword(this.userId, this.newPassword)
        .subscribe(
          () => {
            alert('Mot de passe réinitialisé avec succès!');
            this.userId = null;
            this.newPassword = '';
          },
          (error: any) => {
            console.error('Erreur lors de la réinitialisation du mot de passe :', error);
            alert('Erreur lors de la réinitialisation du mot de passe.');
          }
        );
    } else {
      alert('Veuillez remplir tous les champs.');
    }
  }

}
