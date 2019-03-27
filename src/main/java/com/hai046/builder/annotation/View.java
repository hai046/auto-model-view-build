package com.hai046.builder.annotation;

import java.lang.annotation.*;

/**
 * @author denghaizhu
 * * date 2019-02-22
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface View {

    /**
     * 需要渲染的model
     *
     * @return
     */
    Class<?> model();

    /**
     * 字段映射关系
     *
     * @return
     */
    ViewType[] fieldMapper();

}
