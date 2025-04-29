package com.sprint.annotations.input;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Time {
    String  error="Value must be Time Time with Format:HH:mm:ss";
    String  success = "Time valid ";
    public String error() default error;
    public String success() default success;
}
