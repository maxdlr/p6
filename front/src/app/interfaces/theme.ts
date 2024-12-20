export interface Theme {
  id: number;
  name: string;
  description: string;
  articleCount: number;
  createdAt: Date;
  updatedAt: Date;
}

export interface ArticleTheme {
  id: number;
  name: string;
}

export interface ArticleAuthor {
  id: number;
  username: string;
}
