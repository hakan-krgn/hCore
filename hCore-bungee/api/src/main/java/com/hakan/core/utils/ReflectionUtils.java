package com.hakan.core.utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * General utils class for hCore.
 */
@SuppressWarnings({"unchecked"})
public final class ReflectionUtils {

    /**
     * Creates new instance of the given class.
     *
     * @param path Path of the class.
     * @param <T>  Type.
     * @return New instance of created class.
     */
    @Nonnull
    public static <T> T newInstance(@Nonnull String path) {
        return newInstance(path, new Class[0], new Object[0]);
    }

    /**
     * Creates new instance of given class.
     *
     * @param path    Path of class.
     * @param objects Objects to be used in constructor.
     * @param <T>     Type of class.
     * @return New instance of created class.
     */
    @Nonnull
    public static <T> T newInstance(@Nonnull String path,
                                    @Nonnull Class<?>[] classes,
                                    @Nonnull Object[] objects) {
        try {
            Validate.notNull(path, "path cannot be null!");
            Validate.notNull(classes, "classes cannot be null!");
            Validate.notNull(objects, "objects cannot be null!");

            Class<T> tClass = (Class<T>) Class.forName(path);

            Constructor<T> constructor = tClass.getDeclaredConstructor(classes);
            constructor.setAccessible(true);
            T instance = constructor.newInstance(objects);
            constructor.setAccessible(false);

            return instance;
        } catch (Exception e) {
            e.printStackTrace();
            return (T) classes[0];
        }
    }

    /**
     * Gets value of the given field.
     *
     * @param object    Object.
     * @param fieldName Field name.
     * @param <T>       Type of field.
     * @return Value of the given field.
     */
    @Nullable
    public static <T> T getField(@Nonnull Object object,
                                 @Nonnull String fieldName) {
        try {
            Validate.notNull(object, "object cannot be null!");
            Validate.notNull(fieldName, "fieldName cannot be null!");

            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(object);
            field.setAccessible(false);
            return (value != null) ? (T) value : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Sets static field value.
     *
     * @param clazz     Class.
     * @param fieldName Field name.
     * @param value     Value.
     * @param <T>       Type.
     */
    public static <T> void setField(@Nonnull Class<?> clazz,
                                    @Nonnull String fieldName,
                                    @Nonnull T value) {
        try {
            Validate.notNull(clazz, "class cannot be null!");
            Validate.notNull(fieldName, "fieldName cannot be null!");
            Validate.notNull(value, "value cannot be null!");

            Field field = clazz.getDeclaredField(fieldName);
            boolean accessible = field.isAccessible();

            field.setAccessible(true);
            field.set(null, value);
            field.setAccessible(accessible);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets field value.
     *
     * @param object    Object.
     * @param fieldName Field name.
     * @param value     Value.
     * @param <T>       Type.
     */
    public static <T> void setField(@Nonnull Object object,
                                    @Nonnull String fieldName,
                                    @Nonnull T value) {
        try {
            Validate.notNull(object, "object cannot be null!");
            Validate.notNull(fieldName, "fieldName cannot be null!");
            Validate.notNull(value, "value cannot be null!");

            Field field = object.getClass().getDeclaredField(fieldName);
            boolean accessible = field.isAccessible();

            field.setAccessible(true);
            field.set(object, value);
            field.setAccessible(accessible);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Invokes method.
     *
     * @param object     Object.
     * @param methodName Method name.
     * @param params     Params.
     * @param <T>        Type.
     */
    @Nullable
    public static <T> T invoke(@Nonnull Object object,
                               @Nonnull String methodName,
                               @Nonnull Object... params) {
        return invoke(object.getClass(), object, methodName, params);
    }

    /**
     * Invokes method.
     *
     * @param object     Object.
     * @param methodName Method name.
     * @param params     Params.
     * @param <T>        Type.
     */
    @Nullable
    public static <T> T invoke(@Nonnull Class<?> clazz,
                               @Nonnull Object object,
                               @Nonnull String methodName,
                               @Nonnull Object... params) {

        Class<?>[] paramTypes = new Class<?>[params.length];
        for (int i = 0; i < params.length; i++)
            paramTypes[i] = params[i].getClass();
        return invoke(clazz, object, methodName, paramTypes, params);
    }

    /**
     * Invokes method.
     *
     * @param object     Object.
     * @param methodName Method name.
     * @param params     Params.
     * @param <T>        Type.
     */
    @Nullable
    public static <T> T invoke(@Nonnull Class<?> clazz,
                               @Nonnull Object object,
                               @Nonnull String methodName,
                               @Nonnull Class<?>[] paramTypes,
                               @Nonnull Object... params) {
        try {
            Validate.notNull(clazz, "class cannot be null!");
            Validate.notNull(object, "object cannot be null!");
            Validate.notNull(methodName, "method name cannot be null!");
            Validate.notNull(paramTypes, "param types cannot be null!");
            Validate.notNull(params, "params cannot be null!");

            Method method = clazz.getDeclaredMethod(methodName, paramTypes);
            boolean accessible = method.isAccessible();

            method.setAccessible(true);
            Object value = method.invoke(object, params);
            method.setAccessible(accessible);

            return (value != null) ? (T) value : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Invokes method.
     *
     * @param object Object.
     * @param method Method.
     * @param <T>    Type.
     */
    public static <T> T invoke(@Nonnull Object object,
                               @Nonnull Method method,
                               @Nonnull Object... params) {
        try {
            Validate.notNull(object, "object cannot be null!");
            Validate.notNull(method, "method cannot be null!");

            boolean accessible = method.isAccessible();

            method.setAccessible(true);
            Object value = method.invoke(object, params);
            method.setAccessible(accessible);

            return (value != null) ? (T) value : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
