package com.b3al.med.medi_cal.appointment;

import com.b3al.med.medi_cal.patient.PatientRepository;
import com.b3al.med.medi_cal.prescription.Prescription;
import com.b3al.med.medi_cal.prescription.PrescriptionRepository;
import com.b3al.med.medi_cal.user.UserRepository;
import com.b3al.med.medi_cal.util.NotFoundException;
import com.b3al.med.medi_cal.util.ReferencedWarning;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final AppointmentMapper appointmentMapper;
    private final PrescriptionRepository prescriptionRepository;

    public AppointmentServiceImpl(final AppointmentRepository appointmentRepository,
            final UserRepository userRepository, final PatientRepository patientRepository,
            final AppointmentMapper appointmentMapper,
            final PrescriptionRepository prescriptionRepository) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.appointmentMapper = appointmentMapper;
        this.prescriptionRepository = prescriptionRepository;
    }

    @Override
    public Page<AppointmentDTO> findAll(final String filter, final Pageable pageable) {
        Page<Appointment> page;
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = appointmentRepository.findAllById(longFilter, pageable);
        } else {
            page = appointmentRepository.findAll(pageable);
        }
        return new PageImpl<>(page.getContent()
                .stream()
                .map(appointment -> appointmentMapper.updateAppointmentDTO(appointment, new AppointmentDTO()))
                .toList(),
                pageable, page.getTotalElements());
    }

    @Override
    public AppointmentDTO get(final Long id) {
        return appointmentRepository.findById(id)
                .map(appointment -> appointmentMapper.updateAppointmentDTO(appointment, new AppointmentDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Long create(final AppointmentDTO appointmentDTO) {
        final Appointment appointment = new Appointment();
        appointmentMapper.updateAppointment(appointmentDTO, appointment, userRepository, patientRepository);
        return appointmentRepository.save(appointment).getId();
    }

    @Override
    public void update(final Long id, final AppointmentDTO appointmentDTO) {
        final Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        appointmentMapper.updateAppointment(appointmentDTO, appointment, userRepository, patientRepository);
        appointmentRepository.save(appointment);
    }

    @Override
    public void delete(final Long id) {
        appointmentRepository.deleteById(id);
    }

    @Override
    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Prescription appointmentPrescription = prescriptionRepository.findFirstByAppointment(appointment);
        if (appointmentPrescription != null) {
            referencedWarning.setKey("appointment.prescription.appointment.referenced");
            referencedWarning.addParam(appointmentPrescription.getId());
            return referencedWarning;
        }
        return null;
    }

}
