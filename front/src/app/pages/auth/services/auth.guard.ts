import { CanActivateFn, Router, UrlTree } from '@angular/router';
import { inject } from '@angular/core';
import { SessionService } from '../../../services/session.service';
import { SessionInformation } from '../../../interfaces/session-information';

export const canActivateMe: CanActivateFn = (route, state) => {
  const sessionService = inject(SessionService);
  const router = inject(Router);

  sessionService.$isLogged();

  console.log('yo');

  return sessionService.isLogged ? true : router.createUrlTree(['/login']);
};
