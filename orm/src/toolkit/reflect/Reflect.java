package toolkit.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Reflect {
     public static Field[] getAnnotatedFields(Object obj,Class<? extends Annotation> class1) {
        List<Field> annotatedFields = new ArrayList<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(class1)) {
                annotatedFields.add(field);
            }
        }
        return annotatedFields.toArray(new Field[0]) ;
    }
    public static Field[] getFields(Object obj) {
        return obj.getClass().getDeclaredFields();
    }
  
    @SuppressWarnings("unchecked")
    public static <T> T executeWithParams(Object obj, String methodName, Object... params) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Class<?>[] paramTypes = new Class[params.length];
        for (int i = 0; i < params.length; i++) {
            paramTypes[i] = params[i].getClass();
        }
        Method method = obj.getClass().getMethod(methodName, paramTypes);
        return (T) method.invoke(obj, params);
    }

    @SuppressWarnings("unchecked")
    public static <T> T executeWithoutParams(Object obj, String methodName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = obj.getClass().getMethod(methodName);
        return (T) method.invoke(obj);
    }

    public static <T> T execute(Object obj, String methodName, Object... params) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if(params==null){
            return Reflect.executeWithoutParams(obj, methodName);
        }
        return Reflect.executeWithParams(obj, methodName, params);
    }




}
