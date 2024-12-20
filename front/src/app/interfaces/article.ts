import { ArticleAuthor, ArticleTheme } from './theme';

export interface Article {
  id: number;
  title: string;
  content: string;
  author: ArticleAuthor;
  theme: ArticleTheme;
  createdAt: Date;
  updatedAt: Date;
}
