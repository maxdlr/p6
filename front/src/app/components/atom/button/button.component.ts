import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MatAnchor, MatButton } from '@angular/material/button';
import _ from 'lodash';

@Component({
  selector: 'app-button',
  standalone: true,
  imports: [MatButton, MatAnchor],
  templateUrl: './button.component.html',
  styleUrl: './button.component.scss',
})
export class ButtonComponent implements OnInit {
  @Input() label!: string;
  @Input() color: 'primary' | 'secondary' | 'tertiary' = 'primary';
  @Input() type!: 'button' | 'reset' | 'submit';
  @Output() clicked: EventEmitter<void> = new EventEmitter<void>();
  @Input() route?: string;
  matType!: string;

  ngOnInit() {
    this.label = _.upperFirst(this.label);
    this.defineButtonMatType();
  }

  defineButtonMatType(): void {
    switch (this.color) {
      case 'primary':
        this.matType = 'mat-flat-button';
        break;
      case 'secondary':
        this.matType = 'mat-stroked-button';
        break;
    }
  }
}
