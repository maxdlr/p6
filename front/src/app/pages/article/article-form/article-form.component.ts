import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ArticleService } from '../../../services/article.service';
import { Article } from 'src/app/interfaces/article';
import {
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { NavigationModule } from '../../../modules/navigation/navigation.module';
import { MatFormField, MatLabel } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { MatOption, MatSelect } from '@angular/material/select';
import { ThemeService } from '../../../services/theme.service';
import { Theme } from '../../../interfaces/theme';
import { ArticleRequest } from '../../../interfaces/article-request';
import { MatButton } from '@angular/material/button';
import { SessionService } from '../../../services/session.service';
import { SessionInformation } from '../../../interfaces/session-information';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { SnackService } from '../../../services/snack.service';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-article-form',
  imports: [
    NavigationModule,
    FormsModule,
    ReactiveFormsModule,
    MatFormField,
    MatLabel,
    MatInput,
    MatSelect,
    MatOption,
    MatButton,
    MatProgressSpinner,
    NgIf,
  ],
  templateUrl: './article-form.component.html',
  styleUrl: './article-form.component.scss',
})
export class ArticleFormComponent implements OnInit {
  public mode!: 'edit' | 'create';
  public articleId!: string | null;
  public article!: Article;
  public form!: FormGroup;
  public themes!: Theme[];
  public pageTitle!: string;
  private route = inject(ActivatedRoute);
  private articleService = inject(ArticleService);
  private themeService = inject(ThemeService);
  private sessionService = inject(SessionService);
  private snackService = inject(SnackService);
  private router = inject(Router);
  private sessionInformation!: SessionInformation;

  ngOnInit(): void {
    this.articleId = this.route.snapshot.paramMap.get('id');

    this.mode = this.articleId ? 'edit' : 'create';
    this.sessionInformation = this.sessionService
      .sessionInformation as SessionInformation;

    this.getThemes();
    this.initForm();

    if (this.mode === 'edit') {
      this.getArticle();
    }
  }

  getThemes(): void {
    this.themeService.getAll().subscribe((themes) => {
      this.themes = themes;
    });
  }

  getArticle() {
    this.articleService.getById(Number(this.articleId)).subscribe({
      next: (article) => {
        this.article = article;
        this.fillForm();
        this.pageTitle = article.title ?? 'Modifier Article';
      },
    });
  }

  initForm() {
    this.form = new FormGroup({
      title: new FormControl('', [Validators.required]),
      content: new FormControl('', [Validators.required]),
      theme: new FormControl('', [Validators.required]),
    });
  }

  fillForm() {
    this.form.controls['title'].setValue(this.article.title);
    this.form.controls['content'].setValue(this.article.content);
    this.form.controls['theme'].setValue(this.article.theme);
  }

  submit(): void {
    const articleRequest: ArticleRequest = this.form.value as ArticleRequest;

    articleRequest.author = {
      id: this.sessionInformation.id as number,
      username: this.sessionInformation.username as string,
      email: this.sessionInformation.email as string,
    };

    switch (this.mode) {
      case 'create':
        this.create(articleRequest);
        break;
      case 'edit':
        this.update(articleRequest);
        break;
      default:
        break;
    }
  }

  create(articleRequest: ArticleRequest): void {
    articleRequest.createdAt = new Date();

    this.articleService.create(articleRequest).subscribe({
      next: (article) => {
        this.snackService.inform('Article créé');
        this.router.navigate(['articles/read', article.id]);
      },
    });
  }

  update(articleRequest: ArticleRequest): void {
    articleRequest.updatedAt = new Date();
    this.articleService
      .update(Number(this.articleId), articleRequest)
      .subscribe((article) => {
        this.snackService.inform('Article mis à jour !');
        this.router.navigate(['articles/read', article.id]);
      });
  }

  delete(): void {
    this.articleService.delete(Number(this.articleId)).subscribe(() => {
      this.snackService.inform('Article supprimé !');
      this.router.navigate(['articles']);
    });
  }
}
