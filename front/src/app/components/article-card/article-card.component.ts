import { Component, inject, Input } from '@angular/core';
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
import { MatButton } from '@angular/material/button';
import { Router } from '@angular/router';
import { MatChip } from '@angular/material/chips';

@Component({
  selector: 'app-article-card',
  imports: [
    MatCard,
    MatCardContent,
    MatCardFooter,
    MatCardHeader,
    MatCardTitle,
    MatCardActions,
    MatButton,
    MatCardSubtitle,
    MatChip,
  ],
  templateUrl: './article-card.component.html',
  styleUrl: './article-card.component.scss',
})
export class ArticleCardComponent {
  @Input() article!: Article;
  protected readonly _ = _;
  private router = inject(Router);

  public read(articleId: number) {
    // this.router.navigate(['/articles/:id', articleId]);
    console.log('go see article:', articleId);
  }
}
