package com.maiphong.insightcommerce.validators;

import java.lang.reflect.Method;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateRangeValidator implements ConstraintValidator<DateRange, Object> {

    private String startField;
    private String endField;

    @Override
    public void initialize(DateRange constraintAnnotation) {
        this.startField = constraintAnnotation.startField();
        this.endField = constraintAnnotation.endField();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        try {
            Method startMethod = obj.getClass().getMethod("get"
                    + Character.toUpperCase(startField.charAt(0)) + startField.substring(1));
            Method endMethod = obj.getClass().getMethod("get"
                    + Character.toUpperCase(endField.charAt(0)) + endField.substring(1));
            Temporal start = (Temporal) startMethod.invoke(obj);
            Temporal end = (Temporal) endMethod.invoke(obj);
            if (start == null || end == null) {
                return true;
            }
            return start.until(end, ChronoUnit.NANOS) > 0;
        } catch (Exception e) {
            return true;
        }
    }

}
