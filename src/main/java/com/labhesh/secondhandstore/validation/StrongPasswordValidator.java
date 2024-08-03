package com.labhesh.secondhandstore.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {

    private String entity;

    private static final String PASSWORD_PATTERN =
            "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    @Override
    public void initialize(StrongPassword constraintAnnotation) {
        this.entity = constraintAnnotation.entity();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        boolean matches = Pattern.compile(PASSWORD_PATTERN).matcher(password).matches();

        if (!matches) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(entity + " must be at least 8 characters long, and must include at least one uppercase letter, one lowercase letter, one digit, and one special character.")
                    .addConstraintViolation();
        }

        return matches;
    }
}
