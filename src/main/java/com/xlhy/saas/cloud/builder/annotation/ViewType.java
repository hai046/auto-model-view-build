package com.xlhy.saas.cloud.builder.annotation;

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

    String id();

    Class<?> referenceType();


}
