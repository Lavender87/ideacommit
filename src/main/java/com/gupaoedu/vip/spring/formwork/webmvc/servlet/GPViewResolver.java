package com.gupaoedu.vip.spring.formwork.webmvc.servlet;

import java.io.File;
import java.util.Locale;

public class GPViewResolver {

    private final String DEFAULT_TEMPLATE_SUFFIX = ".html";

    private File templateRootDir;

    public GPViewResolver(String templateRoot) {
        String templateRootPath = this.getClass().getClassLoader().getResource("templateRoot").getFile();
        templateRootDir =new File(templateRootPath);
    }

    public GPView resolveViewName(String viewName, Locale locale) throws Exception {
        if(viewName==null ||"".equals(viewName) )return null;
        viewName = viewName.endsWith(DEFAULT_TEMPLATE_SUFFIX) ? viewName :(viewName+DEFAULT_TEMPLATE_SUFFIX);
        File templateFile = new File((templateRootDir.getPath()+"/"+viewName).replaceAll("/*","/"));
        return new GPView(templateFile);
    }
}
