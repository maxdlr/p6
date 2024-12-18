import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { MatToolbar } from '@angular/material/toolbar';
import { NgOptimizedImage } from '@angular/common';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatButton, MatIconButton } from '@angular/material/button';
import { MatListItem, MatNavList } from '@angular/material/list';
import { RouterLink } from '@angular/router';
import { NavigationService } from '../../services/navigation.service';
import { NavigationItem } from '../../interfaces/navigation-item';
import { MatIcon } from '@angular/material/icon';
import {
  BreakpointObserver,
  BreakpointState,
  MediaMatcher,
} from '@angular/cdk/layout';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-base',
  standalone: true,
  imports: [
    MatToolbar,
    NgOptimizedImage,
    MatSidenavModule,
    MatButton,
    MatNavList,
    MatListItem,
    RouterLink,
    MatIconButton,
    MatIcon,
  ],
  templateUrl: './base.component.html',
  styleUrl: './base.component.scss',
})
export class BaseComponent {
  navigationService = inject(NavigationService);
  navigation: NavigationItem[] = [];
  breakPoints: BreakpointObserver = inject(BreakpointObserver);
  isMobile!: boolean;

  constructor() {
    this.navigation = this.navigationService.navigationItems;
    const $deviceWidthState = this.breakPoints.observe('(max-width: 599px)');
    $deviceWidthState.subscribe({
      next: (result) => {
        console.log(result.matches);
        this.isMobile = result.matches;
      },
    });
  }
}
