package com.sprint.objects;

import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class Mapping {
    private String controllerName;
    private String methodName;

    public Mapping(){}
    public Mapping(String controllerName, String methodName) {
        this.setControllerName(controllerName);
        this.setMethodName(methodName);
    }
    public String getControllerName() {
        return controllerName;
    }

    public void setControllerName(String controllerName) {
        this.controllerName = controllerName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    public boolean equals(Mapping map){
        return map.getControllerName().equals(this.getControllerName()) && map.getMethodName().equals(this.getMethodName());
    }
    public Class<?> getClazz() throws ClassNotFoundException{
        return Class.forName(this.getControllerName());
    }
    public Method getMethod() throws ClassNotFoundException , NoSuchMethodException{
        Class<?> controller= this.getClazz();
        return controller.getDeclaredMethod(this.getMethodName());
    }
    public Object excecute() throws
            ClassNotFoundException ,
            NoSuchMethodException ,
            InstantiationException ,
            IllegalAccessException ,
            InvocationTargetException {
        Object obj=this.getClazz().newInstance();
        Method method = this.getMethod();
        return method.invoke(obj  ) ;
    }
}
