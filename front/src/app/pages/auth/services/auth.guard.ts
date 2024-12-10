import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { SessionService } from '../../../services/session.service';
import { map } from 'rxjs';
import { UserService } from '../../../services/user.service';

export const canActivateMe: CanActivateFn = (route, state) => {
  const sessionService = inject(SessionService);
  const router = inject(Router);

  return sessionService
    .$isLogged()
    .pipe(
      map((isLogged) => (isLogged ? true : router.createUrlTree(['/login']))),
    );
  // return true;
};
