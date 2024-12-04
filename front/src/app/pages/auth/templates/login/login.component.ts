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
import { FloatLabelType } from '@angular/material/form-field';
import { toSignal } from '@angular/core/rxjs-interop';
import { map } from 'rxjs';

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

  ngOnInit(): void {
    this.form = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [Validators.required, Validators.min(8)]),
    });
  }

  public submit(): void {
    const loginRequest = this.form.value as LoginRequest;
    console.log(loginRequest);
    this.authService.login(loginRequest).subscribe({
      next: (response: SessionInformation) => {
        this.sessionService.logIn(response);
        this.router.navigate(['/']);
      },
      error: (error) => {
        this.onError = true;
        console.error(error);
      },
    });
  }
}
