import { Component, inject, OnInit } from '@angular/core';
import { NavigationModule } from '../../modules/navigation/navigation.module';
import { ThemeService } from '../../services/theme.service';
import { Theme } from '../../interfaces/theme';
import { ThemeCardComponent } from '../../components/theme-card/theme-card.component';
import { Router } from '@angular/router';
import { SnackService } from '../../services/snack.service';

@Component({
  selector: 'app-theme',
  standalone: true,
  imports: [NavigationModule, ThemeCardComponent],
  templateUrl: './theme.component.html',
  styleUrl: './theme.component.scss',
})
export class ThemeComponent implements OnInit {
  themes!: Theme[];
  private themeService = inject(ThemeService);
  private router = inject(Router);
  private snack = inject(SnackService);

  ngOnInit(): void {
    this.themeService.getAll().subscribe({
      next: (themes: Theme[]) => {
        if (themes === null) {
          this.snack.error('Session Expired');
          this.router.navigate(['/login']);
        }

        console.log(themes);

        this.themes = themes;
      },
      error: (error) => {
        console.log(error);
        this.snack.error('Session Expired');
        this.router.navigate(['/login']);
      },
    });
  }
}
