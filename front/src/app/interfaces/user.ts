export interface User {
  id: number;
  name: string;
  email: string;
  username: string;
  subscriptionThemes: number[];
  createdAt: Date;
  updatedAt: Date;
}
