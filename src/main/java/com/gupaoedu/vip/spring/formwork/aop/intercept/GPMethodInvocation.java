package com.gupaoedu.vip.spring.formwork.aop.intercept;

import com.gupaoedu.vip.spring.formwork.aop.support.GPMethodInterceptor;

import java.lang.reflect.Method;
import java.util.List;

public class GPMethodInvocation {

    protected final Object proxy;

    protected final Object target;

    protected final Method method;

    protected Object[] arguments;

    private final Class<?> targetClass;

    List<Object> interceptorsAndDynamicMethodMatchers;


    /**
     * Index from 0 of the current interceptor we're invoking.
     * -1 until we invoke: then the current interceptor.
     */
    private int currentInterceptorIndex = -1;

    public GPMethodInvocation(
            Object proxy, Object target, Method method, Object[] arguments,
            Class<?> targetClass, List<Object> interceptorsAndDynamicMethodMatchers) {

        this.proxy = proxy;
        this.target = target;
        this.targetClass = targetClass;
        this.method = method;
        this.arguments = arguments;
        this.interceptorsAndDynamicMethodMatchers = interceptorsAndDynamicMethodMatchers;
    }

    public Object proceed() throws Throwable {

        // We start with an index of -1 and increment early.
        if (this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() - 1) {
            return this.method.invoke(this.target, this.arguments);
        }

        Object interceptorOrInterceptionAdvice =
                this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);
        if (interceptorOrInterceptionAdvice instanceof GPMethodInterceptor) {
            // Evaluate dynamic method matcher here: static part will already have
            // been evaluated and found to match.
            GPMethodInterceptor im =
                    (GPMethodInterceptor) interceptorOrInterceptionAdvice;
            return im.invoke(this);
        }else {
            // Dynamic matching failed.
            // Skip this interceptor and invoke the next in the chain.
            return proceed();
        }

    }

}
