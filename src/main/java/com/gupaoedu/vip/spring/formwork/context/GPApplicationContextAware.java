package com.gupaoedu.vip.spring.formwork.context;

/**
 * IOC容器顶层设置
 * 通过一个监听器扫描所有类，只要实现了这个接口
 * 将自动调用setApplicationContext ，将IOC容器注入到目标类
 */
public interface GPApplicationContextAware {

    void setApplicationContext(GPApplicationContext applicationContext) ;//throws BeansException;
}
