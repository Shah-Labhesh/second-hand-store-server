package com.labhesh.secondhandstore.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OtpValidator implements ConstraintValidator<ValidOtp, String> {

    private static final String OTP_PATTERN = "^\\d{5}$"; // Adjust the pattern if the OTP length or format changes

    @Override
    public void initialize(ValidOtp constraintAnnotation) {
    }

    @Override
    public boolean isValid(String otp, ConstraintValidatorContext context) {
        if (otp == null) {
            return false;
        }
        return otp.matches(OTP_PATTERN);
    }
}

