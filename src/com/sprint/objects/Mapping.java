package com.sprint.objects;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.sprint.annotations.controller.RestAPI;
import com.sprint.exception.ConvertException;
import com.sprint.exception.SprintException;
import com.sprint.framework.MySession;
import com.sprint.helper.MappingHelper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;


public class Mapping {
    private String controllerName;
    private List<VerbAction> verbactions;

    public Mapping(){}
    public Mapping(String controllerName, String methodName , String verb) {
        this.setControllerName(controllerName);
        this.addVerbAction(verb, methodName);
    }
    public String getControllerName() {
        return controllerName;
    }
    
    public List<VerbAction> getVerbactions() {
    	if(this.verbactions==null) {
    		List<VerbAction> verbactions1=new ArrayList<VerbAction>();
    		this.setVerbactions(verbactions1);
    		return verbactions1;
    	}
		return verbactions;
	}
	public void setVerbactions(List<VerbAction> verbactions) {
		this.verbactions = verbactions;
	}
	public void setControllerName(String controllerName) {
        this.controllerName = controllerName;
    }
	public boolean ifExistVerb(String verb) {
		for (VerbAction verbAction : this.getVerbactions()) {
			if(verbAction.getVerb().equals(verb)) {
				return true;
			}
		}
		return false;
	}
    public Class<?> getClazz() throws ClassNotFoundException{
        return Class.forName(this.getControllerName());
    }
    public void addVerbAction(String verb , String methodname) {
    	VerbAction verb_action=new VerbAction(verb , methodname);
    	List<VerbAction> verb_actions = this.getVerbactions();
    	verb_actions.add(verb_action);
    	this.setVerbactions(verb_actions);
    }
    public String getMethodnameWithVerb(String verb) throws NoSuchMethodException{
    	List<VerbAction> verbactions= this.getVerbactions();
    	for (VerbAction verbAction : verbactions) {
			if(verbAction.getVerb().equals(verb)) {
				return verbAction.getMethodname();
			}
		}
    	throw new NoSuchMethodException("ERROR 500 : Il n'y a pas de méthode avec le verbe : " + verb + " dans cette URL dans le contrôleur : " + this.getControllerName());
    }
    public Method getMethod(String verb) throws NoSuchMethodException , ClassNotFoundException{
        Class<?> controller= this.getClazz();
        Method [] method= controller.getDeclaredMethods();
        for (int i = 0; i < method.length; i++) {
        	if(method[i].getName().equals(this.getMethodnameWithVerb(verb) )) {
        		return method[i];
        	}
		}
		return null;
    }
   
	public Object executeWithoutRestAPI(HttpServletRequest req , Object obj ,String verb ) throws 
		IllegalAccessException, IllegalArgumentException,
		InvocationTargetException, NoSuchMethodException, SecurityException, 
		ClassNotFoundException, InstantiationException, SprintException, ConvertException,
		ParseException, IOException, ServletException  
	{
		Field[] fields= obj.getClass().getDeclaredFields();
        for (Field field : fields) {
			if(field.getType()==MySession.class) {
				MySession session=new MySession(req.getSession());
				obj.getClass().getMethod("setSession", MySession.class).invoke(obj, session);
			}
		}
        Method method = this.getMethod(verb);
        if(method.getParameterCount()<=0) {
        	return method.invoke(obj);
        }
        Parameter[] parameters = method.getParameters();
        Object[] objs =MappingHelper.getParametersObject(req, parameters);
        Object ret =method.invoke(obj, objs);
        return ret;
	}
	public Object executeWithRestAPI(HttpServletRequest req ,  Object obj , String verb) throws 
		IllegalAccessException, IllegalArgumentException, 
		InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException,
		InstantiationException, SprintException, ConvertException, ParseException, IOException, ServletException 
	{
		Object obj1=executeWithoutRestAPI(req , obj ,verb);
        if(obj1.getClass()==ModelView.class) {
        	ModelView mv=(ModelView)obj1;
        	mv.toJsonData();
        	obj1= mv;
        }
        else {
        	obj1= new Gson().toJson(obj1);
        }
		return obj1;
	}
	
	public Object excecute(HttpServletRequest req , String verb ) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, 
			InvocationTargetException, NoSuchMethodException, SecurityException, SprintException,
			ConvertException, ParseException, IOException, ServletException
	{
		Class<?> clazz=this.getClazz();
		Object obj=clazz.getDeclaredConstructor().newInstance();
        if(clazz.isAnnotationPresent(RestAPI.class)) {
        	return executeWithRestAPI(req, obj , verb);
        }
        return executeWithoutRestAPI(req, obj , verb);
    }
	
	
}
