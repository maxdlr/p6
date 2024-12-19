import { inject, Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SubscriptionRequest } from '../interfaces/subscription-request';

@Injectable({
  providedIn: 'root',
})
export class SubscriptionService {
  private pathService = environment.apiUrl + 'subscriptions';
  private httpClient = inject(HttpClient);

  public subscribeToTheme(
    subscriptionRequest: SubscriptionRequest,
  ): Observable<void> {
    return this.httpClient.post<void>(
      `${this.pathService}/subscribe`,
      subscriptionRequest,
    );
  }

  public unsubscribeToTheme(
    subscriptionRequest: SubscriptionRequest,
  ): Observable<void> {
    return this.httpClient.post<void>(
      `${this.pathService}/unsubscribe`,
      subscriptionRequest,
    );
  }
}
