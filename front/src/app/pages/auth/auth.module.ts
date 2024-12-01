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
  declarations: [LoginComponent],
  imports: [CommonModule, ReactiveFormsModule, FormsModule, ...mats],
  exports: [LoginComponent],
  providers: [
    {
      provide: MAT_FORM_FIELD_DEFAULT_OPTIONS,
      useValue: { floatLabel: 'always' },
    },
  ],
})
export class AuthModule {}
