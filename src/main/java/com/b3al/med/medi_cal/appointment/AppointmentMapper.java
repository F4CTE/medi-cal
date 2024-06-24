package com.b3al.med.medi_cal.appointment;

import com.b3al.med.medi_cal.patient.Patient;
import com.b3al.med.medi_cal.patient.PatientRepository;
import com.b3al.med.medi_cal.user.User;
import com.b3al.med.medi_cal.user.UserRepository;
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
public interface AppointmentMapper {

    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "patient", ignore = true)
    AppointmentDTO updateAppointmentDTO(Appointment appointment,
            @MappingTarget AppointmentDTO appointmentDTO);

    @AfterMapping
    default void afterUpdateAppointmentDTO(Appointment appointment,
            @MappingTarget AppointmentDTO appointmentDTO) {
        appointmentDTO.setDoctor(appointment.getDoctor() == null ? null : appointment.getDoctor().getId());
        appointmentDTO.setPatient(appointment.getPatient() == null ? null : appointment.getPatient().getId());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "patient", ignore = true)
    Appointment updateAppointment(AppointmentDTO appointmentDTO,
            @MappingTarget Appointment appointment, @Context UserRepository userRepository,
            @Context PatientRepository patientRepository);

    @AfterMapping
    default void afterUpdateAppointment(AppointmentDTO appointmentDTO,
            @MappingTarget Appointment appointment, @Context UserRepository userRepository,
            @Context PatientRepository patientRepository) {
        final User doctor = appointmentDTO.getDoctor() == null ? null : userRepository.findById(appointmentDTO.getDoctor())
                .orElseThrow(() -> new NotFoundException("doctor not found"));
        appointment.setDoctor(doctor);
        final Patient patient = appointmentDTO.getPatient() == null ? null : patientRepository.findById(appointmentDTO.getPatient())
                .orElseThrow(() -> new NotFoundException("patient not found"));
        appointment.setPatient(patient);
    }

}
