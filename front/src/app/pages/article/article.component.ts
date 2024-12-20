import { Component, inject, OnInit } from '@angular/core';
import { ArticleService } from '../../services/article.service';
import { Article } from '../../interfaces/article';
import { NavigationModule } from '../../modules/navigation/navigation.module';
import { ArticleCardComponent } from '../../components/article-card/article-card.component';
import { SessionService } from '../../services/session.service';

@Component({
  selector: 'app-article',
  standalone: true,
  imports: [NavigationModule, ArticleCardComponent],
  templateUrl: './article.component.html',
  styleUrl: './article.component.scss',
})
export class ArticleComponent implements OnInit {
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
