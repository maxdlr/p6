import { Component, EventEmitter, inject, Output } from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogTitle,
} from '@angular/material/dialog';
import { MatButton } from '@angular/material/button';

@Component({
  selector: 'app-dialog',
  imports: [
    MatDialogContent,
    MatDialogTitle,
    MatDialogActions,
    MatButton,
    MatDialogClose,
  ],
  templateUrl: './dialog.component.html',
  styleUrl: './dialog.component.scss',
})
export class DialogComponent {
  // readonly dialogRef = inject(MatDialogRef<DialogComponent>);
  readonly data = inject<{
    title: string;
    content: string;
    cancel: string;
    confirm: string;
  }>(MAT_DIALOG_DATA);
  @Output() confirmed = new EventEmitter<void>();

  public confirm() {
    this.confirmed.emit();
  }
}
