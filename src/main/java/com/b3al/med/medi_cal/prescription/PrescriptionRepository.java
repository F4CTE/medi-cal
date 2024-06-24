package com.b3al.med.medi_cal.prescription;

import com.b3al.med.medi_cal.appointment.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface PrescriptionRepository extends MongoRepository<Prescription, Long> {

    Page<Prescription> findAllById(Long id, Pageable pageable);

    Prescription findFirstByAppointment(Appointment appointment);

}
