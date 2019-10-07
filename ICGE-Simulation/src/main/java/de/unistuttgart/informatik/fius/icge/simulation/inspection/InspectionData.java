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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * Represents all data available for the inspection for a class
 * 
 * @author Tim Neumann
 */
public class InspectionData {
    private final Class<?> c;
    
    private final Map<String, AttributeInspectionPoint> inspectableAttributes;
    private final Map<String, Method>                   inspectableMethods;
    
    /**
     * Creates a new inspection data object for the given class
     * 
     * @param cls
     *     The class to create a inspection data object for.
     */
    public InspectionData(Class<?> cls) {
        this.c = cls;
        this.inspectableAttributes = new HashMap<>();
        this.inspectableMethods = new HashMap<>();
        this.initAttributes();
        this.initMethods();
    }
    
    /**
     * Get the value of the attribute with the given name from the given object.
     * 
     * @param obj
     *     The object to get the value from
     * @param name
     *     The name of the attribute to get the value from
     * @return The value
     */
    public Object getAttributeValue(Object obj, String name) {
        AttributeInspectionPoint p = this.inspectableAttributes.get(name);
        if (p != null) {
            try {
                return p.getValue(obj);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    /**
     * Set's the value of the attribute with the given name for the given object
     * 
     * @param obj
     *     The object to set the value in.
     * @param name
     *     The name of the attribute to set.
     * @param value
     *     The value to set.
     * @return Whether it worked.
     */
    public boolean setAttributeValue(Object obj, String name, Object value) {
        AttributeInspectionPoint p = this.inspectableAttributes.get(name);
        if (p != null) {
            try {
                p.setValue(obj, value);
                return true;
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    
    /**
     * Get the attribute names of the class for this inspection data.
     * 
     * @return A list of attribute names.
     */
    public List<String> getAttributeNames() {
        return Collections.unmodifiableList(new ArrayList<>(this.inspectableAttributes.keySet()));
    }
    
    /**
     * Get the type of the attribute with the given name.
     * 
     * @param attributeName
     *     The name of the attribute
     * @return The type of the attribute.
     */
    public Class<?> getAttributeType(String attributeName) {
        AttributeInspectionPoint p = this.inspectableAttributes.get(attributeName);
        if (p == null) return null;
        return p.getType();
    }
    
    /**
     * Check whether the attribute with the given name is read only.
     * 
     * @param attributeName
     *     The name of the attribute.
     * @return Whether the attribute is read only.
     */
    public boolean isAttributeReadOnly(String attributeName) {
        AttributeInspectionPoint p = this.inspectableAttributes.get(attributeName);
        if (p == null) return true;
        return p.isReadOnly();
    }
    
    /**
     * @return Whether this inspection data has any inspectable elements.
     */
    public boolean hasAnyInspectableElements() {
        return !(this.inspectableAttributes.isEmpty() && this.inspectableMethods.isEmpty());
    }
    
    /**
     * Get the mathod names of the class for this inspection data.
     * 
     * @return A list of method names.
     */
    public List<String> getMethodNames() {
        return Collections.unmodifiableList(new ArrayList<>(this.inspectableMethods.keySet()));
    }
    
    /**
     * Get the method detail for the method with the given name
     * 
     * @param methodName
     *     The name of the method to get the detail for
     * @return The method detail.
     */
    public Method getMethodByName(String methodName) {
        return this.inspectableMethods.get(methodName);
    }
    
    /**
     * Invoke the method with the given name in the given object, using the given arguments
     * 
     * @param obj
     *     The object to call the method on
     * @param methodName
     *     The name of the method to call
     * @param args
     *     The arguments to use
     * @return The return value.
     */
    public Object invokeMethod(Object obj, String methodName, Object... args) {
        Method m = this.inspectableMethods.get(methodName);
        if (m == null) throw new IllegalStateException("No such method!");
        try {
            return m.invoke(obj, args);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Invokation didn't work", e);
        }
    }
    
    private void initMethods() {
        List<Method> methods = AnnotationReader.getAllMethodsWithAnnotationRecursively(this.c, InspectionMethod.class);
        
        for (Method m : methods) {
            m.setAccessible(true);
            this.inspectableMethods.put(this.getDsiplayNameForInspectionMethod(m), m);
        }
    }
    
    private void initAttributes() {
        List<Field> fields = AnnotationReader.getAllAttributesWithAnnotationRecursively(this.c, InspectionAttribute.class);
        List<Method> methods = AnnotationReader.getAllMethodsWithAnnotationRecursively(this.c, InspectionAttribute.class);
        
        for (Field f : fields) {
            f.setAccessible(true);
            this.inspectableAttributes.put(this.getDisplayNameForField(f), new AttributeInspectionPoint(f));
        }
        
        Map<String, Method> getters = new HashMap<>();
        Map<String, Method> setters = new HashMap<>();
        
        for (int i = 0; i < methods.size(); i++) {
            Method m = methods.get(i);
            
            if (this.isGetter(m)) {
                getters.put(this.getDisplayNameForMethod(m, "get"), m);
            } else if (this.isSetter(m)) {
                setters.put(this.getDisplayNameForMethod(m, "set"), m);
            } else throw new InspectionPointException("Method is neither a getter nor a setter! : " + m.getName());
        }
        
        for (Entry<String, Method> entry : getters.entrySet()) {
            String name = entry.getKey();
            Method setter = setters.remove(name);
            Method getter = entry.getValue();
            boolean readOnly = getter.getAnnotation(InspectionAttribute.class).readOnly();
            if (readOnly || setter == null) {
                if (setter != null) throw new InspectionPointException("Getter specifies read only, but setter found! : " + name);
                this.validateReadOnlyGetter(getter);
                getter.setAccessible(true);
                this.inspectableAttributes.put(name, new AttributeInspectionPoint(getter));
                
            } else {
                this.validateMethodPair(getter, setter);
                getter.setAccessible(true);
                setter.setAccessible(true);
                this.inspectableAttributes.put(name, new AttributeInspectionPoint(getter, setter));
            }
            
        }
        
        if (setters.size() > 0) throw new InspectionPointException("No getter for setter! : " + setters.values().iterator().next());
        
    }
    
    private boolean isGetter(Method met) {
        return (met.getParameterTypes().length == 0) && (met.getReturnType() != Void.TYPE);
    }
    
    private boolean isSetter(Method met) {
        return (met.getParameterTypes().length == 1) && (met.getReturnType() == Void.TYPE);
    }
    
    private void validateMethodPair(Method getter, Method setter) {
        Class<?> type = getter.getReturnType();
        
        if (type.equals(Void.TYPE)) throw new InspectionPointException("Getter should return something! : " + getter.getName());
        
        if (
            getter.getParameterTypes().length != 0
        ) throw new InspectionPointException("Getter should not have parameters! : " + getter.getName());
        
        if (
            !setter.getReturnType().equals(Void.TYPE)
        ) throw new InspectionPointException("Setter should not be return type null. : " + setter.getName());
        
        Class<?>[] setterParas = setter.getParameterTypes();
        
        if (setterParas.length != 1) throw new InspectionPointException("Setter should have exactly one parameter! : " + setter.getName());
        
        if (
            !setterParas[0].equals(type)
        ) throw new InspectionPointException("Getter parameter is not the same type as getter return value");
        
    }
    
    private void validateReadOnlyGetter(Method getter) {
        if (
            getter.getReturnType().equals(Void.TYPE)
        ) throw new InspectionPointException("Getter should return something! : " + getter.getName());
        
        if (
            getter.getParameterTypes().length != 0
        ) throw new InspectionPointException("Getter should not have parameters! : " + getter.getName());
    }
    
    private String getDisplayNameForField(Field f) {
        InspectionAttribute anno = f.getAnnotation(InspectionAttribute.class);
        if (anno != null) {
            String name = anno.name();
            if ((name != null) && !name.isEmpty()) return name;
        }
        return f.getName();
    }
    
    private String getDsiplayNameForInspectionMethod(Method m) {
        InspectionMethod anno = m.getAnnotation(InspectionMethod.class);
        if (anno != null) {
            String name = anno.name();
            if ((name != null) && !name.isEmpty()) return name;
        }
        return m.getName();
    }
    
    private String getDisplayNameForMethod(Method m, String possiblePrefixToRemove) {
        InspectionAttribute anno = m.getAnnotation(InspectionAttribute.class);
        if (anno != null) {
            String name = anno.name();
            if ((name != null) && !name.isEmpty()) return name;
        }
        
        String name = m.getName();
        
        if (name.toLowerCase().startsWith(possiblePrefixToRemove)) {
            name = name.substring(3);
            name = name.substring(0, 1).toLowerCase() + name.substring(1);
        }
        return name;
    }
    
    private static class AttributeInspectionPoint {
        private static final Map<Class<?>, Class<?>> primitiveToWrapperMap = new HashMap<>();
        static {
            primitiveToWrapperMap.put(Integer.TYPE, Integer.class);
            primitiveToWrapperMap.put(Long.TYPE, Long.class);
            primitiveToWrapperMap.put(Character.TYPE, Character.class);
            primitiveToWrapperMap.put(Byte.TYPE, Byte.class);
            primitiveToWrapperMap.put(Float.TYPE, Float.class);
            primitiveToWrapperMap.put(Double.TYPE, Double.class);
            primitiveToWrapperMap.put(Short.TYPE, Short.class);
            primitiveToWrapperMap.put(Boolean.TYPE, Boolean.class);
            primitiveToWrapperMap.put(Void.TYPE, Void.class);
        }
        private final boolean  usesField;
        private final Field    f;
        private final Method   getter;
        private final Method   setter;
        private final Class<?> type;
        private final boolean  readOnly;
        
        /**
         * Creates a new attribute inspection point for a field
         * 
         * @param field
         *     The field for the inspection point.
         */
        public AttributeInspectionPoint(Field field) {
            this.usesField = true;
            this.f = field;
            this.getter = null;
            this.setter = null;
            this.type = convertTypeToWrappers(field.getType());
            this.readOnly = field.getAnnotation(InspectionAttribute.class).readOnly();
        }
        
        public AttributeInspectionPoint(Method getter) {
            this.usesField = false;
            this.f = null;
            this.getter = getter;
            this.setter = null;
            this.type = convertTypeToWrappers(getter.getReturnType());
            this.readOnly = true;
        }
        
        public AttributeInspectionPoint(Method getter, Method setter) {
            this.usesField = false;
            this.f = null;
            this.getter = getter;
            this.setter = setter;
            this.type = convertTypeToWrappers(getter.getReturnType());
            this.readOnly = false;
        }
        
        private Class<?> convertTypeToWrappers(Class<?> cls) {
            if (!cls.isPrimitive()) return cls;
            return primitiveToWrapperMap.get(cls);
        }
        
        public Object getValue(Object obj) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            if (this.usesField) return this.f.get(obj);
            return this.getter.invoke(obj);
        }
        
        public void setValue(Object obj, Object value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
            if (this.readOnly) throw new InspectionPointException("Attribute is read only.");
            if (!this.type.isAssignableFrom(value.getClass())) throw new IllegalArgumentException("Not the correct attribute type.");
            if (this.usesField) {
                this.f.set(obj, value);
            } else {
                this.setter.invoke(obj, value);
            }
        }
        
        /**
         * Get's {@link #type type}
         * 
         * @return type
         */
        public Class<?> getType() {
            return this.type;
        }
        
        /**
         * Get's {@link #readOnly readOnly}
         * 
         * @return readOnly
         */
        public boolean isReadOnly() {
            return this.readOnly;
        }
    }
    
    /**
     * A exception that is thrown when an error with a inspection point occurs.
     * 
     * @author Tim Neumann
     */
    public static class InspectionPointException extends RuntimeException {
        /**
         * generated
         */
        private static final long serialVersionUID = 6324656121971704376L;
        
        /**
         * Constructs a new runtime exception with {@code null} as its detail message. The cause is not initialized, and
         * may subsequently be initialized by a call to {@link #initCause}.
         */
        public InspectionPointException() {
            super();
        }
        
        /**
         * Constructs a new runtime exception with the specified detail message. The cause is not initialized, and may
         * subsequently be initialized by a call to {@link #initCause}.
         *
         * @param message
         *     the detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
         */
        public InspectionPointException(String message) {
            super(message);
        }
        
        /**
         * Constructs a new runtime exception with the specified detail message and cause.
         * <p>
         * Note that the detail message associated with {@code cause} is <i>not</i> automatically incorporated in this
         * runtime exception's detail message.
         *
         * @param message
         *     the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
         * @param cause
         *     the cause (which is saved for later retrieval by the {@link #getCause()} method). (A {@code null} value
         *     is permitted, and indicates that the cause is nonexistent or unknown.)
         * @since 1.4
         */
        public InspectionPointException(String message, Throwable cause) {
            super(message, cause);
        }
        
        /**
         * Constructs a new runtime exception with the specified cause and a detail message of
         * {@code (cause==null ? null : cause.toString())} (which typically contains the class and detail message of
         * {@code cause}). This constructor is useful for runtime exceptions that are little more than wrappers for
         * other throwables.
         *
         * @param cause
         *     the cause (which is saved for later retrieval by the {@link #getCause()} method). (A {@code null} value
         *     is permitted, and indicates that the cause is nonexistent or unknown.)
         * @since 1.4
         */
        public InspectionPointException(Throwable cause) {
            super(cause);
        }
        
        /**
         * Constructs a new runtime exception with the specified detail message, cause, suppression enabled or disabled,
         * and writable stack trace enabled or disabled.
         *
         * @param message
         *     the detail message.
         * @param cause
         *     the cause. (A {@code null} value is permitted, and indicates that the cause is nonexistent or unknown.)
         * @param enableSuppression
         *     whether or not suppression is enabled or disabled
         * @param writableStackTrace
         *     whether or not the stack trace should be writable
         *
         * @since 1.7
         */
        protected InspectionPointException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
}
