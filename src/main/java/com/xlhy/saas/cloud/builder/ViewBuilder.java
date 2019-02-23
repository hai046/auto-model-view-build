package com.xlhy.saas.cloud.builder;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.xlhy.saas.cloud.builder.configuration.ViewBuilderFactoryBean;
import com.xlhy.saas.cloud.builder.model.MethodInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
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


    /**
     * 把K模型的类型渲染到V
     *
     * @param models
     * @param view
     * @param <K>
     * @param <V>
     * @return
     */
    public <K, V> List<V> build(Collection<K> models, Class<V> view) {
        if (models == null) {
            logger.info(view.getName() + "'s build model is null skip ");
            return null;
        }
        final List<MethodInfo> methodInfos = viewBuilderFactoryBean.getMethodInfo(view);
        if (CollectionUtils.isEmpty(methodInfos)) {
            throw new RuntimeException(view.getName() + " not mapper ");
        }
        final Constructor<V> constructor = viewBuilderFactoryBean.getConstructor(view);
        Multimap<Class, Object> idsCollectionsMap = HashMultimap.create();
        //聚合id
        models.forEach(m -> {
            for (MethodInfo methodInfo : methodInfos) {
                final Function idFunction = methodInfo.getFunction();
                if (idFunction == null) {
                    continue;
                }
                final Object id = idFunction.apply(m);
                final Function function = modelExtractors.get(methodInfo.getReferenceType());
                if (function == null) {
                    logger.debug("not mapping {}", methodInfo.getReferenceType());
                    continue;
                }
                idsCollectionsMap.put(methodInfo.getReferenceType(), id);
            }
        });

        // id->model
        final Map<Class, Map<?, ?>> idsMapResult = idsCollectionsMap.keySet()
                .stream()
                .collect(Collectors.toMap(k -> k, k -> {
                    final Collection<Object> ids = idsCollectionsMap.get(k);
                    final Function<Collection<?>, Map<?, ?>> mapFunction = (Function<Collection<?>, Map<?, ?>>) modelExtractors.get(k);
                    if (mapFunction == null) {
                        logger.warn("not model={} view builder", k);
                        return Collections.emptyMap();
                    }
                    return mapFunction.apply(ids);
                }));


        //start renderer
        return models.stream().map(m -> {
            if (m == null) {
                logger.warn("view builder {}  model is null, skip it !", view);
                return null;
            }
            try {
                final V o = constructor.newInstance();
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
                    final Map<?, ?> map = idsMapResult.get(methodInfo.getReferenceType());
                    if (map != null) {
                        final Field field = methodInfo.getField();
                        field.set(o, map.get(id));
                    }

                }
                return o;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                logger.warn(e.getMessage(), e);
            }
            return null;
        }).filter(o -> !Objects.isNull(o)).collect(Collectors.toList());
    }


    /**
     * 添加model对应id转换关系，也就是通过对应model的id获取model列表
     *
     * @param function
     * @param clazz
     * @param <K>
     * @param <V>
     */
    public <K, V> void addId2ModelMapper(Function<Collection<K>, Map<K, V>> function, Class<V> clazz) {
        modelExtractors.put(clazz, function);
    }


}
