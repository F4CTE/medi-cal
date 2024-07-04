import {inject} from '@angular/core';
import {ActivatedRouteSnapshot, Routes} from '@angular/router';
import {HomeComponent} from './home/home.component';
import {UserListComponent} from './user/user-list.component';
import {UserAddComponent} from './user/user-add.component';
import {UserEditComponent} from './user/user-edit.component';
import {PatientListComponent} from './patient/patient-list.component';
import {PatientAddComponent} from './patient/patient-add.component';
import {PatientEditComponent} from './patient/patient-edit.component';
import {AppointmentListComponent} from './appointment/appointment-list.component';
import {AppointmentAddComponent} from './appointment/appointment-add.component';
import {AppointmentEditComponent} from './appointment/appointment-edit.component';
import {PrescriptionListComponent} from './prescription/prescription-list.component';
import {PrescriptionAddComponent} from './prescription/prescription-add.component';
import {PrescriptionEditComponent} from './prescription/prescription-edit.component';
import {AuthenticationComponent} from './security/authentication.component';
import {ErrorComponent} from './error/error.component';
import {ADMIN, AuthenticationService, DOCTOR} from 'app/security/authentication.service';
import {ResetPasswordComponent} from "./reset-password/reset-password.component";


export const routes: Routes = [

  {
    path: 'reset-password',
    component: ResetPasswordComponent,
    title: "",
    data: {
      roles: [ADMIN, DOCTOR]
    }

  },
  {
    path: '',
    component: HomeComponent,
    title: $localize`:@@home.index.headline:Welcome to your new app!`,
    data: {
      roles: [ADMIN, DOCTOR]
    }
  },
  {
    path: 'users',
    component: UserListComponent,
    title: $localize`:@@user.list.headline:Users`,
    data: {
      roles: [ADMIN]
    }
  },
  {
    path: 'users/add',
    component: UserAddComponent,
    title: $localize`:@@user.add.headline:Add User`,
    data: {
      roles: [ADMIN]
    }
  },
  {
    path: 'users/edit/:id',
    component: UserEditComponent,
    title: $localize`:@@user.edit.headline:Edit User`,
    data: {
      roles: [ADMIN],
    }
  },
  {
    path: 'patients',
    component: PatientListComponent,
    title: $localize`:@@patient.list.headline:Patients`,
    data: {
      roles: [ADMIN]
    }
  },
  {
    path: 'patients/add',
    component: PatientAddComponent,
    title: $localize`:@@patient.add.headline:Add Patient`,
    data: {
      roles: [ADMIN]
    }
  },
  {
    path: 'patients/edit/:id',
    component: PatientEditComponent,
    title: $localize`:@@patient.edit.headline:Edit Patient`,
    data: {
      roles: [ADMIN]
    }
  },
  {
    path: 'appointments',
    component: AppointmentListComponent,
    title: $localize`:@@appointment.list.headline:Appointments`,
    data: {
      roles: [ADMIN, DOCTOR]
    }
  },
  {
    path: 'appointments/add',
    component: AppointmentAddComponent,
    title: $localize`:@@appointment.add.headline:Add Appointment`,
    data: {
      roles: [ADMIN, DOCTOR]
    }
  },
  {
    path: 'appointments/edit/:id',
    component: AppointmentEditComponent,
    title: $localize`:@@appointment.edit.headline:Edit Appointment`,
    data: {
      roles: [ADMIN, DOCTOR]
    }
  },
  {
    path: 'prescriptions',
    component: PrescriptionListComponent,
    title: $localize`:@@prescription.list.headline:Prescriptions`,
    data: {
      roles: [ADMIN, DOCTOR]
    }
  },
  {
    path: 'prescriptions/add',
    component: PrescriptionAddComponent,
    title: $localize`:@@prescription.add.headline:Add Prescription`,
    data: {
      roles: [ADMIN, DOCTOR]
    }
  },
  {
    path: 'prescriptions/edit/:id',
    component: PrescriptionEditComponent,
    title: $localize`:@@prescription.edit.headline:Edit Prescription`,
    data: {
      roles: [ADMIN, DOCTOR]
    }
  },
  {
    path: 'login',
    component: AuthenticationComponent,
    title: $localize`:@@authentication.login.headline:Login`
  },
  {
    path: 'error',
    component: ErrorComponent,
    title: $localize`:@@error.headline:Error`
  },
  {
    path: '**',
    component: ErrorComponent,
    title: $localize`:@@notFound.headline:Page not found`
  },

];

// add authentication check to all routes
for (const route of routes) {
  route.canActivate = [(route: ActivatedRouteSnapshot) => inject(AuthenticationService).checkAccessAllowed(route)];
}
