package com.b3al.med.medi_cal.patient;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;


@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PatientMapper {

    PatientDTO updatePatientDTO(Patient patient, @MappingTarget PatientDTO patientDTO);

    @Mapping(target = "id", ignore = true)
    Patient updatePatient(PatientDTO patientDTO, @MappingTarget Patient patient);

}
