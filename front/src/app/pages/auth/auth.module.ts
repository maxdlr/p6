import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginComponent } from './templates/login/login.component';
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
import { RegisterComponent } from './templates/register/register.component';
import { AuthBaseComponent } from './components/auth-base/auth-base.component';
import { MeComponent } from './templates/me/me.component';
import { RouterLink } from '@angular/router';
import { NavigationModule } from '../../modules/navigation/navigation.module';
import {
  MatCard,
  MatCardContent,
  MatCardHeader,
  MatCardSubtitle,
  MatCardTitle,
  MatCardTitleGroup,
} from '@angular/material/card';

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
