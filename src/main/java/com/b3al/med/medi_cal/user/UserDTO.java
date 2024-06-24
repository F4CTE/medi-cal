package com.b3al.med.medi_cal.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserDTO {

    private Long id;

    @NotNull
    @Size(max = 50)
    @UserUsernameUnique
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

}
