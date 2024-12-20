import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root',
})
export class SnackService {
  private _snack: MatSnackBar;

  constructor() {
    this._snack = new MatSnackBar();
  }

  public inform(message: string, duration = 3000) {
    this._snack.open(message, undefined, {
      duration: duration,
      verticalPosition: 'top',
    });
  }

  public error(message: string, action = false) {
    this._snack.open(message, action ? 'OK' : '', {
      duration: 10000,
      verticalPosition: 'top',
    });
  }
}
