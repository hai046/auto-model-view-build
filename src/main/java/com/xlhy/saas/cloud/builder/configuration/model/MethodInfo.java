package com.xlhy.saas.cloud.builder.configuration.model;

import java.lang.reflect.Field;
import java.util.function.Function;

/**
 * @author denghaizhu
 * @date 2019-02-22
 */
public class MethodInfo <K,V> {
    private java.lang.reflect.Field Field;
    private Class<?> referenceType;
    private Function<K,V> function;

    public Field getField() {
        return Field;
    }

    public void setField(Field field) {
        this.Field = field;
    }

    public  Function<?,?> getFunction() {
        return function;
    }

    public void setFunction(Function<K,V> function) {
        this.function = function;
    }

    public void setReferenceType(Class<?> referenceType) {
        this.referenceType = referenceType;
    }

    public Class<?> getReferenceType() {
        return referenceType;
    }
}
