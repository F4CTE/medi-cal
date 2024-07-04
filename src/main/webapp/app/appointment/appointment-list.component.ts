import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { AppointmentService } from 'app/appointment/appointment.service';
import { AppointmentDTO } from 'app/appointment/appointment.model';
import { SearchFilterComponent } from 'app/common/list-helper/search-filter.component';
import { SortingComponent } from 'app/common/list-helper/sorting.component';
import { getListParams } from 'app/common/utils';
import { PagedModel, PaginationComponent } from 'app/common/list-helper/pagination.component';


@Component({
  selector: 'app-appointment-list',
  standalone: true,
  imports: [CommonModule, SearchFilterComponent ,SortingComponent, PaginationComponent, RouterLink],
  templateUrl: './appointment-list.component.html'})
export class AppointmentListComponent implements OnInit, OnDestroy {

  appointmentService = inject(AppointmentService);
  errorHandler = inject(ErrorHandler);
  route = inject(ActivatedRoute);
  router = inject(Router);
  appointments?: PagedModel<AppointmentDTO>;
  navigationSubscription?: Subscription;

  sortOptions = {
    'id,ASC': $localize`:@@appointment.list.sort.id,ASC:Sort by Id (Ascending)`,
    'notes,ASC': $localize`:@@appointment.list.sort.notes,ASC:Sort by Notes (Ascending)`,
    'visitDate,ASC': $localize`:@@appointment.list.sort.visitDate,ASC:Sort by Visit Date (Ascending)`
  }

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      confirm: $localize`:@@delete.confirm:Êtes-vous sur ? Confirmer la suppression`,
      deleted: $localize`:@@appointment.delete.success:Le rendez vous à bien été supprimé.`,
      'appointment.prescription.appointment.referenced': $localize`:@@appointment.prescription.appointment.referenced:This entity is still referenced by Prescription ${details?.id} via field Appointment.`
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
    this.appointmentService.getAllAppointments(getListParams(this.route))
        .subscribe({
          next: (data) => this.appointments = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  confirmDelete(id: number) {
    if (confirm(this.getMessage('confirm'))) {
      this.appointmentService.deleteAppointment(id)
          .subscribe({
            next: () => this.router.navigate(['/appointments'], {
              state: {
                msgInfo: this.getMessage('deleted')
              }
            }),
            error: (error) => {
              if (error.error?.code === 'REFERENCED') {
                const messageParts = error.error.message.split(',');
                this.router.navigate(['/appointments'], {
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
