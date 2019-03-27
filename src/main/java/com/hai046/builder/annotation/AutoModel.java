package com.hai046.builder.annotation;

import java.lang.annotation.*;

/**
 * Created by haizhu on 2019-03-27
 * <p></p>
 * 自动获取当前view里面的model对象
 *
 * @author haizhu12345@gmail.com
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AutoModel {
}
