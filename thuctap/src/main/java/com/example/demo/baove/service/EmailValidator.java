package com.example.demo.baove.service;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    private String domain;

    @Override
    public void initialize(ValidEmail constraintAnnotation) {

        this.domain = constraintAnnotation.domain();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || domain == null) {
            return false;
        }
        if (domain.equals("fe")) {
            return value.endsWith("@fe.edu.vn");
        } else if (domain.equals("fpt")) {
            return value.endsWith("@fpt.edu.vn");
        }
        return false;
    }
}

