/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 * 
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.simulation.inspection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * Class responsible for reading annotations.
 * 
 * @author Tim Neumann
 */
public class AnnotationReader {
    private AnnotationReader() {
        //hide constructor
    }
    
    /**
     * Get all methods with the given annotation in the given class and all parent classes.
     * 
     * @param cls
     *     The class to get the methods from
     * @param annotation
     *     The type of annotation all returned methods need to have.
     * @return A list of methods with the given annotation.
     */
    public static List<Method> getAllMethodsWithAnnotationRecursively(Class<?> cls, Class<? extends Annotation> annotation) {
        Method[] methods = cls.getDeclaredMethods();
        List<Method> ret = new ArrayList<>();
        
        for (Method m : methods) {
            if (m.isAnnotationPresent(annotation)) {
                ret.add(m);
            }
        }
        
        Class<?> superCls = cls.getSuperclass();
        if (superCls != null) {
            ret.addAll(getAllMethodsWithAnnotationRecursively(superCls, annotation));
        }
        return ret;
    }
    
    /**
     * Get all attributes with the given annotation in the given class and all parent classes.
     * 
     * @param cls
     *     The class to get the attributes from
     * @param annotation
     *     The type of annotation all returned attributes need to have.
     * @return A list of attributes with the given annotation.
     */
    public static List<Field> getAllAttributesWithAnnotationRecursively(Class<?> cls, Class<? extends Annotation> annotation) {
        Field[] fields = cls.getDeclaredFields();
        List<Field> ret = new ArrayList<>();
        
        for (Field f : fields) {
            if (f.isAnnotationPresent(annotation)) {
                ret.add(f);
            }
        }
        
        Class<?> superCls = cls.getSuperclass();
        if (superCls != null) {
            ret.addAll(getAllAttributesWithAnnotationRecursively(superCls, annotation));
        }
        return ret;
    }
}
