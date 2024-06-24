package com.b3al.med.medi_cal.prescription;

import com.b3al.med.medi_cal.appointment.Appointment;
import com.b3al.med.medi_cal.appointment.AppointmentRepository;
import com.b3al.med.medi_cal.user.UserRole;
import com.b3al.med.medi_cal.util.CustomCollectors;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/prescriptions", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyAuthority('" + UserRole.Fields.ADMIN + "', '" + UserRole.Fields.DOCTOR + "')")
@SecurityRequirement(name = "bearer-jwt")
public class PrescriptionResource {

    private final PrescriptionService prescriptionService;
    private final AppointmentRepository appointmentRepository;

    public PrescriptionResource(final PrescriptionService prescriptionService,
            final AppointmentRepository appointmentRepository) {
        this.prescriptionService = prescriptionService;
        this.appointmentRepository = appointmentRepository;
    }

    @Operation(
            parameters = {
                    @Parameter(
                            name = "page",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = Integer.class)
                    ),
                    @Parameter(
                            name = "size",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = Integer.class)
                    ),
                    @Parameter(
                            name = "sort",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = String.class)
                    )
            }
    )
    @GetMapping
    public ResponseEntity<Page<PrescriptionDTO>> getAllPrescriptions(
            @RequestParam(name = "filter", required = false) final String filter,
            @Parameter(hidden = true) @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable) {
        return ResponseEntity.ok(prescriptionService.findAll(filter, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionDTO> getPrescription(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(prescriptionService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createPrescription(
            @RequestBody @Valid final PrescriptionDTO prescriptionDTO) {
        final Long createdId = prescriptionService.create(prescriptionDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updatePrescription(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final PrescriptionDTO prescriptionDTO) {
        prescriptionService.update(id, prescriptionDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deletePrescription(@PathVariable(name = "id") final Long id) {
        prescriptionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/appointmentValues")
    public ResponseEntity<Map<Long, Long>> getAppointmentValues() {
        return ResponseEntity.ok(appointmentRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Appointment::getId, Appointment::getId)));
    }

}
