package com.nchen.morphine;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

@Target(value = {FIELD, METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    String name();
    boolean nullable();
    boolean updatable();
    boolean unique();
    int length();
}
