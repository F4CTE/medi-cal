import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { PatientService } from 'app/patient/patient.service';
import { PatientDTO } from 'app/patient/patient.model';
import { SearchFilterComponent } from 'app/common/list-helper/search-filter.component';
import { SortingComponent } from 'app/common/list-helper/sorting.component';
import { getListParams } from 'app/common/utils';
import { PagedModel, PaginationComponent } from 'app/common/list-helper/pagination.component';


@Component({
  selector: 'app-patient-list',
  standalone: true,
  imports: [CommonModule, SearchFilterComponent ,SortingComponent, PaginationComponent, RouterLink],
  templateUrl: './patient-list.component.html'})
export class PatientListComponent implements OnInit, OnDestroy {

  patientService = inject(PatientService);
  errorHandler = inject(ErrorHandler);
  route = inject(ActivatedRoute);
  router = inject(Router);
  patients?: PagedModel<PatientDTO>;
  navigationSubscription?: Subscription;

  sortOptions = {
    'id,ASC': $localize`:@@patient.list.sort.id,ASC:Sort by Id (Ascending)`,
    'ssn,ASC': $localize`:@@patient.list.sort.ssn,ASC:Sort by Ssn (Ascending)`,
    'firstName,ASC': $localize`:@@patient.list.sort.firstName,ASC:Sort by First Name (Ascending)`
  }

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      confirm: $localize`:@@delete.confirm:Êtes-vous sur ? Confirmer la suppression`,
      deleted: $localize`:@@patient.delete.success:Le patient a bien été supprimé !`,
      'patient.appointment.patient.referenced': $localize`:@@patient.appointment.patient.referenced:This entity is still referenced by Appointment ${details?.id} via field Patient.`
    };
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
    this.patientService.getAllPatients(getListParams(this.route))
        .subscribe({
          next: (data) => this.patients = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  confirmDelete(id: number) {
    if (confirm(this.getMessage('confirm'))) {
      this.patientService.deletePatient(id)
          .subscribe({
            next: () => this.router.navigate(['/patients'], {
              state: {
                msgInfo: this.getMessage('deleted')
              }
            }),
            error: (error) => {
              if (error.error?.code === 'REFERENCED') {
                const messageParts = error.error.message.split(',');
                this.router.navigate(['/patients'], {
                  state: {
                    msgError: this.getMessage(messageParts[0], { id: messageParts[1] })
                  }
                });
                return;
              }
              this.errorHandler.handleServerError(error.error)
            }
          });
    }
  }

}
