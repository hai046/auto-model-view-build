package com.xlhy.saas.cloud.builder.annotation;

import com.xlhy.saas.cloud.builder.configuration.ViewBuilderScannerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author denghaizhu
 * @date 2019-02-22
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ViewBuilderScannerRegistrar.class)
public @interface EnableView {

    String[] viewBuilderPackages();
}
