package com.sprint.helper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.reflect.JReflect;

import com.sprint.annotations.Entity;
import com.sprint.annotations.FormName;
import com.sprint.annotations.RequestParam;
import com.sprint.utils.ConvertUtil;

import jakarta.servlet.http.HttpServletRequest;

public class MappingHelper {

	public static String getParameterName(Parameter parameter) {
		if(parameter.isAnnotationPresent(RequestParam.class)) {
			return parameter.getAnnotation(RequestParam.class).value();
		}
		if(parameter.isAnnotationPresent(Entity.class)) {
			return parameter.getAnnotation(Entity.class).value();
		}
		return parameter.getName();
		
	}
	public static Object getRequestParamValue(HttpServletRequest req ,String[] paramServletTab ,Parameter parameter) throws Exception {
    	String parameterName ="";
		parameterName=MappingHelper.getParameterName(parameter);
		for (String param : paramServletTab) {
			Object paramValue = null;
			if(param.equals(parameterName)) {
				paramValue=req.getParameter(param);
				Class<?> clazz=parameter.getType();
				 Object obj = clazz.getDeclaredConstructor().newInstance();
				paramValue=ConvertUtil.toObject((String) paramValue ,obj );
				if(paramValue!=null) {
					return paramValue;
				}else {
					throw new Exception("ERROR 7: VALUE OF PARAM :" +parameterName+"IS NULL" );						
				}
			}
		}
		return null;
    }
	public static String getFormName(Field field) {
		if(field.isAnnotationPresent(FormName.class)) {
			return field.getAnnotation(FormName.class).value();
		}
		return field.getName();
	}
	public static String getEntityName(Parameter clazz) {
		if(clazz.isAnnotationPresent(Entity.class)) {
			return clazz.getAnnotation(Entity.class).value();
		}
		return clazz.getName();
	}
	public static String getParamNameForm(Parameter clazz ,Field field ) {
		String entityName=getEntityName(clazz);
		String formName=getFormName(field);
		return entityName+":"+formName;
	}
    public static Object getEntityValue(HttpServletRequest req , Parameter parameter , String[] paramServletTab ) throws Exception {
    	Class<?> clazz=parameter.getType();
    	Object obj=clazz.getDeclaredConstructor().newInstance();
    	Field[] fields=obj.getClass().getDeclaredFields();
    	for (Field field : fields) {
    		String paramName=getParamNameForm(parameter , field);
    		for (String requestParam : paramServletTab) {
    			if(paramName.equals(requestParam)) {
    			   	Method setter = JReflect.isExistSetter(clazz, field.getName());
					if(setter!=null){
						String valueRequest=req.getParameter(paramName);
						Object setValue=ConvertUtil.toObjectWithClass(valueRequest, field.getType());
						setter.invoke(obj, setValue);
					}
				}
			}
		}
    	return obj;
    }
    public static Object[] getParametersObject(HttpServletRequest req ,  Parameter[] parameters ) throws Exception {
    	List<Object> objs=new ArrayList<>();
    	Enumeration<String> parameterServlet=req.getParameterNames();
    	String[] paramServletTab=ConvertUtil.convertEnumerationToTab(parameterServlet);
    	for (Parameter parameter : parameters) {
    		Object paramValue=null;
    		if(parameter.isAnnotationPresent(RequestParam.class)) {
    			paramValue=getRequestParamValue(req, paramServletTab,parameter);    			
    		}
    		if(parameter.isAnnotationPresent(Entity.class)) {
    			paramValue=getEntityValue(req, parameter, paramServletTab);
    		}
    		if(!parameter.isAnnotationPresent(RequestParam.class) && !parameter.isAnnotationPresent(Entity.class)) {
    			throw new Exception("VOUS DEVEZ METTRE UNE ANNOTATION @RequestParam OU @Entity SUR LE PARAMETER "+parameter.getName());
    		}
			objs.add(paramValue);
		}
    	return objs.toArray(new Object[0]);
    }
  
}
