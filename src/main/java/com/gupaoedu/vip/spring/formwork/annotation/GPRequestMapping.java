package com.gupaoedu.vip.spring.formwork.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface GPRequestMapping {

    String name() default "";

    String value() default "";

    String[] path() default {};
}
