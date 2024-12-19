import { Component, inject } from '@angular/core';
import { NavigationItem } from '../../../../interfaces/navigation-item';
import { NavigationService } from '../../services/navigation.service';

@Component({
  selector: 'app-mobile-menu',
  templateUrl: './mobile-menu.component.html',
  styleUrl: './mobile-menu.component.scss',
  standalone: false,
})
export class MobileMenuComponent {
  navigation: NavigationItem[] = [];
  private navigationService = inject(NavigationService);

  constructor() {
    this.navigation = this.navigationService.navigationItems;
  }
}
