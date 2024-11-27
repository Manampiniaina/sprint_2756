package com.sprint.annotations.input;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Date {
    String  error="Value must be Date with Format : yy-MM-dd";
    String  success = "Date valid ";
    public String error() default error;
    public String success() default success;
}
