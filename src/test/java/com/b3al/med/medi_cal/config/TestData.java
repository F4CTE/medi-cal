package com.b3al.med.medi_cal.config;

import com.b3al.med.medi_cal.appointment.Appointment;
import com.b3al.med.medi_cal.appointment.AppointmentRepository;
import com.b3al.med.medi_cal.patient.Gender;
import com.b3al.med.medi_cal.patient.Patient;
import com.b3al.med.medi_cal.patient.PatientRepository;
import com.b3al.med.medi_cal.prescription.Prescription;
import com.b3al.med.medi_cal.prescription.PrescriptionRepository;
import com.b3al.med.medi_cal.user.User;
import com.b3al.med.medi_cal.user.UserRepository;
import com.b3al.med.medi_cal.user.UserRole;
import com.b3al.med.medi_cal.user.UserStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class TestData {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public PatientRepository patientRepository;

    @Autowired
    public AppointmentRepository appointmentRepository;

    @Autowired
    public PrescriptionRepository prescriptionRepository;

    public void clearAll() {
        userRepository.deleteAll();
        patientRepository.deleteAll();
        appointmentRepository.deleteAll();
        prescriptionRepository.deleteAll();
    }

    public void user() {
        final User user = new User();
        user.setId((long)1000);
        user.setUsername("admin");
        user.setPassword("{bcrypt}$2a$10$FMzmOkkfbApEWxS.4XzCKOR7EbbiwzkPEyGgYh6uQiPxurkpzRMa6");
        user.setRole(UserRole.ADMIN);
        user.setStatus(UserStatus.ACTIVE);
        user.setFirstname("Sed diam voluptua.");
        user.setLastname("Nulla facilisis.");
        user.setSpecialization("Ut wisi enim.");
        userRepository.save(user);
        final User user1 = new User();
        user1.setId((long)1001);
        user1.setUsername("doctor");
        user1.setPassword("{bcrypt}$2a$10$FMzmOkkfbApEWxS.4XzCKOR7EbbiwzkPEyGgYh6uQiPxurkpzRMa6");
        user1.setRole(UserRole.ADMIN);
        user1.setStatus(UserStatus.ACTIVE);
        user1.setFirstname("At vero eos.");
        user1.setLastname("Et ea rebum.");
        user1.setSpecialization("Nam liber tempor.");
        userRepository.save(user1);
    }

    public void patient() {
        final Patient patient = new Patient();
        patient.setId((long)1100);
        patient.setSsn("No sea takimata.");
        patient.setFirstName("Nulla facilisis.");
        patient.setLastName("Sed diam voluptua.");
        patient.setDob(LocalDate.of(2023, 7, 4));
        patient.setGender(Gender.MALE);
        patientRepository.save(patient);
        final Patient patient1 = new Patient();
        patient1.setId((long)1101);
        patient1.setSsn("Vel illum dolore.");
        patient1.setFirstName("Et ea rebum.");
        patient1.setLastName("At vero eos.");
        patient1.setDob(LocalDate.of(2023, 7, 5));
        patient1.setGender(Gender.MALE);
        patientRepository.save(patient1);
    }

    public void appointment() {
        final Appointment appointment = new Appointment();
        appointment.setId((long)1200);
        appointment.setNotes("Quis nostrud exerci.");
        appointment.setVisitDate(LocalDateTime.of(2024, 5, 6, 14, 30));
        appointmentRepository.save(appointment);
        final Appointment appointment1 = new Appointment();
        appointment1.setId((long)1201);
        appointment1.setNotes("Commodo consequat.");
        appointment1.setVisitDate(LocalDateTime.of(2024, 5, 7, 14, 30));
        appointmentRepository.save(appointment1);
    }

    public void prescription() {
        final Prescription prescription = new Prescription();
        prescription.setId((long)1300);
        prescription.setMedicine("No sea takimata.");
        prescription.setDosage("Stet clita kasd.");
        prescription.setDuration("Et ea rebum.");
        prescriptionRepository.save(prescription);
        final Prescription prescription1 = new Prescription();
        prescription1.setId((long)1301);
        prescription1.setMedicine("Vel illum dolore.");
        prescription1.setDosage("No sea takimata.");
        prescription1.setDuration("Eget est lorem.");
        prescriptionRepository.save(prescription1);
    }

}
