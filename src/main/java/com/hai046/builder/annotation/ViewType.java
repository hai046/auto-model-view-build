package com.hai046.builder.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author denghaizhu
 * @date 2019-02-22
 */
@Target({})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ViewType {

    /**
     * 自动id
     *
     * @return
     */
    String id();

    /**
     * id对应的model 映射类
     *
     * @return
     */
    Class<?> referenceType();


}
