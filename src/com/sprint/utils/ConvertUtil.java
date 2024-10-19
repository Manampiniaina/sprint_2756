package com.sprint.utils;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.sprint.exception.ConvertException;

public class ConvertUtil {
	public static String[] convertEnumerationToTab(Enumeration<String> enumerations) {
	    List<String> list = new ArrayList<>();                         
	    while (enumerations.hasMoreElements()) {
	        String param = enumerations.nextElement();
	        list.add(param);
	    }
	    return list.toArray(new String[0]);
	}

	public static Object toObject(String parameter , Object obj) throws ConvertException , ParseException{
		Class<?> clazz = obj.getClass();
        if(clazz==Double.class){
            return Double.parseDouble(parameter);
        }
        if(clazz==double.class){
            return Double.parseDouble(parameter);

        }
        if(clazz==Integer.class){
            return Integer.parseInt(parameter);

        }
        if(clazz==int.class){
            return Integer.parseInt(parameter);

        }
        if(clazz==Date.class){
            return DateUtil.toDate(parameter);            
        }
        if(clazz==Timestamp.class){
            return TimestampUtil.toTimestampDatetime(parameter);
        }
        if(clazz==Time.class){
            return TimeUtil.toTime(parameter);
        }
        if(clazz==String.class){
            return parameter;
        }
        
        throw new ConvertException("CAN'T CONVERT TO "+clazz.getName());
    }
	
	public static Object toObjectWithClass(String parameter , Class<?> clazz) throws ConvertException , ParseException{
        if(clazz==Double.class){
            return Double.parseDouble(parameter);
        }
        if(clazz==double.class){
            return Double.parseDouble(parameter);

        }
        if(clazz==Integer.class){
            return Integer.parseInt(parameter);

        }
        if(clazz==int.class){
            return Integer.parseInt(parameter);

        }
        if(clazz==Date.class){
            return DateUtil.toDate(parameter);            
        }
        if(clazz==Timestamp.class){
            return TimestampUtil.toTimestampDatetime(parameter);
        }
        if(clazz==Time.class){
            return TimeUtil.toTime(parameter);
        }
        if(clazz==String.class){
            return parameter;
        }
   
        throw new ConvertException("CAN'T CONVERT TO "+clazz.getName());
    }


}

