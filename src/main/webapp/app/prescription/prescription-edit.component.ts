import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { PrescriptionService } from 'app/prescription/prescription.service';
import { PrescriptionDTO } from 'app/prescription/prescription.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { updateForm } from 'app/common/utils';


@Component({
  selector: 'app-prescription-edit',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './prescription-edit.component.html'
})
export class PrescriptionEditComponent implements OnInit {

  prescriptionService = inject(PrescriptionService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  appointmentValues?: Map<number,string>;
  currentId?: number;

  editForm = new FormGroup({
    id: new FormControl({ value: null, disabled: true }),
    medicine: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    dosage: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    duration: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    appointment: new FormControl(null)
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      updated: $localize`:@@prescription.update.success:L'ordonnance a été mise à jour !`
    };
    return messages[key];
  }

  ngOnInit() {
    this.currentId = +this.route.snapshot.params['id'];
    this.prescriptionService.getAppointmentValues()
        .subscribe({
          next: (data) => this.appointmentValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
    this.prescriptionService.getPrescription(this.currentId!)
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
    const data = new PrescriptionDTO(this.editForm.value);
    this.prescriptionService.updatePrescription(this.currentId!, data)
        .subscribe({
          next: () => this.router.navigate(['/prescriptions'], {
            state: {
              msgSuccess: this.getMessage('updated')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.editForm, this.getMessage)
        });
  }

}
