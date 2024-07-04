import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { PrescriptionService } from 'app/prescription/prescription.service';
import { PrescriptionDTO } from 'app/prescription/prescription.model';
import { SearchFilterComponent } from 'app/common/list-helper/search-filter.component';
import { SortingComponent } from 'app/common/list-helper/sorting.component';
import { getListParams } from 'app/common/utils';
import { PagedModel, PaginationComponent } from 'app/common/list-helper/pagination.component';


@Component({
  selector: 'app-prescription-list',
  standalone: true,
  imports: [CommonModule, SearchFilterComponent ,SortingComponent, PaginationComponent, RouterLink],
  templateUrl: './prescription-list.component.html'})
export class PrescriptionListComponent implements OnInit, OnDestroy {

  prescriptionService = inject(PrescriptionService);
  errorHandler = inject(ErrorHandler);
  route = inject(ActivatedRoute);
  router = inject(Router);
  prescriptions?: PagedModel<PrescriptionDTO>;
  navigationSubscription?: Subscription;

  sortOptions = {
    'id,ASC': $localize`:@@prescription.list.sort.id,ASC:Sort by Id (Ascending)`,
    'medicine,ASC': $localize`:@@prescription.list.sort.medicine,ASC:Sort by Medicine (Ascending)`,
    'dosage,ASC': $localize`:@@prescription.list.sort.dosage,ASC:Sort by Dosage (Ascending)`
  }

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      confirm: $localize`:@@delete.confirm:Êtes-vous sur ? Confirmer la suppression.`,
      deleted: $localize`:@@prescription.delete.success:L'ordonnance a bien été supprimée !`    };
    return messages[key];
  }

  ngOnInit() {
    this.loadData();
    this.navigationSubscription = this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.loadData();
      }
    });
  }

  ngOnDestroy() {
    this.navigationSubscription!.unsubscribe();
  }

  loadData() {
    this.prescriptionService.getAllPrescriptions(getListParams(this.route))
        .subscribe({
          next: (data) => this.prescriptions = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  confirmDelete(id: number) {
    if (confirm(this.getMessage('confirm'))) {
      this.prescriptionService.deletePrescription(id)
          .subscribe({
            next: () => this.router.navigate(['/prescriptions'], {
              state: {
                msgInfo: this.getMessage('deleted')
              }
            }),
            error: (error) => this.errorHandler.handleServerError(error.error)
          });
    }
  }

}
