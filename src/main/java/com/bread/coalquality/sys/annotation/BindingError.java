package com.bread.coalquality.sys.annotation;

import java.lang.annotation.*;


@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BindingError {
}
