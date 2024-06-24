package com.b3al.med.medi_cal.appointment;

import com.b3al.med.medi_cal.util.ReferencedWarning;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface AppointmentService {

    Page<AppointmentDTO> findAll(String filter, Pageable pageable);

    AppointmentDTO get(Long id);

    Long create(AppointmentDTO appointmentDTO);

    void update(Long id, AppointmentDTO appointmentDTO);

    void delete(Long id);

    ReferencedWarning getReferencedWarning(Long id);

}
