package com.b3al.med.medi_cal.patient;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface PatientRepository extends MongoRepository<Patient, Long> {

    Page<Patient> findAllById(Long id, Pageable pageable);

    boolean existsBySsnIgnoreCase(String ssn);

}
