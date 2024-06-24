import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { AppointmentService } from 'app/appointment/appointment.service';
import { AppointmentDTO } from 'app/appointment/appointment.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { updateForm } from 'app/common/utils';


@Component({
  selector: 'app-appointment-edit',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './appointment-edit.component.html'
})
export class AppointmentEditComponent implements OnInit {

  appointmentService = inject(AppointmentService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  doctorValues?: Map<number,string>;
  patientValues?: Map<number,string>;
  currentId?: number;

  editForm = new FormGroup({
    id: new FormControl({ value: null, disabled: true }),
    notes: new FormControl(null, [Validators.maxLength(1000)]),
    visitDate: new FormControl(null, [Validators.required]),
    doctor: new FormControl(null),
    patient: new FormControl(null)
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      updated: $localize`:@@appointment.update.success:Appointment was updated successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.currentId = +this.route.snapshot.params['id'];
    this.appointmentService.getDoctorValues()
        .subscribe({
          next: (data) => this.doctorValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
    this.appointmentService.getPatientValues()
        .subscribe({
          next: (data) => this.patientValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
    this.appointmentService.getAppointment(this.currentId!)
        .subscribe({
          next: (data) => updateForm(this.editForm, data),
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.editForm.markAllAsTouched();
    if (!this.editForm.valid) {
      return;
    }
    const data = new AppointmentDTO(this.editForm.value);
    this.appointmentService.updateAppointment(this.currentId!, data)
        .subscribe({
          next: () => this.router.navigate(['/appointments'], {
            state: {
              msgSuccess: this.getMessage('updated')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.editForm, this.getMessage)
        });
  }

}
