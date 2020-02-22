package com.gupaoedu.vip.spring.formwork.webmvc.servlet;


import com.gupaoedu.vip.spring.formwork.annotation.GPRequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class GPHandlerAdapter {

    public boolean supports(Object handler){
        return (handler instanceof GPHandlerMapping);
    }

    public GPModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        GPHandlerMapping gpHandlerMapping= (GPHandlerMapping)handler;
        Map<String,Integer> paramIndexMapping = new HashMap<String,Integer>();
        //提取方法中的注解的参数，把方法上的注解拿到，得到一个二维数组，
        // 因为一个参数可以有多个注解，而一个方法又有多的参数
        Annotation[][] pa = gpHandlerMapping.getMethod().getParameterAnnotations();
        for (int i=0;i<pa.length;i++){
            for (Annotation a:pa[i]){
                if(a instanceof GPRequestParam){
                    String paramName = ((GPRequestParam)a).value();
                    if(!"".equals(paramName.trim())){
                        paramIndexMapping.put(paramName,i);
                    }
                }
            }
        }

        //提取方法中的request和response参数
        Class<?> [] paramsTypes  = gpHandlerMapping.getMethod().getParameterTypes();
        for(int i=0;i< paramsTypes.length;i++){
            Class<?> type = paramsTypes[i];
            if(type==HttpServletRequest.class || type==HttpServletResponse.class){
                paramIndexMapping.put(type.getName(),i);
            }
        }

        //1 参数获取参数值，并设置

        //2 modelandview返回


        return null;
    }
}
