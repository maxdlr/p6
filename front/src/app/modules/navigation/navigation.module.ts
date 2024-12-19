import { NgModule } from '@angular/core';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { MobileMenuComponent } from './components/mobile-menu/mobile-menu.component';
import { MatIcon } from '@angular/material/icon';
import {
  MatAnchor,
  MatFabAnchor,
  MatIconAnchor,
  MatIconButton,
} from '@angular/material/button';
import { MatToolbar } from '@angular/material/toolbar';
import { RouterLink } from '@angular/router';
import { MatListItem, MatListOption, MatNavList } from '@angular/material/list';
import {
  MatSidenavContainer,
  MatSidenavModule,
} from '@angular/material/sidenav';
import { BaseComponent } from './components/base/base.component';
import { DesktopMenuComponent } from './components/desktop-menu/desktop-menu.component';

const mats = [
  MatIcon,
  MatIconButton,
  MatToolbar,
  MatListItem,
  MatSidenavContainer,
  MatNavList,
  MatSidenavModule,
];

@NgModule({
  declarations: [MobileMenuComponent, BaseComponent, DesktopMenuComponent],
  imports: [
    CommonModule,
    NgOptimizedImage,
    RouterLink,
    ...mats,
    MatAnchor,
    MatIconAnchor,
    MatFabAnchor,
    MatListOption,
  ],
  exports: [MobileMenuComponent, BaseComponent, DesktopMenuComponent],
})
export class NavigationModule {}
