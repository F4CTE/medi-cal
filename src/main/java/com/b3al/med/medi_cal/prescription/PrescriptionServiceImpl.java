package com.b3al.med.medi_cal.prescription;

import com.b3al.med.medi_cal.appointment.AppointmentRepository;
import com.b3al.med.medi_cal.util.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository appointmentRepository;
    private final PrescriptionMapper prescriptionMapper;

    public PrescriptionServiceImpl(final PrescriptionRepository prescriptionRepository,
            final AppointmentRepository appointmentRepository,
            final PrescriptionMapper prescriptionMapper) {
        this.prescriptionRepository = prescriptionRepository;
        this.appointmentRepository = appointmentRepository;
        this.prescriptionMapper = prescriptionMapper;
    }

    @Override
    public Page<PrescriptionDTO> findAll(final String filter, final Pageable pageable) {
        Page<Prescription> page;
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = prescriptionRepository.findAllById(longFilter, pageable);
        } else {
            page = prescriptionRepository.findAll(pageable);
        }
        return new PageImpl<>(page.getContent()
                .stream()
                .map(prescription -> prescriptionMapper.updatePrescriptionDTO(prescription, new PrescriptionDTO()))
                .toList(),
                pageable, page.getTotalElements());
    }

    @Override
    public PrescriptionDTO get(final Long id) {
        return prescriptionRepository.findById(id)
                .map(prescription -> prescriptionMapper.updatePrescriptionDTO(prescription, new PrescriptionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Long create(final PrescriptionDTO prescriptionDTO) {
        final Prescription prescription = new Prescription();
        prescriptionMapper.updatePrescription(prescriptionDTO, prescription, appointmentRepository);
        return prescriptionRepository.save(prescription).getId();
    }

    @Override
    public void update(final Long id, final PrescriptionDTO prescriptionDTO) {
        final Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        prescriptionMapper.updatePrescription(prescriptionDTO, prescription, appointmentRepository);
        prescriptionRepository.save(prescription);
    }

    @Override
    public void delete(final Long id) {
        prescriptionRepository.deleteById(id);
    }

}
