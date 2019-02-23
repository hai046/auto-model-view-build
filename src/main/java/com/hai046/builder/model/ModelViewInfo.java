package com.hai046.builder.model;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * @author denghaizhu
 * @date 2019-02-22
 */
public final class ModelViewInfo {
    private final String className;
    private final Class<?>[] models;

    public ModelViewInfo(String className, Class<?>[] models) {
        this.className = className;
        this.models = models;
    }

    public String getClassName() {
        return className;
    }

    public Class<?>[] getModels() {
        return models;
    }

    @Override
    public int hashCode() {
        return className != null ? className.hashCode() : super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ModelViewInfo) {
            return StringUtils.equals(className, ((ModelViewInfo) obj).className);
        }
        return false;
    }

    @Override
    public String toString() {
        return "ModelViewInfo{" +
                "className='" + className + '\'' +
                ", models=" + Arrays.toString(models) +
                '}';
    }
}

