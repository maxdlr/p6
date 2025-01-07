import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { ArticleService } from '../../../../services/article.service';
import { Article } from '../../../../interfaces/model/article';
import { SessionService } from '../../../../services/session.service';
import { ThemeService } from 'src/app/services/theme.service';
import { Theme } from 'src/app/interfaces/model/theme';
import { takeUntil } from 'rxjs/operators';
import { BreakpointObserver } from '@angular/cdk/layout';
import { Subject } from 'rxjs';
import _ from 'lodash';

@Component({
  selector: 'app-article-list',
  standalone: false,
  templateUrl: './article-list.component.html',
  styleUrl: './article-list.component.scss',
})
export class ArticleListComponent implements OnInit {
  userArticles!: Article[];
  shownArticles!: Article[];
  isMobile = false;
  isLoading = false;
  public userThemes!: Theme[];
  public shownThemes!: Theme[];
  public themes!: Theme[];
  protected readonly _ = _;
  private breakPoints: BreakpointObserver = inject(BreakpointObserver);
  private cdr = inject(ChangeDetectorRef);
  private articleService = inject(ArticleService);
  private sessionService = inject(SessionService);
  private themeService = inject(ThemeService);
  private destroy$ = new Subject<void>();

  ngOnInit(): void {
    this.breakPoints
      .observe('(max-width: 599px)')
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (result) => {
          this.isMobile = result.matches;
          this.cdr.markForCheck();
        },
      });

    this.isMobile = this.breakPoints.isMatched('(max-width: 768px)');
    this.getThemes();
    this.refresh();
  }

  refresh(): void {
    this.isLoading = true;
    this.articleService
      .getAllOfUser(this.sessionService.sessionInformation!.id)
      .subscribe({
        next: (articles: Article[]) => {
          this.shownArticles = articles;
          this.userArticles = articles;
          this.isLoading = false;
        },
      });
  }

  getThemes(): void {
    this.isLoading = true;
    this.themeService.getAll().subscribe((themes: Theme[]) => {
      this.themes = themes;
      this.shownThemes = themes.filter((theme) =>
        this.sessionService?.sessionInformation?.subscriptionThemes.includes(
          theme.id,
        ),
      );
      this.userThemes = themes.filter((theme) =>
        this.sessionService?.sessionInformation?.subscriptionThemes.includes(
          theme.id,
        ),
      );
      this.isLoading = false;
    });
  }

  selectTheme(id: number): void {
    if (this.shownThemes.find((theme) => theme.id === id)) {
      console.log('theme already selected');
      return;
    }

    const themeToAdd: Theme | undefined = this.themes.find(
      (theme) => theme.id === id,
    );
    const articlesToAdd: Article[] = this.userArticles.filter(
      (article: Article) => article.theme.id === id,
    );

    if (themeToAdd) {
      this.shownThemes.push(themeToAdd);
      this.shownArticles.push(...articlesToAdd);
    }
  }

  deselectTheme(id: number): void {
    if (!this.shownThemes.find((theme) => theme.id === id)) {
      console.log('theme not selected');
      return;
    }

    const themeToRemove: Theme | undefined = this.shownThemes.find(
      (theme) => theme.id === id,
    );

    if (themeToRemove) {
      this.shownThemes.splice(this.shownThemes.indexOf(themeToRemove), 1);
      this.shownArticles = this.shownArticles.filter(
        (article: Article) => article.theme.id !== id,
      );
    }
  }

  selectAllThemes(): void {
    this.shownThemes = this.userThemes;
    this.shownArticles = this.userArticles;
  }

  deselectAllThemes(): void {
    this.shownThemes = [];
    this.shownArticles = [];
  }
}
