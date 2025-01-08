import { Component, inject, OnInit } from '@angular/core';
import { ThemeService } from '../../../../services/theme.service';
import { Theme } from '../../../../interfaces/model/theme';

@Component({
  selector: 'app-theme-list',
  standalone: false,
  templateUrl: './theme-list.component.html',
  styleUrl: './theme-list.component.scss',
})
export class ThemeListComponent implements OnInit {
  themes!: Theme[];
  private themeService = inject(ThemeService);

  ngOnInit(): void {
    this.themeService.getAll().subscribe({
      next: (themes: Theme[]) => (this.themes = themes),
    });
  }
}
