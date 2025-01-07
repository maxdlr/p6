import {
  Component,
  EventEmitter,
  inject,
  Input,
  OnInit,
  Output,
} from '@angular/core';
import { Theme } from '../../../../interfaces/model/theme';
import _ from 'lodash';
import { SubscriptionService } from '../../../../services/subscription.service';
import { SubscriptionRequest } from '../../../../interfaces/payload/subscription-request';
import { SessionService } from '../../../../services/session.service';
import { SessionInformation } from '../../../../interfaces/session-information';
import { SnackService } from '../../../../services/snack.service';

@Component({
  selector: 'app-theme-card',
  standalone: false,
  templateUrl: './theme-card.component.html',
  styleUrl: './theme-card.component.scss',
})
export class ThemeCardComponent implements OnInit {
  @Input() public theme!: Theme;
  @Output() subscriptionUpdate: EventEmitter<string> =
    new EventEmitter<string>();

  protected readonly _ = _;
  protected sessionService = inject(SessionService);
  protected sessionInformation!: SessionInformation;
  private subscriptionService = inject(SubscriptionService);
  private snackService = inject(SnackService);

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
        this.subscriptionUpdate.emit();
        this.snackService.inform('Abonné à ' + this.theme.name);
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
        this.subscriptionUpdate.emit();
        this.snackService.inform('Désabonné de ' + this.theme.name);
      },
    });
  }
}
