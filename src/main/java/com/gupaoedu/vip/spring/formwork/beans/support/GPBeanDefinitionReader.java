package com.gupaoedu.vip.spring.formwork.beans.support;

import com.gupaoedu.vip.spring.formwork.beans.config.GPBeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class GPBeanDefinitionReader {

    private List<String> registryBeanClasses = new ArrayList<>();

    private Properties config = new Properties() ;

    private static final String SACN_PACKAGE="scanpackage";

    /**
     * 通过URL定位找到期所对应的文件，然后转换文件流
     * @param configLocations
     */
    public GPBeanDefinitionReader(String[] configLocations) {
        InputStream is = this.getClass().getClassLoader()
                .getResourceAsStream(configLocations[0].replace("classpath",""));
        try {
            config.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        doScan(config.getProperty(SACN_PACKAGE));
    }

    private void doScan(String scanpackage) {
        URL url  = this.getClass().getClassLoader().getResource("/"+scanpackage.replaceAll("\\.","/"));
        File classPath = new File(url.getFile());
        for (File file :  classPath.listFiles()) {
            if(file.isAbsolute()){
                doScan(scanpackage+"."+file.getName());
            }else{
                if(!file.getName().endsWith(".class")){continue;}
                String className = (scanpackage+"."+file.getName()).replace(".class","");
                registryBeanClasses.add(className);
            }
        }

    }

    public Properties getConfig(){
        return this.config;
    }

    public List<GPBeanDefinition> loadBeanDefinitions(){
        List<GPBeanDefinition> result = new ArrayList<>();
        for (String className: registryBeanClasses) {
            GPBeanDefinition gpBeanDefinition = doCreateBeanDefinition(className);
            if(gpBeanDefinition==null)continue;
            result.add(gpBeanDefinition);

        }
        return result;
    }

    /**
     * 配置信息解析
     * @param className
     * @return
     */
    private GPBeanDefinition doCreateBeanDefinition(String className) {
        try {

            Class<?> beanClass = Class.forName(className);
            if(beanClass.isInterface())return null;

            GPBeanDefinition gpBeanDefinition = new GPBeanDefinition();
            gpBeanDefinition.setBeanClassName(className);
            gpBeanDefinition.setFactoryBeanName(beanClass.getSimpleName());
            return gpBeanDefinition;


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
