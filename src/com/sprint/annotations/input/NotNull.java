package com.sprint.annotations.input;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NotNull {
    String  error="Value can not be NULL and enter the correct value";
    String  success = "Value Not NULL valid ";
    public String error() default error;
    public String success() default success;
}
