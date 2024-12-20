import { Component, Input } from '@angular/core';
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
import { MatAnchor } from '@angular/material/button';

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
  ],
  templateUrl: './article-card.component.html',
  styleUrl: './article-card.component.scss',
})
export class ArticleCardComponent {
  @Input() article!: Article;
  protected readonly _ = _;
}
