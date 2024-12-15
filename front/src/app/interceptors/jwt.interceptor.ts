import { HttpEvent, HttpHandlerFn, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { SessionService } from '../services/session.service';
import { Observable } from 'rxjs';
import { CookieService } from 'ngx-cookie-service';

export function jwtInterceptor(
  request: HttpRequest<unknown>,
  next: HttpHandlerFn,
): Observable<HttpEvent<unknown>> {
  const token = inject(CookieService).get('token');

  console.log('interceptor', token);

  if (inject(SessionService).isLogged) {
    request = request.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`,
      },
    });
  }
  return next(request);
}
