import { Injectable } from '@angular/core';
import { NavigationItem } from '../interfaces/navigation-item';

@Injectable({
  providedIn: 'root',
})
export class NavigationService {
  public navigationItems: NavigationItem[] = [
    {
      label: 'Articles',
      url: '/articles',
    },
    {
      label: 'themes',
      url: '/themes',
    },
  ];
}
