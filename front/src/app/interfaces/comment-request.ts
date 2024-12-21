import { User } from './user';

export interface CommentRequest {
  author: User;
  articleId: number;
  content: string;
  createdAt: Date;
}
