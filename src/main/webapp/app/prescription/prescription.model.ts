export class PrescriptionDTO {

  constructor(data:Partial<PrescriptionDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  medicine?: string|null;
  dosage?: string|null;
  duration?: string|null;
  appointment?: number|null;

}
