import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/auth/templates/login/login.component';
import { RegisterComponent } from './pages/auth/templates/register/register.component';
import { MeComponent } from './pages/auth/templates/me/me.component';
import { isLogged } from './pages/auth/services/auth.guard';
import { ThemeComponent } from './pages/theme/theme.component';
import { ArticleComponent } from './pages/article/article.component';

// consider a guard combined with canLoad / canActivate route option
// to manage unauthenticated user to access private routes
const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'me', component: MeComponent, canActivate: [isLogged] },
  { path: 'themes', component: ThemeComponent, canActivate: [isLogged] },
  { path: 'articles', component: ArticleComponent, canActivate: [isLogged] },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
