package com.xlhy.saas.cloud.builder.annotation;

import java.lang.annotation.*;

/**
 * @author denghaizhu
 * @date 2019-02-22
 * 自动获取当前view里面的model对象
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AutoModel {
}
