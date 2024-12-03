import { Component, Input, OnInit } from '@angular/core';
import { MatButton } from '@angular/material/button';
import _ from 'lodash';

@Component({
  selector: 'app-button',
  imports: [MatButton],
  templateUrl: './button.component.html',
  styleUrl: './button.component.scss',
})
export class ButtonComponent implements OnInit {
  @Input() label!: string;

  ngOnInit() {
    this.label = _.upperFirst(this.label);
  }
}
