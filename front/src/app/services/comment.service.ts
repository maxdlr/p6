import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommentRequest } from '../interfaces/comment-request';
import { environment } from '../../environments/environment';
import { DialogComponent } from '../components/dialog/dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { SnackService } from './snack.service';

@Injectable({
  providedIn: 'root',
})
export class CommentService {
  readonly dialog = inject(MatDialog);
  private pathService = environment.apiUrl + 'comments';
  private httpClient = inject(HttpClient);
  private snackService = inject(SnackService);

  public add(commentRequest: CommentRequest) {
    return this.httpClient.post<void>(this.pathService, commentRequest);
  }

  public delete(id: number) {
    return this.httpClient.delete<void>(`${this.pathService}/${id}`);
  }

  public confirmDelete(id: number, onDeleted: () => void) {
    const dialogRef = this.dialog.open(DialogComponent, {
      data: {
        title: `Suppression de votre commentaire`,
        content: `Êtes-vous sûr de vouloir supprimer ce commentaire ?`,
        cancel: 'Annuler',
        confirm: 'Supprimer',
      },
    });

    dialogRef.componentInstance.confirmed.subscribe({
      next: () => {
        this.delete(id).subscribe(() => {
          this.snackService.inform('Commentaire supprimé');
          onDeleted();
        });
      },
    });
  }
}
