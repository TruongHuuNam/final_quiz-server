package com.huunam.identity_service.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD }) //annotation se duoc apply o dau
@Retention(RUNTIME) //se duoc xu ly luc nao
@Constraint(validatedBy = { DobValidator.class }) //class se chiu trach nhiem cho viec validate cho annotation
public @interface DobConstrant {

    String message() default "{Invalid date of birth}";

    int min();

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
