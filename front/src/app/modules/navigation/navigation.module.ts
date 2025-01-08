import { NgModule } from '@angular/core';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { MobileMenuComponent } from './components/mobile-menu/mobile-menu.component';
import { MatIcon } from '@angular/material/icon';
import {
  MatAnchor,
  MatButton,
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
  MatAnchor,
  MatIconAnchor,
  MatFabAnchor,
  MatListOption,
  MatButton,
];

@NgModule({
  declarations: [MobileMenuComponent, BaseComponent, DesktopMenuComponent],
  imports: [CommonModule, NgOptimizedImage, RouterLink, ...mats],
  exports: [BaseComponent],
})
export class NavigationModule {}
