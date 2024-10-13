package com.sprint.utils;

public class StringUtil {
    public static String firstToUpperCase(String s){
        Character first = s.charAt(0);
        first=Character.toUpperCase(first); 
        char[] characters = s.toCharArray();
        characters[0]=first;
        return new String(characters);
    }
  
}
