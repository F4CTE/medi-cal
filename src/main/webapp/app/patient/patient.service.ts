import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { PatientDTO } from 'app/patient/patient.model';
import { PagedModel } from 'app/common/list-helper/pagination.component';


@Injectable({
  providedIn: 'root',
})
export class PatientService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/patients';

  getAllPatients(params?: Record<string,string>) {
    return this.http.get<PagedModel<PatientDTO>>(this.resourcePath, { params });
  }

  getPatient(id: number) {
    return this.http.get<PatientDTO>(this.resourcePath + '/' + id);
  }

  createPatient(patientDTO: PatientDTO) {
    return this.http.post<number>(this.resourcePath, patientDTO);
  }

  updatePatient(id: number, patientDTO: PatientDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, patientDTO);
  }

  deletePatient(id: number) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

}
