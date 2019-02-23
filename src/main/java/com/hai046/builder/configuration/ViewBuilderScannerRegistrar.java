package com.hai046.builder.configuration;

import com.hai046.builder.annotation.EnableView;
import com.hai046.builder.annotation.View;
import com.hai046.builder.model.ModelViewInfo;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author denghaizhu
 * @date 2019-02-22
 * 扫描view builder 配置
 */
public class ViewBuilderScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {
    private ResourceLoader resourceLoader;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false) {

            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                return beanDefinition.getMetadata().isIndependent();
            }
        };
        scanner.setResourceLoader(this.resourceLoader);


        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(
                View.class);
        final Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(EnableView.class.getName());
        if (annotationAttributes == null || annotationAttributes.isEmpty()) {
            return;
        }

        scanner.addIncludeFilter(annotationTypeFilter);
        Set<String> basePackages = getBasePackages(annotationAttributes);
        if (CollectionUtils.isEmpty(basePackages)) {
            return;
        }
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidateComponents = scanner
                    .findCandidateComponents(basePackage);

            Set<ModelViewInfo> modelViewInfos = new HashSet<>();
            for (BeanDefinition candidateComponent : candidateComponents) {
                if (candidateComponent instanceof AnnotatedBeanDefinition) {
                    AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) candidateComponent;
                    AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
                    Map<String, Object> attributes = annotationMetadata
                            .getAnnotationAttributes(View.class.getCanonicalName());
                    String className = annotationMetadata.getClassName();
                    final Class<?>[] models = (Class<?>[]) attributes.get("models");
                    modelViewInfos.add(new ModelViewInfo(className, models));

                }
            }
            registerBean(registry, modelViewInfos);

        }

    }

    private void registerBean(BeanDefinitionRegistry registry,
                              Set<ModelViewInfo> modelViewInfos) {
        BeanDefinitionBuilder definition = BeanDefinitionBuilder
                .genericBeanDefinition(ViewBuilderFactoryBean.class);

        definition.addPropertyValue("modelViewInfos", modelViewInfos);
        definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);

        AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();
        beanDefinition.setPrimary(true);
        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, ViewBuilderFactoryBean.class.getName(), new String[]{});

        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }

    protected Set<String> getBasePackages(Map<String, Object> attrs) {
        Set<String> basePackages = new HashSet<>();
        if (attrs != null) {
            String[] gRpcPackages = (String[]) attrs.get("viewBuilderPackages");
            basePackages.addAll(Arrays.asList(gRpcPackages));
        }
        return basePackages;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
