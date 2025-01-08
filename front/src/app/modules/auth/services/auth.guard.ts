import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { SessionService } from '../../../services/session.service';

export const isLogged: CanActivateFn = (route, state) => {
  const sessionService = inject(SessionService);
  const router = inject(Router);

  const isLogged = sessionService.isLogged;

  switch (isLogged) {
    case true: {
      if (state.url === '/') {
        return router.navigate(['/articles']);
      }
      return true;
    }
    case false:
      return router.navigate(['/login']);
  }
};
