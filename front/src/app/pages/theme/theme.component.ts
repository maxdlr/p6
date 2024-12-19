import { Component, inject, OnInit } from '@angular/core';
import { NavigationModule } from '../../modules/navigation/navigation.module';
import { ThemeService } from '../../services/theme.service';
import { Theme } from '../../interfaces/theme';

@Component({
  selector: 'app-theme',
  standalone: true,
  imports: [NavigationModule],
  templateUrl: './theme.component.html',
  styleUrl: './theme.component.scss',
})
export class ThemeComponent implements OnInit {
  themes!: Theme[];
  private themeService = inject(ThemeService);

  ngOnInit(): void {
    this.themeService.getAll().subscribe({
      next: (themes: Theme[]) => {
        console.log(themes);
        this.themes = themes;
      },
    });
  }
}
