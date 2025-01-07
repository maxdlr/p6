import { NgModule } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ArticleCardComponent } from './components/article-card/article-card.component';
import { ArticleReadComponent } from './components/article-read/article-read.component';
import { ArticleFormComponent } from './components/article-form/article-form.component';
import { ArticleListComponent } from './components/article-list/article-list.component';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { CommentFormComponent } from './components/comment-form/comment-form.component';
import { MatDividerModule } from '@angular/material/divider';
import { NavigationModule } from '../navigation/navigation.module';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatInput, MatInputModule } from '@angular/material/input';

const mats = [
  MatCardModule,
  MatChipsModule,
  MatButtonModule,
  MatProgressSpinnerModule,
  MatDividerModule,
  MatFormFieldModule,
  MatSelectModule,
  MatExpansionModule,
  MatInputModule,
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
    ...mats,
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterLink,
    DatePipe,
    MatInput,
    NavigationModule,
  ],
  exports: [ArticleCardComponent],
})
export class ArticleModule {}
