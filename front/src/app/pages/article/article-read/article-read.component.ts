import { Component, inject, OnInit } from '@angular/core';
import { ArticleService } from '../../../services/article.service';
import { Article } from '../../../interfaces/article';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { NavigationModule } from '../../../modules/navigation/navigation.module';
import { DatePipe, NgIf } from '@angular/common';
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
import { CommentFormComponent } from '../../../components/comment-form/comment-form.component';
import { SessionInformation } from '../../../interfaces/session-information';
import { CommentService } from '../../../services/comment.service';

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
    CommentFormComponent,
    NgIf,
  ],
  templateUrl: './article-read.component.html',
  styleUrl: './article-read.component.scss',
})
export class ArticleReadComponent implements OnInit {
  public article!: Article;
  public isAuthor = false;
  public articleId!: string;
  public sessionInformation!: SessionInformation;
  protected readonly _ = _;
  protected readonly Number = Number;
  private articleService = inject(ArticleService);
  private route = inject(ActivatedRoute);
  private sessionService = inject(SessionService);
  private snackService = inject(SnackService);
  private commentService = inject(CommentService);
  private router = inject(Router);

  ngOnInit(): void {
    this.articleId = this.route.snapshot.paramMap.get('id') as string;
    this.refresh();
  }

  refresh() {
    this.articleService.getById(Number(this.articleId)).subscribe((article) => {
      this.article = article;
      this.sessionInformation = this.sessionService
        .sessionInformation as SessionInformation;
      this.isAuthor = this.sessionInformation.id === article.author.id;
    });
  }

  delete() {
    this.articleService.delete(Number(this.articleId)).subscribe(() => {
      this.snackService.inform('Article supprimé');
      this.router.navigate(['/articles']);
    });
  }

  deleteComment(id: number) {
    this.commentService.delete(id).subscribe({
      next: () => {
        this.snackService.inform('Commentaire supprimé !');
        this.refresh();
      },
    });
  }
}
