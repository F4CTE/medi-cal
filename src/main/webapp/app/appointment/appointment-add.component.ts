import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { AppointmentService } from 'app/appointment/appointment.service';
import { AppointmentDTO } from 'app/appointment/appointment.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';


@Component({
  selector: 'app-appointment-add',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './appointment-add.component.html'
})
export class AppointmentAddComponent implements OnInit {

  appointmentService = inject(AppointmentService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  doctorValues?: Map<number,string>;
  patientValues?: Map<number,string>;

  addForm = new FormGroup({
    notes: new FormControl(null, [Validators.maxLength(1000)]),
    visitDate: new FormControl(null, [Validators.required]),
    doctor: new FormControl(null),
    patient: new FormControl(null)
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      created: $localize`:@@appointment.create.success:Appointment was created successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
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
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    if (!this.addForm.valid) {
      return;
    }
    const data = new AppointmentDTO(this.addForm.value);
    this.appointmentService.createAppointment(data)
        .subscribe({
          next: () => this.router.navigate(['/appointments'], {
            state: {
              msgSuccess: this.getMessage('created')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
        });
  }

}
