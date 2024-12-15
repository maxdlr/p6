import { CanActivateFn, Router, UrlTree } from '@angular/router';
import { inject } from '@angular/core';
import { SessionService } from '../../../services/session.service';

export const canActivateMe: CanActivateFn = (route, state) => {
  console.log('guarding /me');
  const sessionService = inject(SessionService);

  const router = inject(Router);
  if (!sessionService.$validateToken().isLogged) {
    console.log('token expired, back to /login');
    return router.createUrlTree(['/login']);
  }
  console.log('authorized');
  return true;
};
