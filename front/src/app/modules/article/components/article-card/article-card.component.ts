import {
  Component,
  EventEmitter,
  inject,
  Input,
  OnInit,
  Output,
} from '@angular/core';
import { Article } from '../../../../interfaces/model/article';
import _ from 'lodash';
import { SessionService } from '../../../../services/session.service';
import { ArticleService } from '../../../../services/article.service';

@Component({
  selector: 'app-article-card',
  standalone: false,
  templateUrl: './article-card.component.html',
  styleUrl: './article-card.component.scss',
})
export class ArticleCardComponent implements OnInit {
  @Input() article!: Article;

  public isAuthor!: boolean;

  @Output() deleted = new EventEmitter<number>();

  protected readonly _ = _;

  private sessionService = inject(SessionService);

  private articleService = inject(ArticleService);

  ngOnInit(): void {
    this.isAuthor =
      this.sessionService.sessionInformation?.id === this.article.author.id;
  }

  delete(): void {
    this.articleService.confirmDelete(this.article, () =>
      this.deleted.emit(this.article.id),
    );
  }
}
