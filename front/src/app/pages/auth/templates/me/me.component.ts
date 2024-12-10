import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SessionService } from '../../../../services/session.service';
import { UserService } from '../../../../services/user.service';
import { User } from '../../../../interfaces/user';

@Component({
  selector: 'app-me',
  imports: [],
  templateUrl: './me.component.html',
  styleUrl: './me.component.scss',
})
export class MeComponent implements OnInit {
  public user: User | undefined;

  constructor(
    private router: Router,
    private sessionService: SessionService,
    private userService: UserService,
  ) {}

  public ngOnInit(): void {
    this.userService
      .getById(this.sessionService.sessionInformation!.id)
      .subscribe((user: User) => (this.user = user));
  }
}
