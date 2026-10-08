package com.example.val_demo;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ProductIdValidator.class)

public @interface ValidId {
    String message() default "Invalid ProductID";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
