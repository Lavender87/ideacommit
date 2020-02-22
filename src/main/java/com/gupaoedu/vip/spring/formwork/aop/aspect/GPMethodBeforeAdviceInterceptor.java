package com.gupaoedu.vip.spring.formwork.aop.aspect;

import com.gupaoedu.vip.spring.formwork.aop.intercept.GPMethodInvocation;
import com.gupaoedu.vip.spring.formwork.aop.support.GPMethodInterceptor;

import java.lang.reflect.Method;

public class GPMethodBeforeAdviceInterceptor extends GPAbstractAdvisor implements GPMethodInterceptor {

    public GPMethodBeforeAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(GPMethodInvocation invocation) throws Throwable {
        return null;
    }
}
