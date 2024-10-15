package com.sprint.objects;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import com.google.gson.Gson;
import com.sprint.annotations.RestAPI;
import com.sprint.framework.MySession;
import com.sprint.helper.MappingHelper;

import jakarta.servlet.http.HttpServletRequest;


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
   
	public Object executeWithoutRestAPI(HttpServletRequest req , Object obj ) throws Exception{
		Field[] fields= obj.getClass().getDeclaredFields();
        for (Field field : fields) {
			if(field.getType()==MySession.class) {
				MySession session=new MySession(req.getSession());
				obj.getClass().getMethod("setSession", MySession.class).invoke(obj, session);
			}
		}
        Method method = this.getMethod();
        if(method.getParameterCount()<=0) {
        	return method.invoke(obj);
        }
        Parameter[] parameters = method.getParameters();
        Object[] objs =MappingHelper.getParametersObject(req, parameters);
        Object ret =method.invoke(obj, objs);
        return ret;
	}
	public Object executeWithRestAPI(HttpServletRequest req ,  Object obj) throws Exception{
		Object obj1=executeWithoutRestAPI(req , obj);
        if(obj1.getClass()==ModelView.class) {
        	ModelView mv=(ModelView)obj1;
        	mv.toJsonData();
        	obj1= mv;
        }
        else {
        	obj1= new Gson().toJson(obj);
        }
		return obj1;
	}
	public Object excecute(HttpServletRequest req ) throws Exception {
		Class<?> clazz=this.getClazz();
		Object obj=clazz.getDeclaredConstructor().newInstance();
        if(clazz.isAnnotationPresent(RestAPI.class)) {
        	return executeWithRestAPI(req, obj);
        }
        return executeWithoutRestAPI(req, obj);
    }
}
