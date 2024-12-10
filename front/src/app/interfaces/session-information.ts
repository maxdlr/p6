import { User } from './user';

export interface SessionInformation {
  id: number;
  token: string;
  type: string;
  email: string;
  username: string;
  user: User;
}
