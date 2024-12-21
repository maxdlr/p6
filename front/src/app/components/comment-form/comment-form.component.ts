import {
  Component,
  EventEmitter,
  inject,
  Input,
  OnInit,
  Output,
} from '@angular/core';
import {
  FormControl,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatFormField, MatLabel } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { SnackService } from '../../services/snack.service';
import { CommentRequest } from '../../interfaces/comment-request';
import { User } from '../../interfaces/user';
import { SessionService } from 'src/app/services/session.service';
import { CommentService } from '../../services/comment.service';
import { MatButton } from '@angular/material/button';

@Component({
  selector: 'app-comment-form',
  imports: [
    FormsModule,
    MatFormField,
    MatInput,
    MatLabel,
    ReactiveFormsModule,
    MatButton,
  ],
  templateUrl: './comment-form.component.html',
  styleUrl: './comment-form.component.scss',
})
export class CommentFormComponent implements OnInit {
  public commentForm!: FormControl;
  @Input({ required: true }) articleId!: number;
  @Output() commentAdded = new EventEmitter();
  private commentService = inject(CommentService);
  private snackService = inject(SnackService);
  private sessionService = inject(SessionService);
  private author!: User;

  ngOnInit(): void {
    this.commentForm = new FormControl('', Validators.required);

    if (this.sessionService.sessionInformation) {
      this.author = {
        email: this.sessionService.sessionInformation.email,
        username: this.sessionService.sessionInformation.username,
        id: this.sessionService.sessionInformation.id,
      };
    }
  }

  submit(): void {
    if (!this.author) {
      this.snackService.error('Cannot find author');
      return;
    }

    const commentRequest: CommentRequest = {
      author: this.author,
      articleId: this.articleId,
      content: this.commentForm.value,
      createdAt: new Date(),
    };

    this.commentService.add(commentRequest).subscribe({
      next: () => {
        this.snackService.inform('Comment added !');
        this.commentAdded.emit();
      },
    });
  }
}
