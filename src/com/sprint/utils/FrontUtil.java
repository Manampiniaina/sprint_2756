package com.sprint.utils;

import com.sprint.annotations.AnnotationController;
import com.sprint.annotations.Get;
import com.sprint.objects.Mapping;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;

public class FrontUtil {
    public static Class<?>[] getListControllers(String packagePath ){
        Reflections reflect = new Reflections(packagePath);
        Set<Class<?>> classes= reflect.getTypesAnnotatedWith(AnnotationController.class);
        return classes.toArray(new Class<?>[0]);
    }

   
    public static HashMap<String , Mapping> getAllMapping(Class<?>[] controllers){
        HashMap<String , Mapping> mapping = new HashMap<>();
        for (int i = 0; i < controllers.length; i++) {
            AnnotationController annotationController = controllers[i].getAnnotation(AnnotationController.class);
            Reflections reflect = new Reflections(controllers[i].getName() , new MethodAnnotationsScanner());
            Method[] methods = reflect.getMethodsAnnotatedWith(Get.class).toArray(new Method[0]);
            for (Method method : methods) {
                Get get = method.getAnnotation(Get.class);
                String url = annotationController.value() + "/" + get.value();
                Mapping map = new Mapping(controllers[i].getName(), method.getName());
                mapping.put(url, map);
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
    public static String getMetaUrl(String url){
        String[] splits=url.split("/");
        if(splits.length>2){
            return  splits[2]+"/"+splits[3];
        } else if (splits.length==2) {
            return "/";
        }
        return url;
    }

}
