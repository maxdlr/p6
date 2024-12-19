import { Component, inject, OnInit } from '@angular/core';
import { ArticleService } from '../../services/article.service';
import { Article } from '../../interfaces/article';
import { NavigationModule } from '../../modules/navigation/navigation.module';
import { ArticleCardComponent } from '../../components/article-card/article-card.component';
import { Router } from '@angular/router';
import { SnackService } from '../../services/snack.service';

@Component({
  selector: 'app-article',
  imports: [NavigationModule, ArticleCardComponent],
  templateUrl: './article.component.html',
  styleUrl: './article.component.scss',
})
export class ArticleComponent implements OnInit {
  articleService = inject(ArticleService);
  articles!: Article[];
  private router = inject(Router);
  private snack = inject(SnackService);

  ngOnInit(): void {
    this.articleService.getAllOfUser().subscribe({
      next: (articles: Article[]) => {
        if (articles === null) {
          this.snack.error('Session Expired');
          this.router.navigate(['/login']);
        }

        this.articles = articles;
        console.log(articles);
      },
      error: (error) => {
        console.log(error);
        this.snack.error('Session Expired');
        this.router.navigate(['/login']);
      },
    });
  }
}
