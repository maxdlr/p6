import { Component, inject, OnInit } from '@angular/core';
import { NavigationModule } from '../../modules/navigation/navigation.module';
import { ThemeService } from '../../services/theme.service';
import { Theme } from '../../interfaces/theme';
import { ThemeCardComponent } from '../../components/theme-card/theme-card.component';
import { MatProgressSpinner } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-theme',
  standalone: true,
  imports: [NavigationModule, ThemeCardComponent, MatProgressSpinner],
  templateUrl: './theme.component.html',
  styleUrl: './theme.component.scss',
})
export class ThemeComponent implements OnInit {
  themes!: Theme[];
  private themeService = inject(ThemeService);

  ngOnInit(): void {
    this.themeService.getAll().subscribe({
      next: (themes: Theme[]) => (this.themes = themes),
    });
  }
}
