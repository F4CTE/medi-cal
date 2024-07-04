package com.b3al.med.medi_cal.user;

import lombok.experimental.FieldNameConstants;


@FieldNameConstants(onlyExplicitlyIncluded = true)
public enum UserRole {

    @FieldNameConstants.Include
    ADMIN,
    @FieldNameConstants.Include
    DOCTOR,
    @FieldNameConstants.Include
    SERVER

}
