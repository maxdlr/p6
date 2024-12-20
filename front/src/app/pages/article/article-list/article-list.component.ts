import { Component, inject, OnInit } from '@angular/core';
import { ArticleService } from '../../../services/article.service';
import { Article } from '../../../interfaces/article';
import { NavigationModule } from '../../../modules/navigation/navigation.module';
import { SessionService } from '../../../services/session.service';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { ArticleCardComponent } from '../../../components/article-card/article-card.component';

@Component({
  selector: 'app-article-list',
  standalone: true,
  imports: [NavigationModule, MatProgressSpinner, ArticleCardComponent],
  templateUrl: './article-list.component.html',
  styleUrl: './article-list.component.scss',
})
export class ArticleListComponent implements OnInit {
  articles!: Article[];
  private articleService = inject(ArticleService);
  private sessionService = inject(SessionService);

  ngOnInit(): void {
    this.articleService
      .getAllOfUser(this.sessionService.sessionInformation!.id)
      .subscribe({
        next: (articles: Article[]) => (this.articles = articles),
      });
  }
}
