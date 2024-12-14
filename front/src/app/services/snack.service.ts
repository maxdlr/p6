import { Component, Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root',
})
export class SnackService {
  private _snack: MatSnackBar;

  constructor() {
    this._snack = new MatSnackBar();
  }

  public inform(message: string) {
    this._snack.open(message, undefined, {
      duration: 3000,
      verticalPosition: 'top',
    });
  }

  public error(message: string) {
    this._snack.open(message, 'OK', {
      duration: 10000,
      verticalPosition: 'top',
    });
  }
}
