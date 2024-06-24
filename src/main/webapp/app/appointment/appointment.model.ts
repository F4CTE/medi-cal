export class AppointmentDTO {

  constructor(data:Partial<AppointmentDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  notes?: string|null;
  visitDate?: string|null;
  doctor?: number|null;
  patient?: number|null;

}
