import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatIconAnchor, MatIconButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';

@Component({
  selector: 'app-button-icon',
  imports: [MatIconButton, MatIconAnchor, MatIcon],
  templateUrl: './button-icon.component.html',
  styleUrl: './button-icon.component.scss',
})
export class ButtonIconComponent {
  @Input() icon!: string;
  @Input() color: 'primary' | 'secondary' | 'tertiary' = 'primary';
  @Input() type!: 'button' | 'reset' | 'submit';
  @Output() clicked: EventEmitter<void> = new EventEmitter<void>();
  @Input() route?: string;
}
