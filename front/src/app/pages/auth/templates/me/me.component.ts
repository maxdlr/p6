import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SessionService } from '../../../../services/session.service';
import { UserService } from '../../../../services/user.service';
import { User } from '../../../../interfaces/user';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UserEditRequest } from '../../interfaces/user-edit-request';
import { SnackService } from '../../../../services/snack.service';
import _ from 'lodash';
import { ThemeService } from '../../../../services/theme.service';
import { Theme } from '../../../../interfaces/theme';
import { DialogComponent } from '../../../../components/dialog/dialog.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-me',
  standalone: false,
  templateUrl: './me.component.html',
  styleUrl: './me.component.scss',
})
export class MeComponent implements OnInit {
  public user!: User;
  public themes!: Theme[];
  form!: FormGroup;
  readonly dialog = inject(MatDialog);
  protected readonly _ = _;
  protected readonly ThemeService = ThemeService;
  private sessionService = inject(SessionService);
  private userService = inject(UserService);
  private themeService = inject(ThemeService);
  private router = inject(Router);
  private snack = inject(SnackService);

  public ngOnInit(): void {
    this.initForm();
    this.refresh();
  }

  public refresh() {
    this.userService
      .$getById(this.sessionService.sessionInformation!.id)
      .subscribe({
        next: (user: User) => {
          this.user = user;
          this.form.controls['email'].setValue(this.user.email);
          this.form.controls['username'].setValue(this.user.username);
          this.getThemes();
        },
        error: (error) => {
          console.error(error);
        },
      });
  }

  public getThemes() {
    this.themeService.getAll().subscribe((themes) => {
      this.themes = themes.filter((theme: Theme) =>
        this.user.subscriptionThemes?.includes(theme.id),
      );
    });
  }

  public initForm(): void {
    this.form = new FormGroup({
      email: new FormControl('', [Validators.email]),
      username: new FormControl(''),
    });
  }

  public submit(): void {
    const userEditRequest = this.form.value as UserEditRequest;

    this.userService
      .$edit(this.sessionService.sessionInformation!.id, userEditRequest)
      .subscribe({
        next: (response: User) => {
          this.snack.inform('Informations modifiée');

          this.user = response;
          this.form.controls['email'].setValue(this.user.email);
          this.form.controls['username'].setValue(this.user.username);
        },
        error: (error) => {
          console.error(error);
        },
      });
  }

  public logOut(): void {
    const dialogRef = this.dialog.open(DialogComponent, {
      data: {
        title: 'Déconnexion',
        content: `Êtes-vous sûr de vouloir vous déconnecter ?`,
        cancel: 'Annuler',
        confirm: 'Me déconnecter',
      },
    });

    dialogRef.componentInstance.confirmed.subscribe({
      next: () => {
        this.sessionService.logOut();
        this.snack.inform('A bientôt!');
        this.router.navigate(['/']);
      },
    });
  }
}
