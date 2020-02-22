package com.gupaoedu.vip.spring.formwork.aop.support;

import com.gupaoedu.vip.spring.formwork.aop.aspect.GPAfterReturningAdviceInterceptor;
import com.gupaoedu.vip.spring.formwork.aop.aspect.GPAspectJAfterThrowingAdvice;
import com.gupaoedu.vip.spring.formwork.aop.aspect.GPMethodBeforeAdviceInterceptor;
import com.gupaoedu.vip.spring.formwork.aop.config.GPAopConfig;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GPAdvisedSupport {

    private Class<?> targetClass;

    private Object target;

    private GPAopConfig config;

    public GPAdvisedSupport(GPAopConfig config) {
        this.config = config;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method, Class<?> targetClass) {
        return null;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
        parse();

        Pattern pattern = Pattern.compile("");
        try {
            Class aspectClass =Class.forName(this.config.getAspectClass());
            Map<String,Method> methodMap = new HashMap<>();
            for (Method method:   aspectClass.getMethods()) {
                methodMap.put(method.getName(),method);
            }

            Method[] methods=this.targetClass.getMethods();
            for (Method method:  methods) {
                String methodString =method.toString();
                if(methodString.contains("throws")){

                }

                Matcher matcher = pattern.matcher(methodString);
                if(matcher.matches()){
                    List<Object> advises = new LinkedList<>();
                    //包装成MethodInterceptor
                    /**
                     * before
                     */
                    if(null!=this.config.getAspectBefore() && !"".equals(this.config.getAspectBefore())){
                        this.config.getAspectBefore();
                        advises.add(new GPMethodBeforeAdviceInterceptor(methodMap.get(this.config.getAspectBefore()),aspectClass.newInstance()));
                    }

                    /**
                     * after
                     */
                    if(null!=this.config.getAspectAfter() && !"".equals(this.config.getAspectAfter())){
                        this.config.getAspectAfter();
                        advises.add(new GPAfterReturningAdviceInterceptor(methodMap.get(this.config.getAspectAfter()),aspectClass.newInstance()));
                    }


                    /**
                     *afterthrowing
                     */

                    if(null!=this.config.getAspectAfterThrow() && !"".equals(this.config.getAspectAfterThrow())){
                        this.config.getAspectAfterThrow();
                        advises.add(new GPAspectJAfterThrowingAdvice(methodMap.get(this.config.getAspectAfterThrow()),aspectClass.newInstance()));
                    }

                }

            }




        } catch (Exception e) {

        }


    }

    /**
     * 匹配符合条件的类
     * pointCut=public .* com.gupaoedu.vip.spring.demo.service.."Service..*(.*)
     */
    private void parse() {
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public boolean pointMatch(){
        return true;
    }
}
