package com.drewsir.feather.server.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)//注解的作用目标:接口、类、枚举、注解
@Retention(RetentionPolicy.RUNTIME)// 注解会在 class 字节码文件中存在，在运行时可以通过反射获取到
@Documented//说明该注解将被包含在 Javadoc 中
public @interface Interceptor {
    int order() default 0 ;
}
