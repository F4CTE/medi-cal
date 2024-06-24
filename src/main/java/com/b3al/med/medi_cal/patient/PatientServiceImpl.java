package com.b3al.med.medi_cal.patient;

import com.b3al.med.medi_cal.appointment.Appointment;
import com.b3al.med.medi_cal.appointment.AppointmentRepository;
import com.b3al.med.medi_cal.util.NotFoundException;
import com.b3al.med.medi_cal.util.ReferencedWarning;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final AppointmentRepository appointmentRepository;

    public PatientServiceImpl(final PatientRepository patientRepository,
            final PatientMapper patientMapper, final AppointmentRepository appointmentRepository) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public Page<PatientDTO> findAll(final String filter, final Pageable pageable) {
        Page<Patient> page;
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = patientRepository.findAllById(longFilter, pageable);
        } else {
            page = patientRepository.findAll(pageable);
        }
        return new PageImpl<>(page.getContent()
                .stream()
                .map(patient -> patientMapper.updatePatientDTO(patient, new PatientDTO()))
                .toList(),
                pageable, page.getTotalElements());
    }

    @Override
    public PatientDTO get(final Long id) {
        return patientRepository.findById(id)
                .map(patient -> patientMapper.updatePatientDTO(patient, new PatientDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Long create(final PatientDTO patientDTO) {
        final Patient patient = new Patient();
        patientMapper.updatePatient(patientDTO, patient);
        return patientRepository.save(patient).getId();
    }

    @Override
    public void update(final Long id, final PatientDTO patientDTO) {
        final Patient patient = patientRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        patientMapper.updatePatient(patientDTO, patient);
        patientRepository.save(patient);
    }

    @Override
    public void delete(final Long id) {
        patientRepository.deleteById(id);
    }

    @Override
    public boolean ssnExists(final String ssn) {
        return patientRepository.existsBySsnIgnoreCase(ssn);
    }

    @Override
    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Patient patient = patientRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Appointment patientAppointment = appointmentRepository.findFirstByPatient(patient);
        if (patientAppointment != null) {
            referencedWarning.setKey("patient.appointment.patient.referenced");
            referencedWarning.addParam(patientAppointment.getId());
            return referencedWarning;
        }
        return null;
    }

}
