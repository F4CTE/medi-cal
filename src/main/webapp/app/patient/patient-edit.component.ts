import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { PatientService } from 'app/patient/patient.service';
import { PatientDTO } from 'app/patient/patient.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { updateForm } from 'app/common/utils';


@Component({
  selector: 'app-patient-edit',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './patient-edit.component.html'
})
export class PatientEditComponent implements OnInit {

  patientService = inject(PatientService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  currentId?: number;

  editForm = new FormGroup({
    id: new FormControl({ value: null, disabled: true }),
    ssn: new FormControl(null, [Validators.required, Validators.maxLength(50)]),
    firstName: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    lastName: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    dob: new FormControl(null, [Validators.required]),
    gender: new FormControl(null, [Validators.required])
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      updated: $localize`:@@patient.update.success:Patient was updated successfully.`,
      PATIENT_SSN_UNIQUE: $localize`:@@Exists.patient.ssn:This Ssn is already taken.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.currentId = +this.route.snapshot.params['id'];
    this.patientService.getPatient(this.currentId!)
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
    const data = new PatientDTO(this.editForm.value);
    this.patientService.updatePatient(this.currentId!, data)
        .subscribe({
          next: () => this.router.navigate(['/patients'], {
            state: {
              msgSuccess: this.getMessage('updated')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.editForm, this.getMessage)
        });
  }

}
