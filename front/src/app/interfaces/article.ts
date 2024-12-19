import { ArticleTheme } from './theme';

export interface Article {
  id: number;
  title: string;
  content: string;
  authorId: number;
  theme: ArticleTheme;
}
