package com.b3al.med.medi_cal.appointment;

import com.b3al.med.medi_cal.patient.Patient;
import com.b3al.med.medi_cal.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface AppointmentRepository extends MongoRepository<Appointment, Long> {

    Page<Appointment> findAllById(Long id, Pageable pageable);

    Appointment findFirstByDoctor(User user);

    Appointment findFirstByPatient(Patient patient);

}
