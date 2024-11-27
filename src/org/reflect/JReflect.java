package org.reflect;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.*;
import com.sprint.utils.StringUtil;
public class JReflect {
    private String packagePath;
    private String className;
    public JReflect(String packagePath, String className) {
        this.packagePath = packagePath;
        this.className = className;
    }
    public String getClassName() {
        return className;
    }

    public void setClassName(String classNam) {
        this.className = classNam;
    }

    public String getPackagePath() {
        return packagePath;
    }

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }

    public Class<?> getType() throws  ClassNotFoundException{
        return Class.forName(this.getClassName());
    }

    public Set<Class<?>> getTypes() {
        Set<Class<?>> classes = new HashSet<>();

        try {
            String packageName= (this.getPackagePath().replace("." , "/"));
            URL resource = Thread.currentThread().getContextClassLoader().getResource(packagePath);
            if (resource == null) {
                throw new IllegalArgumentException("Le package " + packageName + " est introuvable.");
            }

            File directory = new File(resource.getFile());
            if (!directory.exists()) {
                throw new IllegalArgumentException("Le répertoire " + directory + " est introuvable.");
            }
            for (String fileName : Objects.requireNonNull(directory.list())) {
                if (fileName.endsWith(".class")) {
                    String className = packageName + '.' + fileName.substring(0, fileName.length() - 6);
                    this.setClassName(className);
                    Class<?> clazz = this.getType();
                    classes.add(clazz);
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return classes;
    }

    public Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation){
        try {
            Set<Class<?>> classes_annotated = new HashSet<>();
            Set<Class<?>> classes = getTypes();
            for (Class<?> clazz : classes) {
                if (clazz.isAnnotationPresent(annotation)) {
                    classes_annotated.add(clazz);
                }
            }
            return classes_annotated;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public Method[] getDeclaredMethods(){
        try{
            return this.getType().getDeclaredMethods();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    public static String getMethodNameWithArgs(Method method) {
    	String methodName = method.getName();
    	Parameter[] parameters= method.getParameters();
    	Parameter parameter=null;
    	for (int i = 0; i < parameters.length; i++) {
			 parameter=parameters[i];
			 if(i==0) {
				 methodName+="("+parameter.getType().getName()+",";				 
			 }
			 else if(i==parameters.length-1) {
				 methodName+=parameter.getType().getName()+")";		
			 }
			 else {
				 methodName+=parameter.getType().getName()+",";	
			 }
		}
    	return methodName;
    }
    public Method[] getMethodsAnnotatedWith(Class<? extends Annotation> annotation){
        List<Method> methods_annotated = new ArrayList<>();
        Method[] methods = this.getDeclaredMethods();
        for (Method method : methods) {
            if(method.isAnnotationPresent(annotation)){
                methods_annotated.add(method);
            }
        }
        return methods_annotated.toArray(new Method[0]);
    }
    public  Parameter[] getParameterAnnotatedWith(Method method , Class<? extends Annotation> annotation) {
    	List<Parameter> parametersAnnotated=new ArrayList<>();
    	Parameter[] parameters = method.getParameters();
    	for (Parameter parameter : parameters) {
			if(parameter.isAnnotationPresent(annotation)) {
				parametersAnnotated.add(parameter);
			}
		}
    	return parametersAnnotated.toArray(new Parameter[0]);
    }
    public static Method isExistMethod(String methodName, Class<?>clazz) {
    	Method[] methods=clazz.getDeclaredMethods();
    	for (Method method : methods) {
			if(method.getName().equals(methodName)) {
				return method;
			}
		}
    	return null;
    }
    public static Method isExistSetter(Class<?>clazz, String fieldName) throws NoSuchMethodException {
    	Method ishere=isExistMethod("set"+StringUtil.firstToUpperCase(fieldName), clazz);
    	if(ishere!=null) {
    		return ishere;
    	}
    	throw new NoSuchMethodException("VOUS DEVEZ AVOIR UN SETTER POUR LE CHAMP "+fieldName  +" DANS LA CLASSE "+clazz.getName());
    }
}
