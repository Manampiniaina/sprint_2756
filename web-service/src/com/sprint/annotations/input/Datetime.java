package com.sprint.annotations.input;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Datetime {
    String  error="Value must be Date Time with Format: yy-MM-dd HH:mm:ss";
    String  success = "DateTime valid ";
    public String error() default error;
    public String success() default success;
}
