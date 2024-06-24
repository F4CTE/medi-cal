package com.b3al.med.medi_cal.appointment;

import com.b3al.med.medi_cal.patient.Patient;
import com.b3al.med.medi_cal.patient.PatientRepository;
import com.b3al.med.medi_cal.user.User;
import com.b3al.med.medi_cal.user.UserRepository;
import com.b3al.med.medi_cal.user.UserRole;
import com.b3al.med.medi_cal.util.CustomCollectors;
import com.b3al.med.medi_cal.util.ReferencedException;
import com.b3al.med.medi_cal.util.ReferencedWarning;
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
@RequestMapping(value = "/api/appointments", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyAuthority('" + UserRole.Fields.ADMIN + "', '" + UserRole.Fields.DOCTOR + "')")
@SecurityRequirement(name = "bearer-jwt")
public class AppointmentResource {

    private final AppointmentService appointmentService;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;

    public AppointmentResource(final AppointmentService appointmentService,
            final UserRepository userRepository, final PatientRepository patientRepository) {
        this.appointmentService = appointmentService;
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
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
    public ResponseEntity<Page<AppointmentDTO>> getAllAppointments(
            @RequestParam(name = "filter", required = false) final String filter,
            @Parameter(hidden = true) @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable) {
        return ResponseEntity.ok(appointmentService.findAll(filter, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> getAppointment(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(appointmentService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createAppointment(
            @RequestBody @Valid final AppointmentDTO appointmentDTO) {
        final Long createdId = appointmentService.create(appointmentDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateAppointment(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final AppointmentDTO appointmentDTO) {
        appointmentService.update(id, appointmentDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAppointment(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = appointmentService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        appointmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/doctorValues")
    public ResponseEntity<Map<Long, String>> getDoctorValues() {
        return ResponseEntity.ok(userRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getId, User::getUsername)));
    }

    @GetMapping("/patientValues")
    public ResponseEntity<Map<Long, String>> getPatientValues() {
        return ResponseEntity.ok(patientRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Patient::getId, Patient::getSsn)));
    }

}
