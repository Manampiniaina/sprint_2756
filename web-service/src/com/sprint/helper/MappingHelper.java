package com.sprint.helper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.reflect.JReflect;

import com.sprint.annotations.parameter.Entity;
import com.sprint.annotations.parameter.FormName;
import com.sprint.annotations.parameter.RequestFile;
import com.sprint.annotations.parameter.RequestParam;
import com.sprint.exception.ConvertException;
import com.sprint.exception.SprintException;
import com.sprint.framework.MySession;
import com.sprint.utils.CheckUtil;
import com.sprint.utils.ConvertUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

public class MappingHelper {
	public static String getParameterName(Parameter parameter) {
		if(parameter.isAnnotationPresent(RequestParam.class)) {
			return parameter.getAnnotation(RequestParam.class).value();
		}
		if(parameter.isAnnotationPresent(Entity.class)) {
			return parameter.getAnnotation(Entity.class).value();
		}
		if(parameter.isAnnotationPresent(RequestFile.class)) {
			return parameter.getAnnotation(RequestFile.class).value();
		}
		return parameter.getName();
		
	}
	public static Object getRequestFile(HttpServletRequest req ,Parameter parameter ) throws IOException, ServletException {
		String parameterName ="";
		parameterName=MappingHelper.getParameterName(parameter);
		Part part=req.getPart(parameterName);
		return part;
	}
	public static Object getRequestParamValue(HttpServletRequest req ,String[] paramServletTab ,Parameter parameter) throws ConvertException, ParseException  {
    	String parameterName ="";
		parameterName=MappingHelper.getParameterName(parameter);
		
		for (String param : paramServletTab) {
			Object paramValue = null;
			if(param.equals(parameterName)) {
				paramValue=req.getParameter(param);
				Class<?> clazz=parameter.getType();
				paramValue=ConvertUtil.toObjectWithClass((String) paramValue ,clazz );
				if(paramValue!=null) {
					return paramValue;
				}
			}
		}
		return null;
    }
//	0 c'est un request parameter - 1 c'est un entity - 2 c'est un session - 3 c'est un Part 
	public static Object getValue(HttpServletRequest req ,String[] paramServletTab  ,Parameter parameter) throws ConvertException, ParseException, NoSuchMethodException, 
		InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException, 
		IOException, ServletException  
	{
		int checktype=CheckUtil.checkTypeParameter(parameter);
		Object paramValue=null;
		if(checktype==0) {
			paramValue=getRequestParamValue(req, paramServletTab, parameter);
		}
		if(checktype==1) {
			paramValue=getEntityValue(req, parameter, paramServletTab);
		}
		if(checktype==2) {
			paramValue=new MySession(req.getSession());
		}
		if(checktype==3) {
			paramValue=getRequestFile(req, parameter) ;
		}
		return paramValue;
		
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
    public static Object getEntityValue(HttpServletRequest req , Parameter parameter , String[] paramServletTab ) throws NoSuchMethodException, ConvertException, ParseException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException  {
    	Class<?> clazz=parameter.getType();
    	Object obj=clazz.getDeclaredConstructor().newInstance();
    	Field[] fields=obj.getClass().getDeclaredFields();
    	for (Field field : fields) {
    		String paramName=getParamNameForm(parameter , field);
    		for (String requestParam : paramServletTab) {
    			if(paramName.equals(requestParam)) {
//    			   	Method setter = JReflect.isExistSetter(clazz, field.getName());
					String valueRequest=req.getParameter(paramName);
					if(CheckUtil.checkFieldValue(field , valueRequest)){
						field.setAccessible(true);
						field.set(obj, ConvertUtil.toObjectWithClass(valueRequest, field.getType()));
					}

//					if(setter!=null){
//						String valueRequest=req.getParameter(paramName);
//						Object setValue=ConvertUtil.toObjectWithClass(valueRequest, field.getType());
//						setter.invoke(obj, setValue);
//					}
//					else{
//						throw new ConvertException("Field with name "+ paramName+ " and in :"+clazz.getName() +"  must have a getter and setter");
//					}
				}
			}
		}
    	return obj;
    }
   
  
    public static Object[] getParametersObject(HttpServletRequest req ,  Parameter[] parameters ) throws SprintException, ConvertException,
    		ParseException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException,
    		InvocationTargetException, SecurityException, IOException, ServletException 
    {
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
    		if(parameter.isAnnotationPresent(RequestFile.class)) {
    			paramValue=getRequestFile(req, parameter);
    		}
    		if(!parameter.isAnnotationPresent(RequestParam.class) 
    				&& !parameter.isAnnotationPresent(Entity.class)
    				&&parameter.getType()!=MySession.class&&!parameter.isAnnotationPresent(RequestFile.class)  ) {
    			paramValue=getValue(req, paramServletTab,parameter);   
    		}
			objs.add(paramValue);
		}
    	return objs.toArray(new Object[0]);
    }
  
}
