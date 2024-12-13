import {
  ChangeDetectionStrategy,
  Component,
  inject,
  OnInit,
} from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { SessionInformation } from '../../../../interfaces/session-information';
import { SessionService } from '../../../../services/session.service';
import { LoginRequest } from '../../interfaces/login-request';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class LoginComponent implements OnInit {
  public hide = true;
  public onError = false;
  form!: FormGroup;
  private authService = inject(AuthService);
  private router = inject(Router);
  private sessionService = inject(SessionService);
  private snackBar = inject(MatSnackBar);

  ngOnInit(): void {
    this.form = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [Validators.required, Validators.min(8)]),
    });
  }

  public submit(): void {
    const loginRequest = this.form.value as LoginRequest;
    console.log('loginRequest:', loginRequest);
    this.authService.login(loginRequest).subscribe({
      next: (response: SessionInformation) => {
        console.log(response);
        this.sessionService.logIn(response);
        this.snackBar.open('Welcome back !', '', { duration: 3000 });
        this.router.navigate(['/me']);
      },
      error: (error) => {
        this.onError = true;
        console.error(error);
      },
    });
  }
}
