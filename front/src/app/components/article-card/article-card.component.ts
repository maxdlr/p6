import {
  Component,
  EventEmitter,
  inject,
  Input,
  OnInit,
  Output,
} from '@angular/core';
import {
  MatCard,
  MatCardActions,
  MatCardContent,
  MatCardFooter,
  MatCardHeader,
  MatCardSubtitle,
  MatCardTitle,
} from '@angular/material/card';
import { Article } from '../../interfaces/article';
import _ from 'lodash';
import { RouterLink } from '@angular/router';
import { MatChip } from '@angular/material/chips';
import { DatePipe } from '@angular/common';
import { MatAnchor, MatButton } from '@angular/material/button';
import { SessionService } from '../../services/session.service';
import { SnackService } from 'src/app/services/snack.service';
import { ArticleService } from '../../services/article.service';

@Component({
  selector: 'app-article-list-card',
  imports: [
    MatCard,
    MatCardContent,
    MatCardFooter,
    MatCardHeader,
    MatCardTitle,
    MatCardActions,
    MatCardSubtitle,
    MatChip,
    DatePipe,
    RouterLink,
    MatAnchor,
    MatButton,
  ],
  templateUrl: './article-card.component.html',
  styleUrl: './article-card.component.scss',
})
export class ArticleCardComponent implements OnInit {
  @Input() article!: Article;
  public isAuthor!: boolean;
  @Output() deleted = new EventEmitter<number>();
  protected readonly _ = _;
  private sessionService = inject(SessionService);
  private snackService = inject(SnackService);
  private articleService = inject(ArticleService);

  ngOnInit(): void {
    this.isAuthor =
      this.sessionService.sessionInformation?.id === this.article.author.id;
  }

  delete(id: number) {
    this.articleService.delete(id).subscribe(() => {
      this.snackService.inform('Article supprimé');
      this.deleted.emit(id);
    });
  }
}
