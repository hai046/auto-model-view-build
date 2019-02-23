package com.xlhy.sass.cloud.builder;

import com.xlhy.sass.cloud.builder.configuration.ViewBuilderFactoryBean;
import com.xlhy.sass.cloud.builder.configuration.model.MethodInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author denghaizhu
 * @date 2019-02-22
 */
@Service
public class ViewBuilder {

    private static Logger logger = LoggerFactory.getLogger(ViewBuilder.class);
    @Autowired
    private ViewBuilderFactoryBean viewBuilderFactoryBean;

    private Map<Class<?>, Function<?, ?>> modelExtractors = new HashMap<>();


    public <K, V> Object build(Collection<K> models, Class<V> view) {

        final List<MethodInfo> methodInfos = viewBuilderFactoryBean.mapperAndReturnMethodInfo(view);
        return models.stream().map(m -> {
            if (m == null) {
                logger.warn("view builder {}  model is null, skip it !", view);
                return null;
            }
            final Constructor<?> constructor = viewBuilderFactoryBean.getConstructor(view);

            try {
                final Object o = constructor.newInstance();
                for (MethodInfo methodInfo : methodInfos) {
                    final Function idFunction = methodInfo.getFunction();
                    if (idFunction == null) {
                        if (methodInfo.getReferenceType() == view) {
                            final Field field = methodInfo.getField();
                            field.set(o, m);
                        }
                        continue;
                    }
                    final Object id = idFunction.apply(m);
                    final Function function = modelExtractors.get(methodInfo.getReferenceType());
                    if (function == null) {
                        logger.debug("not mapping {}", methodInfo.getReferenceType());
                        continue;
                    }
                    final Object idModelView = function.apply(id);
                    final Field field = methodInfo.getField();
                    field.set(o, idModelView);
                }
                return o;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());


    }


    public <K, V> void addModelData(Function<K, V> function, Class<V> clazz) {
        modelExtractors.put(clazz, function);
    }


}
