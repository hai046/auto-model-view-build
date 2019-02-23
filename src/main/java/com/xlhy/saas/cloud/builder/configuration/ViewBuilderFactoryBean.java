package com.xlhy.saas.cloud.builder.configuration;

import com.xlhy.saas.cloud.builder.annotation.AutoModel;
import com.xlhy.saas.cloud.builder.annotation.View;
import com.xlhy.saas.cloud.builder.annotation.ViewType;
import com.xlhy.saas.cloud.builder.model.MethodInfo;
import com.xlhy.saas.cloud.builder.model.ModelViewInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author denghaizhu
 * @date 2019-02-22
 */
@Service
public class ViewBuilderFactoryBean {

    private static Logger logger = LoggerFactory.getLogger(ViewBuilderFactoryBean.class);

    private Set<ModelViewInfo> modelViewInfos;

    private Map<Class, List<MethodInfo>> viewMapperMap = new HashMap<>();
    private Map<Class, Constructor> classConstructorHashMap = new HashMap<>();

    public Set<ModelViewInfo> getModelViewInfos() {
        return modelViewInfos;
    }

    public void setModelViewInfos(Set<ModelViewInfo> modelViewInfos) {
        this.modelViewInfos = modelViewInfos;
        init(modelViewInfos);
    }

    private void init(Set<ModelViewInfo> modelViewInfos) {
        logger.info("modelViewInfos={}", modelViewInfos);

        for (ModelViewInfo modelViewInfo : modelViewInfos) {
            try {
                final Class<?> aViewClass = Class.forName(modelViewInfo.getClassName());
                final View annotation = aViewClass.getAnnotation(View.class);
                final Class<?> model = annotation.model();
                final ViewType[] viewTypes = annotation.fieldMapper();
                if (viewTypes.length < 1) {
                    continue;
                }

                final Field[] declaredFields = aViewClass.getDeclaredFields();
                Map<Class, Field> classFieldMap = new HashMap<>(viewTypes.length);
                for (Field field : declaredFields) {
                    final AutoModel autoModel = field.getAnnotation(AutoModel.class);
                    if (autoModel != null) {
                        field.setAccessible(true);
                        final Class<?> type = field.getType();
                        classFieldMap.put(type, field);
                        logger.info("{}  field {}", aViewClass.getName(), field.getName());
                    }
                }

                List<MethodInfo> item = new ArrayList<>(viewTypes.length);
                for (int i = 0; i < viewTypes.length; i++) {
                    ViewType viewType = viewTypes[i];
                    final Method method = model.getMethod("get" + (viewType.id().substring(0, 1).toUpperCase()) + viewType.id().substring(1));
                    MethodInfo methodInfo = new MethodInfo();
                    methodInfo.setField(classFieldMap.get(viewType.referenceType()));
                    methodInfo.setReferenceType(viewType.referenceType());
                    methodInfo.setFunction(bean -> ReflectionUtils.invokeMethod(method, bean));
                    item.add(methodInfo);
                }


                final Field viewModel = classFieldMap.get(model);
                if (viewModel != null) {
                    MethodInfo methodInfo = new MethodInfo();
                    methodInfo.setField(viewModel);
                    methodInfo.setReferenceType(aViewClass);
                    item.add(methodInfo);
                }


                final Constructor<?> constructor = aViewClass.getConstructor();
                if (constructor == null) {
                    final String msg = String.format("class %s must be have default constructor", aViewClass.getName());
                    logger.error(msg);
                    throw new RuntimeException(msg);
                }

                classConstructorHashMap.put(aViewClass, constructor);
                viewMapperMap.put(aViewClass, item);
                logger.info("scan item={}", item);


            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    public List<MethodInfo> getMethodInfo(Class<?> view) {
        return viewMapperMap.get(view);

    }


    public <V> Constructor<V> getConstructor(Class<V> view) {
        return classConstructorHashMap.get(view);
    }
}
