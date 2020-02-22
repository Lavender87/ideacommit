package com.gupaoedu.vip.spring.formwork.aop;

import com.gupaoedu.vip.spring.formwork.aop.intercept.GPMethodInvocation;
import com.gupaoedu.vip.spring.formwork.aop.support.GPAdvisedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class GPJdkDynamicAopProxy implements GPAopProxy, InvocationHandler {

    private GPAdvisedSupport advised;


    public GPJdkDynamicAopProxy(GPAdvisedSupport advised) {
        this.advised = advised;
    }

    @Override
    public Object getProxy() {
        return null;
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {

        return Proxy.newProxyInstance(classLoader,this.advised.getTargetClass().getInterfaces(),this);

    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        GPMethodInvocation methodInvocation =
                new GPMethodInvocation(proxy,null,method,args,this.advised.getTargetClass(),null);
        return methodInvocation.proceed();
    }
}
