import { User } from '../model/user';

export interface CommentRequest {
  author: User;
  articleId: number;
  content: string;
  createdAt: Date;
}
