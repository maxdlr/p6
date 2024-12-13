import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './pages/home/home.component';
import { NgOptimizedImage } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatButtonToggle } from '@angular/material/button-toggle';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { AuthModule } from './pages/auth/auth.module';
import { MAT_FORM_FIELD_DEFAULT_OPTIONS } from '@angular/material/form-field';
import { ButtonComponent } from './components/atom/button/button.component';
import { jwtInterceptor } from './interceptors/jwt.interceptor';

const mats = [MatButtonModule, MatButtonToggle];

@NgModule({
  declarations: [AppComponent, HomeComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    NgOptimizedImage,
    // AuthModule,
    ...mats,
    ButtonComponent,
    AuthModule,
  ],
  providers: [
    provideHttpClient(withInterceptors([jwtInterceptor])),
    {
      provide: MAT_FORM_FIELD_DEFAULT_OPTIONS,
      useValue: { floatLabel: 'auto', appearance: 'outline' },
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
