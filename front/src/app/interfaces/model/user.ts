export interface User {
  id: number;
  email: string;
  username: string;
  subscriptionThemes?: number[];
  createdAt?: Date | string;
  updatedAt?: Date | string;
}
