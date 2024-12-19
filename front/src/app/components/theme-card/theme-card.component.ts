import { Component, inject, Input } from '@angular/core';
import { NavigationModule } from '../../modules/navigation/navigation.module';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatButton } from '@angular/material/button';
import { Theme } from '../../interfaces/theme';
import _ from 'lodash';
import { SubscriptionService } from '../../services/subscription.service';
import { SubscriptionRequest } from '../../interfaces/subscription-request';
import { SessionService } from '../../services/session.service';

@Component({
  selector: 'app-theme-card',
  standalone: true,
  imports: [NavigationModule, MatCardModule, MatChipsModule, MatButton],
  templateUrl: './theme-card.component.html',
  styleUrl: './theme-card.component.scss',
})
export class ThemeCardComponent {
  @Input() public theme!: Theme;
  // private subscriptionService = inject(SubscriptionService);
  protected readonly _ = _;
  private subscriptionService = inject(SubscriptionService);
  private sessionService = inject(SessionService);

  subscribeToTheme() {
    const subscriptionRequest: SubscriptionRequest = {
      userId: this.sessionService.sessionInformation?.id as number,
      themeId: this.theme.id,
    };

    this.subscriptionService.subscribeToTheme(subscriptionRequest).subscribe({
      next: () => {
        console.log('subscribed');
      },
    });
  }
}
