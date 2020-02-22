package com.gupaoedu.vip.spring.formwork.webmvc;


import com.gupaoedu.vip.spring.formwork.annotation.GPController;
import com.gupaoedu.vip.spring.formwork.annotation.GPRequestMapping;
import com.gupaoedu.vip.spring.formwork.context.GPApplicationContext;
import com.gupaoedu.vip.spring.formwork.webmvc.servlet.*;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Handler;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GPDispatcherServlet extends HttpServlet {

    private final String CONTENT_CONFIG_LOCATION="contentConfigLocation";

    private GPApplicationContext context;

    private List<GPHandlerMapping> handleMappings= new ArrayList<>();

    private Map<GPHandlerMapping, GPHandlerAdapter> handlerAdapters = new ConcurrentHashMap<>();

    private List<GPViewResolver> viewResolvers = new ArrayList<>();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            this.doDispatcher(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        //1.初始化 ApplicationContext
        context = new GPApplicationContext(config.getInitParameter(CONTENT_CONFIG_LOCATION));

        //2 初始化springMvc九大组件


    }

    public void doDispatcher(HttpServletRequest request,HttpServletResponse response) throws Exception {
        //1.通过request 中拿到的URL 去匹配一个HandleMapping
        GPHandlerMapping handler = getHandler(request);
        if(handler==null)return ;
        GPHandlerAdapter ha = getHandlerAdapter(handler);
        //
        GPModelAndView mv = ha.handle(request,response,handler);
        processDispatchResult(request,response,mv);

    }
    //处理结果转化成html，json，freemarker ....
    private void processDispatchResult(HttpServletRequest request, HttpServletResponse response, GPModelAndView mv) throws Exception {
        if(this.viewResolvers.isEmpty() || mv==null)return ;
        for (GPViewResolver viewResolver:this.viewResolvers){
            GPView view = viewResolver.resolveViewName(mv.getViewName().toString(), Locale.SIMPLIFIED_CHINESE);
            view.render(mv.getModel(),request,response);
        }
    }

    private GPHandlerAdapter getHandlerAdapter(GPHandlerMapping handlerMapping) {
        if(this.handlerAdapters.isEmpty())return null;
        GPHandlerAdapter ha= this.handlerAdapters.get(handlerMapping);
        if(ha.supports(handlerMapping))return ha;
        return null;
    }

    private GPHandlerMapping getHandler(HttpServletRequest request) {
        if(this.handleMappings.isEmpty())return null;
        String url = request.getRequestURI();
        String context = request.getContextPath();
        url = url.replace(context,"").replaceAll("/+","/");
        for(GPHandlerMapping handler:this.handleMappings){
            Matcher matcher = handler.getPattern().matcher(url);
            if(!matcher.matches())continue;
            return handler;
        }

        return null;
    }


    protected void initStrategies(GPApplicationContext context) {
        //
        initMultipartResolver(context);
        //
        initLocaleResolver(context);
        //
        initThemeResolver(context);
        //*
        initHandlerMappings(context);
        //*
        initHandlerAdapters(context);
        //
        initHandlerExceptionResolvers(context);
        //
        initRequestToViewNameTranslator(context);
        //*
        initViewResolvers(context);
        //
        initFlashMapManager(context);
    }

    private void initFlashMapManager(GPApplicationContext context) {
    }

    private void initViewResolvers(GPApplicationContext context) {
    }

    private void initHandlerExceptionResolvers(GPApplicationContext context) {
    }

    private void initHandlerAdapters(GPApplicationContext context) {
        for(GPHandlerMapping gpHandlerMapping:this.handleMappings){
            this.handlerAdapters.put(gpHandlerMapping,new GPHandlerAdapter());
        }
    }

    private void initHandlerMappings(GPApplicationContext context) {
        String[] beanNames = context.getBeanDefinitionNames();
        for (String beanName:beanNames) {
            Object controller = context.getBean(beanName);
            Class<?> clazz = controller.getClass();
            if(!clazz.isAnnotationPresent(GPController.class))continue;
            String baseUrl="";
            if(clazz.isAnnotationPresent(GPRequestMapping.class)){
                GPRequestMapping requestMapping = clazz.getAnnotation(GPRequestMapping.class);
                baseUrl = requestMapping.value();
            }

            Method[] methods =clazz.getMethods();
            for(Method method: methods){
                if(!method.isAnnotationPresent(GPRequestMapping.class))continue;
                GPRequestMapping requestMapping = clazz.getAnnotation(GPRequestMapping.class);
                String regex= ("/"+baseUrl+"/"+requestMapping.value()).replaceAll("/+","/");
                Pattern pattern=Pattern.compile(regex);

                this.handleMappings.add(new GPHandlerMapping(pattern,controller,method));

            }

        }
    }

    private void initThemeResolver(GPApplicationContext context) {
    }

    private void initRequestToViewNameTranslator(GPApplicationContext context) {
    }

    private void initLocaleResolver(GPApplicationContext context) {
    }

    private void initMultipartResolver(GPApplicationContext context) {
    }
}
