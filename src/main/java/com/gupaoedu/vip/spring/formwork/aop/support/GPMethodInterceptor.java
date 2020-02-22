package com.gupaoedu.vip.spring.formwork.aop.support;

import com.gupaoedu.vip.spring.formwork.aop.intercept.GPMethodInvocation;

public interface GPMethodInterceptor {

    Object invoke(GPMethodInvocation invocation) throws Throwable;
}
