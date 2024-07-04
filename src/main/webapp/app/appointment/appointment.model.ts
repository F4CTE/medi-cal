export class AppointmentDTO {
  id?: number | null = null;
  notes?: string | null = null;
  visitDate?: string | null = null;
  doctor?: number | null = null;
  patient?: number | null = null;

  constructor(data?: Partial<AppointmentDTO>) {
    if (data) {
      Object.assign(this, data);
    }
  }
}
