package com.gupaoedu.vip.spring.formwork.aop;

import com.gupaoedu.vip.spring.formwork.aop.support.GPAdvisedSupport;

public class GPCglibAopProxy implements GPAopProxy{


    private GPAdvisedSupport advised;

    public GPCglibAopProxy(GPAdvisedSupport advised) {
        this.advised = advised;
    }

    @Override
    public Object getProxy() {
        return null;
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return null;
    }
}
