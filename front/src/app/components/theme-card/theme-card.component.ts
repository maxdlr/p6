import { Component, inject, Input, OnInit } from '@angular/core';
import { NavigationModule } from '../../modules/navigation/navigation.module';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatButton } from '@angular/material/button';
import { Theme } from '../../interfaces/theme';
import _ from 'lodash';
import { SubscriptionService } from '../../services/subscription.service';
import { SubscriptionRequest } from '../../interfaces/subscription-request';
import { SessionService } from '../../services/session.service';
import { SessionInformation } from '../../interfaces/session-information';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-theme-card',
  standalone: true,
  imports: [
    NavigationModule,
    MatCardModule,
    MatChipsModule,
    MatButton,
    CommonModule,
  ],
  templateUrl: './theme-card.component.html',
  styleUrl: './theme-card.component.scss',
})
export class ThemeCardComponent implements OnInit {
  @Input() public theme!: Theme;
  protected readonly _ = _;
  protected sessionService = inject(SessionService);
  protected sessionInformation!: SessionInformation;
  private subscriptionService = inject(SubscriptionService);

  ngOnInit(): void {
    this.sessionInformation = this.sessionService
      .sessionInformation as SessionInformation;
  }

  subscribeToTheme() {
    const subscriptionRequest: SubscriptionRequest = {
      userId: this.sessionService.sessionInformation?.id as number,
      themeId: this.theme.id,
    };

    this.subscriptionService.subscribeToTheme(subscriptionRequest).subscribe({
      next: (userThemeIds) => {
        this.sessionInformation.subscriptionThemes = userThemeIds;
        this.sessionService.updateSessionCookie();

        console.log('subscribed to ' + this.theme.id);
      },
    });
  }

  unsubscribeToTheme() {
    const subscriptionRequest: SubscriptionRequest = {
      userId: this.sessionService.sessionInformation?.id as number,
      themeId: this.theme.id,
    };

    this.subscriptionService.unsubscribeToTheme(subscriptionRequest).subscribe({
      next: (userThemeIds) => {
        this.sessionInformation.subscriptionThemes = userThemeIds;
        this.sessionService.updateSessionCookie();
        console.log('unsubscribed from ' + this.theme.id);
      },
    });
  }
}
