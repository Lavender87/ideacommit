package com.gupaoedu.vip.spring.formwork.annotation;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GPRequestParam {

    String value() default "";

    String name() default "";

    boolean required() default true;

}
