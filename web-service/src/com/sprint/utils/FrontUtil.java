package com.sprint.utils;

import com.sprint.annotations.controller.AnnotationController;
import com.sprint.annotations.parameter.FormName;
import com.sprint.annotations.method.Get;
import com.sprint.annotations.method.Post;
import com.sprint.annotations.method.Url;

import com.sprint.objects.Mapping;

import jakarta.servlet.ServletException;

import org.reflect.JReflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class FrontUtil {
	public static String generateErrorPage(String errorMessage) {
	    StringBuilder html = new StringBuilder();
	    html.append("<html><head><title>Erreur</title>");
	    html.append("<style>");
	    html.append("body { display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }");
	    html.append(".exception { font-size: 24px; color: red; text-align: center; }");
	    html.append("</style></head><body>");
	    html.append("<div class='exception'>" + errorMessage + "</div>");
	    html.append("</body></html>");
	    return html.toString();
	}

    public static Class<?>[] getListControllers(String packagePath ){
        JReflect reflect = new JReflect(packagePath,null);
        Set<Class<?>> classes= reflect.getTypesAnnotatedWith(AnnotationController.class);
        return classes.toArray(new Class<?>[0]);
    }
    public static String getVerbName(Method method) {
    	if(method.isAnnotationPresent(Post.class)) {
    		return "Post";
    	}
    	if( method.isAnnotationPresent(Get.class)) {
    		return "Get";
    	}
    	if(!method.isAnnotationPresent(Post.class) && !method.isAnnotationPresent(Get.class)) {
    		return "Get";
    	}
    	return "Get";
    }
    public static boolean isRepeat (HashMap<String, String> hashurl ,List<HashMap<String, String> > urls_check  ) {
    	 String verb=hashurl.get("verb");
    	 String url=hashurl.get("url");
    	 for (HashMap<String, String> hashMap : urls_check) {
    		 String url_check = hashMap.get("url");
    		 String verb_check = hashMap.get("verb");
    		 if(url_check.equals(url) && verb_check.equals(verb) ) {
    			 return true;
    		 }
		}
    	return false;
    }

    public static HashMap<String , Mapping> getAllMapping(Class<?>[] controllers) throws ServletException{
        HashMap<String , Mapping> mapping = new HashMap<>();
        List<HashMap<String, String> > urls_check = new ArrayList<>();
        for (Class<?> controller : controllers) {
            JReflect reflect = new JReflect(null,controller.getName());
            Method[] methods = reflect.getMethodsAnnotatedWith(Url.class);
            for (Method method : methods) {
                Url get = method.getAnnotation(Url.class);
                String url = get.value();
                String verb = getVerbName(method);
                String methodname= method.getName();
                String controllername=controller.getName();
                HashMap<String, String> hashurl =new HashMap<String, String>();
                hashurl.put("verb", verb);
                hashurl.put("url", url);
                if(!isRepeat(hashurl, urls_check)){
                    urls_check.add(hashurl);
                    if(mapping.containsKey(url)){
                    	Mapping map = mapping.get(url);
                    	map.addVerbAction(verb, methodname);
                    	mapping.replace(url, map);
                    }
                    else {
                    	Mapping map = new Mapping(controllername,methodname, verb);
                    	mapping.put(url, map);                    	
                    }
                    
                }
                else{
                    throw new ServletException("ERROR 2: there are a duplicate method mapping :"
                            +url +" verb :" +verb+ " in controller: "+controller.getName()
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

    public static String getMetaUrl(String url){
        String[] splits=url.split("/" , 3);
        if(splits.length>2){
            return  splits[2];
        } else if (splits.length==2) {
            return "/";
        }
        return url;
    }

}

