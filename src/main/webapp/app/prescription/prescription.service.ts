import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { PrescriptionDTO } from 'app/prescription/prescription.model';
import { PagedModel } from 'app/common/list-helper/pagination.component';
import { map } from 'rxjs';
import { transformRecordToMap } from 'app/common/utils';


@Injectable({
  providedIn: 'root',
})
export class PrescriptionService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/prescriptions';

  getAllPrescriptions(params?: Record<string,string>) {
    return this.http.get<PagedModel<PrescriptionDTO>>(this.resourcePath, { params });
  }

  getPrescription(id: number) {
    return this.http.get<PrescriptionDTO>(this.resourcePath + '/' + id);
  }

  createPrescription(prescriptionDTO: PrescriptionDTO) {
    return this.http.post<number>(this.resourcePath, prescriptionDTO);
  }

  updatePrescription(id: number, prescriptionDTO: PrescriptionDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, prescriptionDTO);
  }

  deletePrescription(id: number) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

  getAppointmentValues() {
    return this.http.get<Record<string,number>>(this.resourcePath + '/appointmentValues')
        .pipe(map(transformRecordToMap));
  }

}
