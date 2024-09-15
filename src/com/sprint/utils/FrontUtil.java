package com.sprint.utils;

import com.sprint.annotations.AnnotationController;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FrontUtil {
    public static Class<?>[] getListControllers(String packagePath ){
        Reflections reflect = new Reflections(packagePath);
        Set<Class<?>> classes= reflect.getTypesAnnotatedWith(AnnotationController.class);
        return classes.toArray(new Class<?>[0]);
    }

}
