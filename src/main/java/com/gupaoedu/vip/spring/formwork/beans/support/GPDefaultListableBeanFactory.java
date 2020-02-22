package com.gupaoedu.vip.spring.formwork.beans.support;

import com.gupaoedu.vip.spring.formwork.beans.config.GPBeanDefinition;
import com.gupaoedu.vip.spring.formwork.context.support.GPAbstractApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GPDefaultListableBeanFactory extends GPAbstractApplicationContext {

    public final Map<String, GPBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
}
