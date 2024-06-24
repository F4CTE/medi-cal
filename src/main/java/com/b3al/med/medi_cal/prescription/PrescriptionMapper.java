package com.b3al.med.medi_cal.prescription;

import com.b3al.med.medi_cal.appointment.Appointment;
import com.b3al.med.medi_cal.appointment.AppointmentRepository;
import com.b3al.med.medi_cal.util.NotFoundException;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;


@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PrescriptionMapper {

    @Mapping(target = "appointment", ignore = true)
    PrescriptionDTO updatePrescriptionDTO(Prescription prescription,
            @MappingTarget PrescriptionDTO prescriptionDTO);

    @AfterMapping
    default void afterUpdatePrescriptionDTO(Prescription prescription,
            @MappingTarget PrescriptionDTO prescriptionDTO) {
        prescriptionDTO.setAppointment(prescription.getAppointment() == null ? null : prescription.getAppointment().getId());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "appointment", ignore = true)
    Prescription updatePrescription(PrescriptionDTO prescriptionDTO,
            @MappingTarget Prescription prescription,
            @Context AppointmentRepository appointmentRepository);

    @AfterMapping
    default void afterUpdatePrescription(PrescriptionDTO prescriptionDTO,
            @MappingTarget Prescription prescription,
            @Context AppointmentRepository appointmentRepository) {
        final Appointment appointment = prescriptionDTO.getAppointment() == null ? null : appointmentRepository.findById(prescriptionDTO.getAppointment())
                .orElseThrow(() -> new NotFoundException("appointment not found"));
        prescription.setAppointment(appointment);
    }

}
