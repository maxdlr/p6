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
import { SnackService } from '../../../../services/snack.service';
import _ from 'lodash';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class LoginComponent implements OnInit {
  public onError = false;
  form!: FormGroup;
  private authService = inject(AuthService);
  private router = inject(Router);
  private sessionService = inject(SessionService);
  private snackService = inject(SnackService);

  ngOnInit(): void {
    this.form = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [Validators.required, Validators.min(8)]),
    });
  }

  public submit(): void {
    const loginRequest = this.form.value as LoginRequest;
    this.authService.login(loginRequest).subscribe({
      next: (response: SessionInformation) => {
        this.sessionService.logIn(response);
        this.snackService.inform(
          `Bonjour ${_.upperFirst(response.username)} !`,
          1000,
        );
        return this.router.navigate(['/articles']);
      },
      error: (error) => {
        this.onError = true;
        if (error.status === 404) {
          this.sessionService.logOut();
        }
        this.snackService.error(error.error.message);
      },
    });
  }
}
