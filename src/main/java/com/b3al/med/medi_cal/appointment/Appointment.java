package com.b3al.med.medi_cal.appointment;

import com.b3al.med.medi_cal.patient.Patient;
import com.b3al.med.medi_cal.prescription.Prescription;
import com.b3al.med.medi_cal.user.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;


@Document
@Getter
@Setter
public class Appointment {

    @Id
    private Long id;

    @Size(max = 1000)
    private String notes;

    @NotNull
    private LocalDateTime visitDate;

    @DocumentReference(lazy = true, lookup = "{ 'appointment' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<Prescription> prescriptions;

    @DocumentReference(lazy = true)
    private User doctor;

    @DocumentReference(lazy = true)
    private Patient patient;

    @CreatedDate
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    private OffsetDateTime lastUpdated;

    @Version
    private Integer version;

}
