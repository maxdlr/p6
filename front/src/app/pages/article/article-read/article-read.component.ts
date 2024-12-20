import { Component, inject, OnInit } from '@angular/core';
import { ArticleService } from '../../../services/article.service';
import { Article } from '../../../interfaces/article';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { NavigationModule } from '../../../modules/navigation/navigation.module';
import { DatePipe } from '@angular/common';
import { MatChip } from '@angular/material/chips';
import { MatDivider } from '@angular/material/divider';
import _ from 'lodash';
import { MatAnchor, MatButton } from '@angular/material/button';
import { SessionService } from '../../../services/session.service';
import {
  MatCard,
  MatCardContent,
  MatCardHeader,
  MatCardSubtitle,
  MatCardTitle,
  MatCardTitleGroup,
} from '@angular/material/card';
import { SnackService } from '../../../services/snack.service';

@Component({
  selector: 'app-article-read',
  imports: [
    MatProgressSpinner,
    NavigationModule,
    DatePipe,
    MatChip,
    MatDivider,
    MatAnchor,
    RouterLink,
    MatCard,
    MatCardHeader,
    MatCardTitleGroup,
    MatCardTitle,
    MatCardSubtitle,
    MatCardContent,
    MatButton,
  ],
  templateUrl: './article-read.component.html',
  styleUrl: './article-read.component.scss',
})
export class ArticleReadComponent implements OnInit {
  public article!: Article;
  public isAuthor = false;
  protected readonly _ = _;
  private articleId!: string;
  private articleService = inject(ArticleService);
  private route = inject(ActivatedRoute);
  private sessionService = inject(SessionService);
  private snackService = inject(SnackService);
  private router = inject(Router);

  ngOnInit(): void {
    this.articleId = this.route.snapshot.paramMap.get('id') as string;
    this.getArticle();
  }

  getArticle() {
    this.articleService.getById(Number(this.articleId)).subscribe((article) => {
      this.article = article;
      this.isAuthor =
        this.sessionService.sessionInformation?.id === article.author.id;
    });
  }

  delete() {
    this.articleService.delete(Number(this.articleId)).subscribe(() => {
      this.snackService.inform('Article supprimé');
      this.router.navigate(['/articles']);
    });
  }
}
