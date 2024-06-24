package com.b3al.med.medi_cal.patient;

import com.b3al.med.medi_cal.user.UserRole;
import com.b3al.med.medi_cal.util.ReferencedException;
import com.b3al.med.medi_cal.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@RequestMapping(value = "/api/patients", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAuthority('" + UserRole.Fields.ADMIN + "')")
@SecurityRequirement(name = "bearer-jwt")
public class PatientResource {

    private final PatientService patientService;

    public PatientResource(final PatientService patientService) {
        this.patientService = patientService;
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
    @PreAuthorize("hasAnyAuthority('" + UserRole.Fields.ADMIN + "', '" + UserRole.Fields.DOCTOR + "')")
    public ResponseEntity<Page<PatientDTO>> getAllPatients(
            @RequestParam(name = "filter", required = false) final String filter,
            @Parameter(hidden = true) @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable) {
        return ResponseEntity.ok(patientService.findAll(filter, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('" + UserRole.Fields.ADMIN + "')")
    public ResponseEntity<PatientDTO> getPatient(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(patientService.get(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('" + UserRole.Fields.ADMIN + "')")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createPatient(@RequestBody @Valid final PatientDTO patientDTO) {
        final Long createdId = patientService.create(patientDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('" + UserRole.Fields.ADMIN + "')")
    public ResponseEntity<Long> updatePatient(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final PatientDTO patientDTO) {
        patientService.update(id, patientDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('" + UserRole.Fields.ADMIN + "')")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deletePatient(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = patientService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        patientService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
