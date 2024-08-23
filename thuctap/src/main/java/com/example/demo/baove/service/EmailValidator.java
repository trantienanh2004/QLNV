package com.example.demo.baove.service;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    private String domain;

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
        // Bạn cần xác định cách nào để lấy giá trị từ annotation
        this.domain = constraintAnnotation.domain(); // Giả sử bạn có thuộc tính `domain` trong annotation
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

