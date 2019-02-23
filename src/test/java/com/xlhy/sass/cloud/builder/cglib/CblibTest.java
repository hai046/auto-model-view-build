package com.xlhy.sass.cloud.builder.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author denghaizhu
 * @date 2019-02-22
 */
public class CblibTest implements MethodInterceptor {

    private Object target;


    //定义获取代理对象方法
    public Object getCglibProxy(Object objectTarget) {
        this.target = objectTarget;
        //为目标对象target赋值
        Enhancer enhancer = new Enhancer();
        //设置父类,因为Cglib是针对指定的类生成一个子类，所以需要指定父类
        enhancer.setSuperclass(objectTarget.getClass());
        enhancer.setCallback(this);// 设置回调
        Object result = enhancer.create();//创建并返回代理对象
        return result;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Object invoke = method.invoke(target, args);
        System.out.println("cglib end "+method.getName());
        return invoke;
    }

    public static void main(String args[]) {
        CblibTest cblibTest = new CblibTest();
        final CglibImpl cglibProxy = (CglibImpl) cblibTest.getCglibProxy(new CglibImpl());
        System.out.println(cglibProxy.test());
        System.exit(0);
    }


}
