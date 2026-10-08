package com.tausifk.ecorys.application;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationFormValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validFormHasNoViolations() {
        assertThat(validator.validate(form("1234567890", "Rahim", "01712345678", LocalDate.now(), "Factory closed")))
                .isEmpty();
    }

    @Test
    void mandatoryFieldsAreRequired() {
        assertThat(invalidFields(form(null, " ", null, null, ""))).containsExactlyInAnyOrder(
                "nid", "name", "mobileNumber", "applicationDate", "reason");
    }

    @Test
    void rejectsMalformedNidMobileAndFutureDate() {
        assertThat(invalidFields(form("12345", "Rahim", "0123", LocalDate.now().plusDays(1), "Factory closed")))
                .containsExactlyInAnyOrder("nid", "mobileNumber", "applicationDate");
    }

    private Set<String> invalidFields(ApplicationForm form) {
        return validator.validate(form).stream()
                .map(ConstraintViolation::getPropertyPath)
                .map(Object::toString)
                .collect(Collectors.toSet());
    }

    private static ApplicationForm form(String nid, String name, String mobile, LocalDate date, String reason) {
        ApplicationForm form = new ApplicationForm();
        form.setNid(nid);
        form.setName(name);
        form.setMobileNumber(mobile);
        form.setApplicationDate(date);
        form.setReason(reason);
        return form;
    }
}
