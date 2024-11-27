package com.sprint.annotations.input;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Email {
    String  error="Value must be an Email For example : test123@gmail.com";
    String  success = "Email valid";
    public String error() default error;
    public String success() default success;
}
