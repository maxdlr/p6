import { Theme } from './theme';
import { User } from './user';

export interface ArticleRequest {
  title: string;
  content: string;
  theme: Theme;
  author: User;
  createdAt: Date;
}
