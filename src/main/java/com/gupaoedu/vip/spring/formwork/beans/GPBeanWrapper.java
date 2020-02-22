package com.gupaoedu.vip.spring.formwork.beans;

public class GPBeanWrapper {

    private Object wrappedInstance;

    private Class<?> wrappedClass;


    public GPBeanWrapper(Object wrappedInstance) {
        this.wrappedInstance = wrappedInstance;
//        this.wrappedClass = wrappedClass;
    }

    /**
     * Return the bean instance wrapped by this object.
     */
    public Object getWrappedInstance(){
        return null;
    }

    /**
     * Return the type of the wrapped bean instance.
     */
    public Class<?> getWrappedClass(){
        return null;
    }
}
