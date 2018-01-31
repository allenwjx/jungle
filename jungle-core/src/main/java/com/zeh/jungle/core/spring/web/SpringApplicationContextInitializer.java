package com.zeh.jungle.core.spring.web;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * 
 * @author allen
 * @version $Id: SpringApplicationContextInitializer.java, v 0.1 2017年3月22日 下午2:46:13 allen Exp $
 */
public class SpringApplicationContextInitializer implements ApplicationContextInitializer<XmlWebApplicationContext> {

    /** 
     * @see ApplicationContextInitializer#initialize(org.springframework.context.ConfigurableApplicationContext)
     */
    @Override
    public void initialize(XmlWebApplicationContext applicationContext) {
        applicationContext.setAllowBeanDefinitionOverriding(false);
    }

}
