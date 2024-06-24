export class PatientDTO {

  constructor(data:Partial<PatientDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  ssn?: string|null;
  firstName?: string|null;
  lastName?: string|null;
  dob?: string|null;
  gender?: string|null;

}
