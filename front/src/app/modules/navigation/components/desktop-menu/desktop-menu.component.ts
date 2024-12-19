import { Component, inject } from '@angular/core';
import { NavigationItem } from '../../../../interfaces/navigation-item';
import { NavigationService } from '../../services/navigation.service';

@Component({
  selector: 'app-desktop-menu',
  standalone: false,
  templateUrl: './desktop-menu.component.html',
  styleUrl: './desktop-menu.component.scss',
})
export class DesktopMenuComponent {
  navigation: NavigationItem[] = [];
  private navigationService = inject(NavigationService);

  constructor() {
    this.navigation = this.navigationService.navigationItems;
  }
}
