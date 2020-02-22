package com.gupaoedu.vip.spring.formwork.webmvc.servlet;

import java.util.Map;

public class GPModelAndView {

    private Object viewName;

    private Map<String,?> model;

    public GPModelAndView(Object viewName) {
        this.viewName = viewName;
    }

    public GPModelAndView(Object viewName, Map<String, ?> model) {
        this.viewName = viewName;
        this.model = model;
    }


    public Object getViewName() {
        return viewName;
    }

    /*public void setViewName(Object viewName) {
        this.viewName = viewName;
    }*/

    public Map<String, ?> getModel() {
        return model;
    }

  /*  public void setModel(Map<String, ?> model) {
        this.model = model;
    }*/
}
