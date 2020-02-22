package com.gupaoedu.vip.spring.formwork.aop.aspect;

import java.lang.reflect.Method;

public abstract  class GPAbstractAdvisor {

    private Method aspectMethod;

    private Object aspectTarget;

    public GPAbstractAdvisor(Method aspectMethod, Object aspectTarget) {
        this.aspectMethod = aspectMethod;
        this.aspectTarget = aspectTarget;
    }
}
