package com.sprint.utils;

import java.lang.reflect.Parameter;
import java.net.URL;
import java.net.URLDecoder;

import com.sprint.framework.MySession;

import jakarta.servlet.http.Part;

public class CheckUtil {
	

    public static boolean isEntity(Class<?> clazz) {
    	System.out.println("clazz isEntity");
        URL classUrl = clazz.getResource('/' + clazz.getName().replace('.', '/') + ".class");
        if (classUrl == null) {
        	System.out.println("class url null");
            return false; 
        }

        String classPath;
        try {
            classPath = URLDecoder.decode(classUrl.getPath(), "UTF-8");
            System.out.println("clazzPath");
        } catch (Exception e) {
        	System.out.println("exeption isEntity false");
            return false; 
        }
        System.out.println("clazz isEntity final check");
        return classPath.contains("WEB-INF/classes") || classPath.contains("WEB-INF/lib");
    }
	
	//	0 c'est un request parameter - 1 c'est un entity - 2 c'est un session - 3 c'est un Part 
	public static int checkTypeParameter(Parameter parameter ) {
		Class<?> clazz =parameter.getType();
	 	if(isEntity(clazz)) {
	 		return 1;
	 	}
	 	if(clazz==MySession.class){
	 		return 2;
	 	}
	 	if(clazz==Part.class) {
	 		return 3;
	 	}
	 	return 0;
        
	}
}
