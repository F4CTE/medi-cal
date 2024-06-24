package com.b3al.med.medi_cal.patient;

import com.b3al.med.medi_cal.appointment.Appointment;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;


@Document
@Getter
@Setter
public class Patient {

    @Id
    private Long id;

    @Indexed(unique = true)
    @NotNull
    @Size(max = 50)
    private String ssn;

    @NotNull
    @Size(max = 255)
    private String firstName;

    @NotNull
    @Size(max = 255)
    private String lastName;

    @NotNull
    private LocalDate dob;

    @NotNull
    private Gender gender;

    @DocumentReference(lazy = true, lookup = "{ 'patient' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<Appointment> appointments;

    @CreatedDate
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    private OffsetDateTime lastUpdated;

    @Version
    private Integer version;

}
