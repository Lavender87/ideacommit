package com.gupaoedu.vip.spring.demo.action;

import com.gupaoedu.vip.spring.demo.service.IQueryservice;
import com.gupaoedu.vip.spring.formwork.annotation.GPAutowried;
import com.gupaoedu.vip.spring.formwork.annotation.GPController;
import com.gupaoedu.vip.spring.formwork.annotation.GPRequestMapping;
import com.gupaoedu.vip.spring.formwork.annotation.GPRequestParam;
import com.gupaoedu.vip.spring.formwork.webmvc.servlet.GPModelAndView;
import com.sun.deploy.net.HttpResponse;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@GPController
@GPRequestMapping("/web")
public class MyAction {

    @GPAutowried
    IQueryservice queryservice;

    @GPRequestMapping("/query*.json")
    public void query(HttpServletRequest request, HttpResponse response,
                      @GPRequestParam("name")String name){

    }


    @GPRequestMapping("/add*.json")
    public GPModelAndView add(HttpServletRequest request, HttpResponse response,
                              @GPRequestParam("name")String name,@GPRequestParam("addr")String addr){

        Map<String,Object> resultMap = new HashMap<>();
        try{
            String result= name + ":"+ addr;
            resultMap.put("result",result);

            return new GPModelAndView("resultMap",resultMap);
        }catch (Exception e){
//            resultMap.put("detail",e.getCause().getMessage());
//            resultMap.put("staceTrace",e.getCause().getStackTrace());

            resultMap.put("detail",e.getMessage());
            resultMap.put("staceTrace", Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]]",""));

        }

        return new GPModelAndView("500",resultMap);


    }

}
