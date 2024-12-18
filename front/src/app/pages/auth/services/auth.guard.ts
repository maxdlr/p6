import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { SessionService } from '../../../services/session.service';

export const isLogged: CanActivateFn = (route, state) => {
  const sessionService = inject(SessionService);
  const router = inject(Router);

  return sessionService.isLogged ? true : router.navigate(['/login']);
};
