import { Theme } from './theme';
import { Comment } from './comment';
import { User } from './user';

export interface Article {
  id: number;
  title: string;
  content: string;
  author: User;
  theme: Theme;
  createdAt: Date;
  updatedAt: Date;
  comments: Comment[];
}
