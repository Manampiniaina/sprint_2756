package com.sprint.utils;

import com.sprint.annotations.AnnotationController;
import com.sprint.annotations.FormName;
import com.sprint.annotations.RequestParam;
import com.sprint.annotations.Url;
import com.sprint.objects.Mapping;
import org.reflect.JReflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class FrontUtil {
	
	public static boolean isPresentParameter(String parameterName , Mapping mapping) throws Exception {
    	Method method = mapping.getMethod();
		Parameter[] parameters = method.getParameters(); 
    	for (Parameter parameter : parameters) {
    		if(parameter.isAnnotationPresent(RequestParam.class)){
    			RequestParam annotation = parameter.getAnnotation(RequestParam.class);
    			String annotationValue=annotation.value();
    			if(annotationValue.equals(parameterName)){
    				return true;
    			}
    		}
    		else {
    			throw new Exception("ERROR 5: ALL PARAMETER MUST HAVE @RequestParam IN PARAM : "+parameter.getName() + " METHOD: "+method.getName()
    	     	+" IN CONTROLLER : " +mapping.getControllerName() );
    		}
		}
		return false;
	}
	
	public static int countEnumeration(Enumeration<String> enumerations) {
		int count=0;
		while(enumerations.hasMoreElements()) {
			enumerations.nextElement();
			count++;
		}
		return count;
	}
    public static Class<?>[] getListControllers(String packagePath ){
        JReflect reflect = new JReflect(packagePath,null);
        Set<Class<?>> classes= reflect.getTypesAnnotatedWith(AnnotationController.class);
        return classes.toArray(new Class<?>[0]);
    }
    
    public static HashMap<String , Mapping> getAllMapping(Class<?>[] controllers) throws Exception{
        HashMap<String , Mapping> mapping = new HashMap<>();
        List<String> urls_check = new ArrayList<>();
        for (Class<?> controller : controllers) {
            JReflect reflect = new JReflect(null,controller.getName());
            Method[] methods = reflect.getMethodsAnnotatedWith(Url.class);
            for (Method method : methods) {
            	
                Url get = method.getAnnotation(Url.class);
                String url = get.value();
                if(urls_check.isEmpty()||!urls_check.contains(url)){
                    urls_check.add(url);
                    Mapping map = new Mapping(controller.getName(), method.getName());
                    mapping.put(url, map);
                }
                else{
                    throw new Exception("ERROR 2: there are a duplicate method mapping :"
                            +url+" in controller: "+controller.getName()
                            +" in the method :"+method.getName()
                    );
                }

            }
        }
        return mapping;
    }
    public static Mapping getMapping(String url , HashMap<String , Mapping> mapping){
        if(mapping.containsKey(url)  ){
            return mapping.get(url);
        } else if (mapping.containsKey("/"+url)  ) {
            return mapping.get("/"+url);
        } else if (mapping.containsKey(url+"/")) {
            return mapping.get(url+"/");
        } else if (mapping.containsKey("/"+url+"/")) {
            return mapping.get("/" + url + "/");
        }
        return null;
    }

    public static int countChar(char counting , String str){
        int count=0;
        for(int i =0;i<str.length();i++){
            if (str.charAt(i)==counting){
                count++;
            }
        }
        return count;
    }
    public static String getAddDispactcher(String url){
        int count=FrontUtil.countChar('/', url) ;
        String addDispat="";
        for (int i = 0; i < count; i++) {
            addDispat+="../";
        }
        return (addDispat);
    }

    public static String getMetaUrl(String url){
        String[] splits=url.split("/" , 3);
        if(splits.length>2){
            return  splits[2];
        } else if (splits.length==2) {
            return "/";
        }
        return url;
    }
    public static String getFormName(Field field) {
    	if(field.isAnnotationPresent(FormName.class)) {
    		return field.getAnnotation(FormName.class).value(); 		
    	}
    	return field.getName();
    }
}

