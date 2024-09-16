package com.sprint.objects;

public class Mapping {
    private String controllerName;
    private String methodName;

    public Mapping(){

    }
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



}
