package com.huunam.identity_service.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class DobValidator implements ConstraintValidator<DobConstrant, LocalDate> {

    private int min;

    @Override
    public void initialize(DobConstrant constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        min = constraintAnnotation.min(); //khoi tao method da khai bao trong Constrant
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (Objects.isNull(value))
            return true;
        Long years = ChronoUnit.YEARS.between(value,LocalDate.now()); //ham lay tuoi cua user so voi thoi gian hien tai

        return years >= min;
    }
}
