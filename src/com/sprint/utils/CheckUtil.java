package com.sprint.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.sprint.annotations.input.*;
import com.sprint.annotations.input.Number;
import com.sprint.exception.InputException;
import com.sprint.framework.MySession;

import jakarta.servlet.http.Part;

public class CheckUtil {
	

    public static boolean isEntity(Class<?> clazz) {
        URL classUrl = clazz.getResource('/' + clazz.getName().replace('.', '/') + ".class");
        if (classUrl == null) {
            return false; 
        }

        String classPath;
        try {
            classPath = URLDecoder.decode(classUrl.getPath(), "UTF-8");
        } catch (Exception e) {
            return false; 
        }
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

	// Vérifie si la chaîne est une adresse e-mail valide
	public static boolean isEmail(String value) {
		if (value == null || value.isEmpty()) {
			return false;
		}
		return value.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
	}

	// Vérifie si la chaîne est une date valide au format "yyyy-MM-dd"
	public static boolean isDate(String value) {
		if (value == null || value.isEmpty()) {
			return false;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sdf.setLenient(false);
			sdf.parse(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// Vérifie si la chaîne est un datetime valide au format "yyyy-MM-dd HH:mm:ss"
	public static boolean isDatetime(String value) {
		if (value == null || value.isEmpty()) {
			return false;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sdf.setLenient(false);
			sdf.parse(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// Vérifie si la chaîne n'est ni null ni vide
	public static boolean isNotNull(String value) {
		return value != null && !value.trim().isEmpty();
	}

	// Vérifie si la chaîne est un nombre valide
	public static boolean isNumber(String value) {
		if (value == null || value.isEmpty()) {
			return false;
		}
		try {
			Double.parseDouble(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	// Vérifie si la chaîne contient uniquement des lettres et des espaces
	public static boolean isText(String value) {
		if(!isEmail(value) && !isDate(value) && !isDatetime(value) && !isNumber(value)&& !isTime(value)  ){
			return true;
		}
		return false;
	}

	// Vérifie si la chaîne est une heure valide au format "HH:mm:ss"
	public static boolean isTime(String value) {
		if (value == null || value.isEmpty()) {
			return false;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			sdf.setLenient(false);
			sdf.parse(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public static boolean checkFieldValue(Field field , String value ){
		String error="";
		if(field.isAnnotationPresent(Date.class)){
			if (!isDate(value)){
				error  = field.getAnnotation(Date.class).error();
				throw new InputException(error);
			}
		}
		if(field.isAnnotationPresent(Datetime.class)){
			if (!isDatetime(value)){
				error  = field.getAnnotation(Datetime.class).error();
				throw new InputException(error);
			}
		}
		if(field.isAnnotationPresent(Email.class)){
			if (!isEmail(value)){
				error  = field.getAnnotation(Email.class).error();
				throw new InputException(error);
			}
		}
		if(field.isAnnotationPresent(NotNull.class)){
			if (!isNotNull(value)){
				error  = field.getAnnotation(NotNull.class).error();
				throw new InputException(error);
			}
		}
		if(field.isAnnotationPresent(Number.class)){
			if (!isNumber(value)){
				error  = field.getAnnotation(Number.class).error();
				throw new InputException(error);
			}
		}
		if(field.isAnnotationPresent(Text.class)){
			if (!isText(value)){
				error  = field.getAnnotation(Text.class).error();
				throw new InputException(error);
			}
		}
		if(field.isAnnotationPresent(Time.class)){
			if (!isTime(value)){
				error  = field.getAnnotation(Time.class).error();
				throw new InputException(error);
			}
		}
		return true;
	}
}
