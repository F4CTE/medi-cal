import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { PatientService } from 'app/patient/patient.service';
import { PatientDTO } from 'app/patient/patient.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';


@Component({
  selector: 'app-patient-add',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './patient-add.component.html'
})
export class PatientAddComponent {

  patientService = inject(PatientService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  addForm = new FormGroup({
    ssn: new FormControl(null, [Validators.required, Validators.maxLength(50)]),
    firstName: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    lastName: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    dob: new FormControl(null, [Validators.required]),
    gender: new FormControl(null, [Validators.required])
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      created: $localize`:@@patient.create.success:Patient was created successfully.`,
      PATIENT_SSN_UNIQUE: $localize`:@@Exists.patient.ssn:This Ssn is already taken.`
    };
    return messages[key];
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    if (!this.addForm.valid) {
      return;
    }
    const data = new PatientDTO(this.addForm.value);
    this.patientService.createPatient(data)
        .subscribe({
          next: () => this.router.navigate(['/patients'], {
            state: {
              msgSuccess: this.getMessage('created')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
        });
  }

}
