import { Component } from '@angular/core';
import { MatToolbar } from '@angular/material/toolbar';
import { NgOptimizedImage } from '@angular/common';

@Component({
  selector: 'app-base',
  standalone: true,
  imports: [MatToolbar, NgOptimizedImage],
  templateUrl: './base.component.html',
  styleUrl: './base.component.scss',
})
export class BaseComponent {}
