package com.b3al.med.medi_cal.appointment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AppointmentDTO {

    private Long id;

    @Size(max = 1000)
    private String notes;

    @NotNull
    private LocalDateTime visitDate;

    private Long doctor;

    private Long patient;

}
