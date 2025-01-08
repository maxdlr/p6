import { NgModule } from '@angular/core';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ThemeCardComponent } from './components/theme-card/theme-card.component';
import { ThemeListComponent } from './components/theme-list/theme-list.component';
import { NavigationModule } from '../navigation/navigation.module';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';

const mats = [MatProgressSpinnerModule, MatButtonModule, MatCardModule];

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
