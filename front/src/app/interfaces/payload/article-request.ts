import { Theme } from '../model/theme';
import { User } from '../model/user';

export interface ArticleRequest {
  title: string;
  content: string;
  theme: Theme;
  author: User;
  createdAt: Date;
  updatedAt: Date;
}
