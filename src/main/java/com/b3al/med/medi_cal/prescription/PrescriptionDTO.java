package com.b3al.med.medi_cal.prescription;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PrescriptionDTO {

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

    private Long appointment;

}
