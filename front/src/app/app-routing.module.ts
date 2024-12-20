import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/auth/templates/login/login.component';
import { RegisterComponent } from './pages/auth/templates/register/register.component';
import { MeComponent } from './pages/auth/templates/me/me.component';
import { isLogged } from './pages/auth/services/auth.guard';
import { ThemeComponent } from './pages/theme/theme.component';
import { ArticleReadComponent } from './pages/article/article-read/article-read.component';
import { ArticleListComponent } from './pages/article/article-list/article-list.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'me', component: MeComponent, canActivate: [isLogged] },
  { path: 'themes', component: ThemeComponent, canActivate: [isLogged] },
  {
    path: 'articles',
    component: ArticleListComponent,
    canActivate: [isLogged],
  },
  {
    path: 'articles/:id',
    component: ArticleReadComponent,
    canActivate: [isLogged],
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
