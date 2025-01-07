import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginComponent } from './components/login/login.component';
import {
  MatButton,
  MatButtonModule,
  MatIconButton,
} from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import {
  MAT_FORM_FIELD_DEFAULT_OPTIONS,
  MatError,
  MatFormField,
  MatFormFieldModule,
  MatHint,
  MatLabel,
  MatSuffix,
} from '@angular/material/form-field';
import { MatInput, MatInputModule } from '@angular/material/input';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatChipInput } from '@angular/material/chips';
import { RegisterComponent } from './components/register/register.component';
import { AuthBaseComponent } from './components/auth-base/auth-base.component';
import { MeComponent } from './components/me/me.component';
import { RouterLink } from '@angular/router';
import { NavigationModule } from '../navigation/navigation.module';
import {
  MatCard,
  MatCardContent,
  MatCardHeader,
  MatCardSubtitle,
  MatCardTitle,
  MatCardTitleGroup,
} from '@angular/material/card';
import { MatDivider } from '@angular/material/divider';
import { ThemeCardComponent } from '../theme/components/theme-card/theme-card.component';
import { MatProgressSpinner } from '@angular/material/progress-spinner';

const mats = [
  MatButton,
  MatIcon,
  MatSuffix,
  MatIconButton,
  MatInput,
  MatChipInput,
  MatFormField,
  MatLabel,
  MatHint,
  MatError,
  MatInputModule,
  MatFormFieldModule,
  MatButtonModule,
  MatButton,
];

@NgModule({
  declarations: [
    LoginComponent,
    RegisterComponent,
    AuthBaseComponent,
    MeComponent,
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    ...mats,
    RouterLink,
    NavigationModule,
    MatCard,
    MatCardHeader,
    MatCardTitleGroup,
    MatCardTitle,
    MatCardSubtitle,
    MatCardContent,
    MatDivider,
    ThemeCardComponent,
    MatProgressSpinner,
  ],
  exports: [LoginComponent, RegisterComponent, MeComponent],
  providers: [
    {
      provide: MAT_FORM_FIELD_DEFAULT_OPTIONS,
      useValue: { floatLabel: 'always' },
    },
  ],
})
export class AuthModule {}
