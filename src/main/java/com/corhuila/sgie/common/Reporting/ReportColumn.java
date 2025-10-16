package com.corhuila.sgie.common.Reporting;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface ReportColumn {
    String header();
    int order() default 0;
    String format() default "";
    int width() default -1;
    boolean autosize() default false;
    boolean wrap() default false;
    boolean text() default false;
}
