package com.b3al.med.medi_cal.prescription;

import com.b3al.med.medi_cal.appointment.Appointment;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;


@Document
@Getter
@Setter
public class Prescription {

    @Id
    private Long id;

    @NotNull
    @Size(max = 255)
    private String medicine;

    @NotNull
    @Size(max = 255)
    private String dosage;

    @NotNull
    @Size(max = 255)
    private String duration;

    @DocumentReference(lazy = true)
    private Appointment appointment;

    @CreatedDate
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    private OffsetDateTime lastUpdated;

    @Version
    private Integer version;

}
