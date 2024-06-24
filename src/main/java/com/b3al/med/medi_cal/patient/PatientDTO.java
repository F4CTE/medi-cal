package com.b3al.med.medi_cal.patient;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PatientDTO {

    private Long id;

    @NotNull
    @Size(max = 50)
    @PatientSsnUnique
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

}
