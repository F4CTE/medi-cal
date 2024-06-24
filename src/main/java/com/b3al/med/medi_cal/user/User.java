package com.b3al.med.medi_cal.user;

import com.b3al.med.medi_cal.appointment.Appointment;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class User {

    @Id
    private Long id;

    @Indexed(unique = true)
    @NotNull
    @Size(max = 50)
    private String username;

    @Size(max = 255)
    private String password;

    @NotNull
    private UserRole role;

    @NotNull
    private UserStatus status;

    @Size(max = 50)
    private String firstname;

    @Size(max = 255)
    private String lastname;

    @Size(max = 255)
    private String specialization;

    @DocumentReference(lazy = true, lookup = "{ 'doctor' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<Appointment> appointment;

    @CreatedDate
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    private OffsetDateTime lastUpdated;

    @Version
    private Integer version;

}
