package com.nchen.morphine.annotations;

import com.nchen.morphine.builders.CascadeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ManyToOne {
    String joinColumn() default "";
    CascadeType[] cascade() default {CascadeType.CASCADE_ALL};
}
