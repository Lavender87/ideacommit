package com.gupaoedu.vip.spring.formwork.beans;

public interface GPBeanFactory {

    Object getBean(String beanName);

    Object getBean(Class<?> beanClass);
}
