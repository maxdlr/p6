import { Component, Input, OnInit } from '@angular/core';
import _ from 'lodash';

@Component({
  selector: 'app-auth-base',
  standalone: false,
  templateUrl: './auth-base.component.html',
  styleUrl: './auth-base.component.scss',
})
export class AuthBaseComponent implements OnInit {
  @Input() title!: string;

  ngOnInit(): void {
    this.title = _.upperFirst(this.title);
  }
}
