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
import { AuthModule } from './modules/auth/auth.module';
import { MAT_FORM_FIELD_DEFAULT_OPTIONS } from '@angular/material/form-field';
import { jwtInterceptor } from './interceptors/jwt.interceptor';
import { authInterceptor } from './interceptors/auth.interceptor';

const mats = [MatButtonModule, MatButtonToggle];

@NgModule({
  declarations: [AppComponent, HomeComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    AuthModule,
    BrowserAnimationsModule,
    NgOptimizedImage,
    ...mats,
  ],
  providers: [
    provideHttpClient(withInterceptors([jwtInterceptor, authInterceptor])),
    {
      provide: MAT_FORM_FIELD_DEFAULT_OPTIONS,
      useValue: { floatLabel: 'auto', appearance: 'outline' },
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
