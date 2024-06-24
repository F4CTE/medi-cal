import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { AppointmentDTO } from 'app/appointment/appointment.model';
import { PagedModel } from 'app/common/list-helper/pagination.component';
import { map } from 'rxjs';
import { transformRecordToMap } from 'app/common/utils';


@Injectable({
  providedIn: 'root',
})
export class AppointmentService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/appointments';

  getAllAppointments(params?: Record<string,string>) {
    return this.http.get<PagedModel<AppointmentDTO>>(this.resourcePath, { params });
  }

  getAppointment(id: number) {
    return this.http.get<AppointmentDTO>(this.resourcePath + '/' + id);
  }

  createAppointment(appointmentDTO: AppointmentDTO) {
    return this.http.post<number>(this.resourcePath, appointmentDTO);
  }

  updateAppointment(id: number, appointmentDTO: AppointmentDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, appointmentDTO);
  }

  deleteAppointment(id: number) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

  getDoctorValues() {
    return this.http.get<Record<string,string>>(this.resourcePath + '/doctorValues')
        .pipe(map(transformRecordToMap));
  }

  getPatientValues() {
    return this.http.get<Record<string,string>>(this.resourcePath + '/patientValues')
        .pipe(map(transformRecordToMap));
  }

}
