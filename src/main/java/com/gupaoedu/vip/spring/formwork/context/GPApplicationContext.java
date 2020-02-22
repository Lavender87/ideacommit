package com.gupaoedu.vip.spring.formwork.context;

import com.gupaoedu.vip.spring.formwork.annotation.GPAutowried;
import com.gupaoedu.vip.spring.formwork.annotation.GPController;
import com.gupaoedu.vip.spring.formwork.annotation.GPService;
import com.gupaoedu.vip.spring.formwork.aop.GPAopProxy;
import com.gupaoedu.vip.spring.formwork.aop.GPCglibAopProxy;
import com.gupaoedu.vip.spring.formwork.aop.GPJdkDynamicAopProxy;
import com.gupaoedu.vip.spring.formwork.aop.config.GPAopConfig;
import com.gupaoedu.vip.spring.formwork.aop.support.GPAdvisedSupport;
import com.gupaoedu.vip.spring.formwork.beans.GPBeanFactory;
import com.gupaoedu.vip.spring.formwork.beans.GPBeanWrapper;
import com.gupaoedu.vip.spring.formwork.beans.config.GPBeanDefinition;
import com.gupaoedu.vip.spring.formwork.beans.config.GPBeanPostProcessor;
import com.gupaoedu.vip.spring.formwork.beans.support.GPBeanDefinitionReader;
import com.gupaoedu.vip.spring.formwork.beans.support.GPDefaultListableBeanFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class GPApplicationContext extends GPDefaultListableBeanFactory implements GPBeanFactory {

    public static void main(String[] args) {
        System.out.println("1----"+GPApplicationContext.class.getSimpleName());

        //得到对象的全路径
        System.out.println("2----"+GPApplicationContext.class);

        //得到对象的类模板示例，也就是Class
        System.out.println("3----"+GPApplicationContext.class.getClass());

        //得到Class类的名称
        System.out.println("4----"+GPApplicationContext.class.getClass().getName());


//        1----GPApplicationContext
//        2----class com.gupaoedu.vip.spring.formwork.context.GPApplicationContext
//        3----class java.lang.Class
//        4----java.lang.Class

        //beanName="com.gupaoedu.vip.spring.demo.service.IQueryservice"
        //className="com.gupaoedu.vip.spring.demo.service.impl.QueryserviceImpl"
    }

    //单例IOC容器
    private Map<String,Object> singletonObject =new ConcurrentHashMap<>();

    private Map<String,GPBeanWrapper> factoryBeanInstanceCache =new ConcurrentHashMap<>();

    private String[] configLocations;

    private GPBeanDefinitionReader reader;

    public GPApplicationContext( String...configLocations){

        this.configLocations = configLocations;
        refresh();

    }

    @Override
    protected void refresh() {
        //1 定位配置文件 2 加载配置文件
        List<GPBeanDefinition> beanDefinitions = new GPBeanDefinitionReader(this.configLocations).loadBeanDefinitions();
        //3 注册到容器
        doBeanDefinitionRegistry(beanDefinitions);
        //4，把不是延迟加载的类，提前初始化
        doAutowried();
    }

    private void doBeanDefinitionRegistry(List<GPBeanDefinition> beanDefinitions){

        for (GPBeanDefinition gpBeanDefinition: beanDefinitions) {
            super.beanDefinitionMap.put(gpBeanDefinition.getFactoryBeanName(),gpBeanDefinition);
        }

    }

    private void doAutowried(){
        for (Map.Entry<String,GPBeanDefinition> beanDefinitionEntry:
             super.beanDefinitionMap.entrySet()) {
            String beanName= beanDefinitionEntry.getKey();
            if(!beanDefinitionEntry.getValue().isLazyInit()){
                getBean(beanName);
            }

        }

    }

    @Override
    public Object getBean(Class<?> beanClass) {
        return getBean(beanClass.getName());
    }

    @Override
    public Object getBean(String beanName) {
        GPBeanDefinition gpBeanDefinition = this.beanDefinitionMap.get(beanName);
        //1 初始化
        Object instance =  instantiateBean(beanName,gpBeanDefinition);

        GPBeanPostProcessor postProcessor = new GPBeanPostProcessor();
        try {
            postProcessor.postProcessBeforeInitialization(instance,beanName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        GPBeanWrapper gpBeanWrapper =  new GPBeanWrapper(instance);

//        createProxy()
        // 2放到IOC容器
        factoryBeanInstanceCache.put(beanName,gpBeanWrapper);

        try {
            postProcessor.postProcessAfterInitialization(instance,beanName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //3 注入
        populateBean(beanName,new GPBeanDefinition(),gpBeanWrapper);

        return factoryBeanInstanceCache.get(beanName);
    }

    private void populateBean(String beanName, GPBeanDefinition gpBeanDefinition, GPBeanWrapper gpBeanWrapper) {
        Object instance = gpBeanWrapper.getWrappedInstance();

        Class<?> clazz = gpBeanWrapper.getWrappedClass();


        if(!(clazz.isAnnotationPresent(GPController.class) || clazz.isAnnotationPresent(GPService.class)))
            return;

        //获取该类所有的fields
        Field[] fields = clazz.getDeclaredFields();
        for (Field field: fields) {
            if(!clazz.isAnnotationPresent(GPController.class))continue;
            GPAutowried autowried=field.getAnnotation(GPAutowried.class);
            String autowriedBeanName= autowried.value().trim();
            if("".equals(autowriedBeanName)){
                autowriedBeanName = field.getType().getName();
            }
            field.setAccessible(true);
            try {
                if(null==instance || null==this.factoryBeanInstanceCache.get(autowriedBeanName).getWrappedInstance())continue;
                field.set(instance,this.factoryBeanInstanceCache.get(autowriedBeanName).getWrappedInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private Object instantiateBean(String beanName, GPBeanDefinition gpBeanDefinition) {
        //1\拿到药实例化的对象类名
        String className= gpBeanDefinition.getBeanClassName();
        //2\反射实例化，得到一个对象
        Object instance = null;
        Class<?> clazz = null;
        if(this.singletonObject.containsKey(className)){
            instance = this.singletonObject.get(className);

        }else{

            GPAdvisedSupport config = instantionAopConfig(gpBeanDefinition);
            config.setTargetClass(clazz);
            config.setTarget(instance);
            if(config.pointMatch()){
                instance = createProxy(config);
            }


            try {
                clazz = Class.forName(className);
                instance  =  clazz.newInstance();
                this.singletonObject.put(className,instance);
                this.singletonObject.put(gpBeanDefinition.getFactoryBeanName(),instance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return instance;
    }

    private GPAopProxy createProxy(GPAdvisedSupport advise){
        Class targetClass = advise.getTargetClass();
        if(targetClass.getInterfaces().length>0){
            return new GPJdkDynamicAopProxy(advise);
        }
        return new GPCglibAopProxy(advise);
    }

    private GPAdvisedSupport instantionAopConfig(GPBeanDefinition gpBeanDefinition){
        GPAopConfig config = new GPAopConfig();
        config.setPointCut(this.reader.getConfig().getProperty(""));
        config.setAspectClass(this.reader.getConfig().getProperty("aspectClass"));
        config.setAspectBefore(this.reader.getConfig().getProperty("aspectBefore"));
        config.setAspectAfter(this.reader.getConfig().getProperty("aspectAfter"));
        config.setAspectAfterThrow(this.reader.getConfig().getProperty("aspectAfterThrow"));
        config.setAspectAfterThrowingName(this.reader.getConfig().getProperty("aspectAfterThrowingName"));

        return new GPAdvisedSupport(config);
    }

    public String[] getBeanDefinitionNames(){
        return this.beanDefinitionMap.keySet().toArray(new String[this.beanDefinitionMap.size()]);
    }

    public int getBeanDefinitionCount(){
        return this.beanDefinitionMap.size();
    }

    public Properties getConfig(){
        return this.reader.getConfig();
    }
}
