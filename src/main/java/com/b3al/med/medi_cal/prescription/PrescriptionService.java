package com.b3al.med.medi_cal.prescription;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface PrescriptionService {

    Page<PrescriptionDTO> findAll(String filter, Pageable pageable);

    PrescriptionDTO get(Long id);

    Long create(PrescriptionDTO prescriptionDTO);

    void update(Long id, PrescriptionDTO prescriptionDTO);

    void delete(Long id);

}
