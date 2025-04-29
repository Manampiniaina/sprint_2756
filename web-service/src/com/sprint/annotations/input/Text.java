package com.sprint.annotations.input;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Text {
    String error = "The value of input must be a text only a text !";
    String success = "Value input success";
    public String error() default error;
}
