import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SessionService } from '../../../../services/session.service';
import { UserService } from '../../../../services/user.service';
import { User } from '../../../../interfaces/user';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { LoginRequest } from '../../interfaces/login-request';
import { SessionInformation } from '../../../../interfaces/session-information';
import { UserEditRequest } from '../../interfaces/user-edit-request';
import { MatSnackBar } from '@angular/material/snack-bar';

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
  private snackBar = inject(MatSnackBar);

  public ngOnInit(): void {
    this.userService
      .$getById(this.sessionService.sessionInformation!.id)
      .subscribe({
        next: (user: User) => {
          this.user = user;
        },
      });

    this.form = new FormGroup({
      email: new FormControl('', [Validators.email]),
      username: new FormControl(''),
    });
  }

  public submit(): void {
    const userEditRequest = this.form.value as UserEditRequest;

    this.userService
      .$edit(this.sessionService.sessionInformation!.id, userEditRequest)
      .subscribe({
        next: (response: User) => {
          this.snackBar.open('Account information edited successfully', '', {
            duration: 3000,
          });
          this.user = response;
        },
        error: (error) => {
          console.error(error);
        },
      });
  }

  public logOut(): void {
    this.sessionService.logOut();
    this.router.navigate(['/']);
  }
}
