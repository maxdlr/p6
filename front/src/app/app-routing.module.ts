import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './modules/auth/components/login/login.component';
import { RegisterComponent } from './modules/auth/components/register/register.component';
import { MeComponent } from './modules/auth/components/me/me.component';
import { isLogged } from './modules/auth/services/auth.guard';
import { ThemeListComponent } from './modules/theme/components/theme-list/theme-list.component';
import { ArticleReadComponent } from './modules/article/components/article-read/article-read.component';
import { ArticleListComponent } from './modules/article/components/article-list/article-list.component';
import { ArticleFormComponent } from './modules/article/components/article-form/article-form.component';

const routes: Routes = [
  { path: '', component: HomeComponent, canActivate: [isLogged] },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'me', component: MeComponent, canActivate: [isLogged] },
  { path: 'themes', component: ThemeListComponent, canActivate: [isLogged] },
  {
    path: 'articles',
    component: ArticleListComponent,
    canActivate: [isLogged],
  },
  {
    path: 'articles/read/:id',
    component: ArticleReadComponent,
    canActivate: [isLogged],
  },
  {
    path: 'articles/edit/:id',
    component: ArticleFormComponent,
    canActivate: [isLogged],
  },
  {
    path: 'articles/add',
    component: ArticleFormComponent,
    canActivate: [isLogged],
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
