package com.hai046.builder.annotation;

import com.hai046.builder.configuration.ViewBuilderScannerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author denghaizhu
 * @date 2019-02-22
 * 开启view渲染的总开关
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ViewBuilderScannerRegistrar.class)
public @interface EnableView {

    String[] viewBuilderPackages();
}
