package com.drewsir.feather.server.annotation;


import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FeatherBean {

    String value() default "" ;
}
