import {
  ChangeDetectionStrategy,
  Component,
  inject,
  OnInit,
} from '@angular/core';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ValidationErrors,
  Validators,
} from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { SessionService } from '../../../../services/session.service';
import { RegisterRequest } from '../../interfaces/register-request';
import { SessionInformation } from '../../../../interfaces/session-information';
import { SnackService } from '../../../../services/snack.service';

@Component({
  selector: 'app-register',
  standalone: false,
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class RegisterComponent implements OnInit {
  public onError = false;
  form!: FormGroup;
  private authService = inject(AuthService);
  private router = inject(Router);
  private sessionService = inject(SessionService);
  private snack = inject(SnackService);

  ngOnInit(): void {
    this.form = new FormGroup(
      {
        username: new FormControl('', [Validators.required]),
        email: new FormControl('', [Validators.required, Validators.email]),
        password: new FormControl('', [
          Validators.required,
          Validators.minLength(8),
          Validators.pattern(
            new RegExp(
              '^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};\':"\\\\|,.<>/?])(?=.{8,}).*$',
            ),
          ),
        ]),
        verifyPassword: new FormControl('', [Validators.required]),
      },
      { validators: this.passwordMatchValidator },
    );
  }

  passwordMatchValidator(group: AbstractControl): ValidationErrors | null {
    const password = group.get('password')?.value;
    const verifyPassword = group.get('verifyPassword')?.value;
    return password === verifyPassword ? null : { passwordsMismatch: true };
  }

  public submit(): void {
    const registerRequest = this.form.value as RegisterRequest;
    this.authService.register(registerRequest).subscribe({
      next: () => {
        this.authService.login(registerRequest).subscribe({
          next: (response: SessionInformation) => {
            this.sessionService.logIn(response);
            this.snack.inform("Account created! Here's your account.");
            this.router.navigate(['/me']);
          },
        });
      },
      error: (error) => {
        this.onError = true;
        console.error(error);
        this.snack.error(error.error.message);
      },
    });
  }
}
