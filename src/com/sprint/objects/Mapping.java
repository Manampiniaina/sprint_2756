package com.sprint.objects;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.sprint.annotations.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import com.sprint.utils.ConvertUtil;

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
    
    public Method getMethod() throws Exception{
        Class<?> controller= this.getClazz();
        Method [] method= controller.getDeclaredMethods();
        for (int i = 0; i < method.length; i++) {
        	if(method[i].getName().equals(this.getMethodName())) {
        		return method[i];
        	}
		}
        throw new Exception("ERROR 9 : Ther are no method with name :"+this.getControllerName());
    }
    private String getParameterName(Parameter parameter) {
    	if(parameter.isAnnotationPresent(RequestParam.class)) {
			return parameter.getAnnotation(RequestParam.class).value();
		}

    	return parameter.getName();
	
    }
	private Object getParameterValue(HttpServletRequest req ,String[] paramServletTab ,Parameter parameter,int parametersLength) throws Exception {
    	String parameterName ="";
		parameterName=this.getParameterName(parameter);
		if(paramServletTab.length>=parametersLength ) {
			for (String param : paramServletTab) {
				Object paramValue = null;
				if(param.equals(parameterName)) {
					paramValue=req.getParameter(param);
					paramValue=ConvertUtil.toObject((String) paramValue , parameter.getType());
					if(paramValue!=null) {
						return paramValue;
					}else {
						throw new Exception("ERROR 7: VALUE OF PARAM :" +parameterName+"IS NULL" );						
					}
				}
			}
		}
		else {
			throw new Exception("ERROR 8: INSUFFICIENT PARAMETERS IN THE FORM");	 			
		}
		return null;
    }
    
    private Object[] getParametersObject(HttpServletRequest req ,  Parameter[] parameters ) throws Exception {
    	List<Object> objs=new ArrayList<>();
    	Enumeration<String> parameterServlet=req.getParameterNames();
    	String[] paramServletTab=ConvertUtil.convertEnumerationToTab(parameterServlet);
    	for (Parameter parameter : parameters) {
			Object paramValue=this.getParameterValue(req, paramServletTab,parameter, parameters.length);
			objs.add(paramValue);
		}
    	return objs.toArray(new Object[0]);
    }
    @SuppressWarnings("deprecation")
	public Object excecute(HttpServletRequest req ) throws Exception {
        Object obj=this.getClazz().newInstance();
        Method method = this.getMethod();
        if(method.getParameterCount()<=0) {
        	return method.invoke(obj);
        }
        Parameter[] parameters = method.getParameters();
        Object[] objs =this.getParametersObject(req, parameters);
        Object ret =method.invoke(obj, objs);
        return ret;
    }
}
