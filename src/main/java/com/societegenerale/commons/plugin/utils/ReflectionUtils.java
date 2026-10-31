package com.societegenerale.commons.plugin.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReflectionUtils {
    public static Class<?> loadClassWithContextClassLoader(String className) {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            throw ReflectionException.wrap(e);
        }
    }

    public static <T> T newInstance(Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw ReflectionException.wrap(e);
        }
    }

    // This will always be unsafe, so we might as well make it easier for the caller
    @SuppressWarnings("unchecked")
    public static <T> T invoke(Method method, Object owner, Object... args) {
        try {
            method.setAccessible(true);
            return (T) method.invoke(owner, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw ReflectionException.wrap(e);
        }
    }

    // This will always be unsafe, so we might as well make it easier for the caller
    @SuppressWarnings("unchecked")
    public static <T> T getValue(Field field, Object owner) {
        try {
            field.setAccessible(true);
            return (T) field.get(owner);
        } catch (IllegalAccessException e) {
            throw ReflectionException.wrap(e);
        }
    }

    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        Set<Class<?>> visited = new HashSet<>();
        addFieldsRecursively(fields, clazz, visited);
        return fields;
    }

    private static void addFieldsRecursively(List<Field> fields, Class<?> clazz, Set<Class<?>> visited) {
        if (clazz == Object.class || !visited.add(clazz)) {
            return;
        }
        for (Class<?> interfaceClazz : clazz.getInterfaces()) {
            addFieldsRecursively(fields, interfaceClazz, visited);
        }
        Class<?> superClass = clazz.getSuperclass();
        if (null != superClass) {
            addFieldsRecursively(fields, superClass, visited);
        }
        Collections.addAll(fields, clazz.getDeclaredFields());
    }
}
