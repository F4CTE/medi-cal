import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { PrescriptionService } from 'app/prescription/prescription.service';
import { PrescriptionDTO } from 'app/prescription/prescription.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';


@Component({
  selector: 'app-prescription-add',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './prescription-add.component.html'
})
export class PrescriptionAddComponent implements OnInit {

  prescriptionService = inject(PrescriptionService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  appointmentValues?: Map<number,string>;

  addForm = new FormGroup({
    medicine: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    dosage: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    duration: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    appointment: new FormControl(null)
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      created: $localize`:@@prescription.create.success:Prescription was created successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.prescriptionService.getAppointmentValues()
        .subscribe({
          next: (data) => this.appointmentValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    if (!this.addForm.valid) {
      return;
    }
    const data = new PrescriptionDTO(this.addForm.value);
    this.prescriptionService.createPrescription(data)
        .subscribe({
          next: () => this.router.navigate(['/prescriptions'], {
            state: {
              msgSuccess: this.getMessage('created')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
        });
  }

}
