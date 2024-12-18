import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SessionService } from '../../../../services/session.service';
import { UserService } from '../../../../services/user.service';
import { User } from '../../../../interfaces/user';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UserEditRequest } from '../../interfaces/user-edit-request';
import { SnackService } from '../../../../services/snack.service';

@Component({
  selector: 'app-me',
  standalone: false,
  templateUrl: './me.component.html',
  styleUrl: './me.component.scss',
})
export class MeComponent implements OnInit {
  public user!: User;
  form!: FormGroup;
  private router = inject(Router);
  private sessionService = inject(SessionService);
  private userService = inject(UserService);
  private snack = inject(SnackService);

  public ngOnInit(): void {
    this.form = new FormGroup({
      email: new FormControl('', [Validators.email]),
      username: new FormControl(''),
    });

    this.userService
      .$getById(this.sessionService.sessionInformation!.id)
      .subscribe({
        next: (user: User) => {
          this.user = user;
          this.form.controls['email'].setValue(this.user.email);
          this.form.controls['username'].setValue(this.user.username);
        },
        error: (error) => {
          console.log(error);
          this.snack.error('Session Expired');
          this.router.navigate(['/login']);
        },
      });
  }

  public submit(): void {
    const userEditRequest = this.form.value as UserEditRequest;

    //todo: authenticate to authorize email change, so backend can regenerate new token.

    this.userService
      .$edit(this.sessionService.sessionInformation!.id, userEditRequest)
      .subscribe({
        next: (response: User) => {
          this.snack.inform('Informations modifiée');

          this.user = response;
          this.form.controls['email'].setValue(this.user.email);
          this.form.controls['username'].setValue(this.user.username);
        },
        error: (error) => {
          console.error(error);
        },
      });
  }

  public logOut(): void {
    this.sessionService.logOut();
    this.snack.inform('A bientôt!');
    this.router.navigate(['/']);
  }
}
