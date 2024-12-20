import { HttpInterceptorFn } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';
import { inject } from '@angular/core';
import { SnackService } from '../services/snack.service';
import { Router } from '@angular/router';
import { SessionService } from '../services/session.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const snack = inject(SnackService);
  const router = inject(Router);
  const sessionService = inject(SessionService);

  return next(req).pipe(
    catchError((error) => {
      if (error.status === 401) {
        sessionService.logOut();
        snack.error('Session Expired');
        router.navigate(['/login']);
      }
      return throwError(() => error);
    }),
  );
};
