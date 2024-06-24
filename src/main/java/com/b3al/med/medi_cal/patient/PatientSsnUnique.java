package com.b3al.med.medi_cal.patient;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import org.springframework.web.servlet.HandlerMapping;


/**
 * Validate that the ssn value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = PatientSsnUnique.PatientSsnUniqueValidator.class
)
public @interface PatientSsnUnique {

    String message() default "{Exists.patient.ssn}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class PatientSsnUniqueValidator implements ConstraintValidator<PatientSsnUnique, String> {

        private final PatientService patientService;
        private final HttpServletRequest request;

        public PatientSsnUniqueValidator(final PatientService patientService,
                final HttpServletRequest request) {
            this.patientService = patientService;
            this.request = request;
        }

        @Override
        public boolean isValid(final String value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                    ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("id");
            if (currentId != null && value.equalsIgnoreCase(patientService.get(Long.parseLong(currentId)).getSsn())) {
                // value hasn't changed
                return true;
            }
            return !patientService.ssnExists(value);
        }

    }

}
