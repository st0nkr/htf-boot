package com.teto.domain.annotation;

import com.teto.domain.meta.Tag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Meta {
    int length() default -1;
    boolean id() default false;
    boolean notnull() default false;
    Tag tag() default Tag.Unknown;
    String keyWords() default "";
    boolean ignore() default false;
    int index() default 100;
    String[] selection() default {};
    boolean unique() default false;
    boolean display() default false;
}

