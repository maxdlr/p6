import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ArticleCardComponent } from './components/article-card/article-card.component';
import { ArticleReadComponent } from './components/article-read/article-read.component';
import { ArticleFormComponent } from './components/article-form/article-form.component';
import { ArticleListComponent } from './components/article-list/article-list.component';
import { MatCardModule } from '@angular/material/card';
import { MatChip } from '@angular/material/chips';
import { MatAnchor, MatButton } from '@angular/material/button';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { CommentFormComponent } from './components/comment-form/comment-form.component';
import { MatDivider } from '@angular/material/divider';
import { NavigationModule } from '../navigation/navigation.module';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatOption, MatSelect } from '@angular/material/select';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatInput } from '@angular/material/input';
import { MatExpansionModule } from '@angular/material/expansion';

const mats = [
  MatCardModule,
  MatChip,
  MatButton,
  MatAnchor,
  MatProgressSpinner,
  MatDivider,
  MatFormFieldModule,
  MatExpansionModule,
  MatSelect,
  MatOption,
  MatInput,
];

@NgModule({
  declarations: [
    ArticleCardComponent,
    ArticleReadComponent,
    ArticleFormComponent,
    ArticleListComponent,
    CommentFormComponent,
  ],
  imports: [
    CommonModule,
    FormsModule,
    NavigationModule,
    ReactiveFormsModule,
    RouterLink,
    ...mats,
  ],
  exports: [ArticleCardComponent],
})
export class ArticleModule {}
