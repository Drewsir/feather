package com.drewsir.feather.server.annotation;


import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FeatherRoute {

    String value() default "" ;
}
