package com.maiphong.insightcommerce.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.ZonedDateTime;
import java.lang.reflect.Method;

public class EndDateAfterStartDateValidator implements ConstraintValidator<EndDateAfterStartDate, Object> {

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        try {
            Method getStartDate = obj.getClass().getMethod("getStartDate");
            Method getEndDate = obj.getClass().getMethod("getEndDate");

            ZonedDateTime startDate = (ZonedDateTime) getStartDate.invoke(obj);
            ZonedDateTime endDate = (ZonedDateTime) getEndDate.invoke(obj);

            if (startDate == null || endDate == null) {
                return true; // Let other constraints handle null values
            }
            return endDate.isAfter(startDate);
        } catch (Exception e) {
            return false; // If reflection fails, validation fails
        }
    }
}
