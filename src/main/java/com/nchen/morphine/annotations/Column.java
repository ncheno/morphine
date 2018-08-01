package com.nchen.morphine.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

@Target(value = {FIELD, METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    String name() default "";
    boolean nullable() default true;
    boolean unique() default false;
    int length() default 0;
}
