import { ArticleAuthor, ArticleTheme } from './theme';
import { Comment } from './comment';

export interface Article {
  id: number;
  title: string;
  content: string;
  author: ArticleAuthor;
  theme: ArticleTheme;
  createdAt: Date;
  updatedAt: Date;
  comments: Comment[];
}
