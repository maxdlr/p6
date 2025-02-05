import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  inject,
  OnDestroy,
} from '@angular/core';
import { NavigationService } from '../../services/navigation.service';
import { NavigationItem } from '../../../../interfaces/navigation-item';
import { BreakpointObserver } from '@angular/cdk/layout';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { FixturesService } from '../../../../services/fixtures.service';
import { SnackService } from '../../../../services/snack.service';

@Component({
  selector: 'app-base',
  standalone: false,
  templateUrl: './base.component.html',
  styleUrl: './base.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class BaseComponent implements OnDestroy {
  navigation: NavigationItem[] = [];
  isMobile = false;
  private navigationService = inject(NavigationService);
  private breakPoints: BreakpointObserver = inject(BreakpointObserver);
  private cdr = inject(ChangeDetectorRef);
  private destroy$ = new Subject<void>();
  private fixturesService = inject(FixturesService);
  private snackService = inject(SnackService);

  constructor() {
    this.navigation = this.navigationService.navigationItems;
    this.breakPoints
      .observe('(max-width: 599px)')
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (result) => {
          this.isMobile = result.matches;
          this.cdr.markForCheck();
        },
      });

    this.isMobile = this.breakPoints.isMatched('(max-width: 599px)');
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  public loadFixtures() {
    this.fixturesService.loadFixtures().subscribe({
      next: () => {
        this.snackService.inform('Fixtures créés');
      },
    });
  }

  public removeFixtures() {
    this.fixturesService.removeFixtures().subscribe({
      next: () => {
        this.snackService.inform('Fixtures supprimées');
      },
    });
  }
}
