import { NgModule } from '@angular/core';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ThemeCardComponent } from './components/theme-card/theme-card.component';
import { ThemeListComponent } from './components/theme-list/theme-list.component';
import { NavigationModule } from '../navigation/navigation.module';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { MatCardModule } from '@angular/material/card';
import { MatButton } from '@angular/material/button';

const mats = [MatProgressSpinner, MatCardModule, MatButton];

@NgModule({
  declarations: [ThemeCardComponent, ThemeListComponent],
  imports: [
    CommonModule,
    NavigationModule,
    NgOptimizedImage,
    RouterLink,
    ...mats,
  ],
  exports: [ThemeCardComponent],
})
export class ThemeModule {}
