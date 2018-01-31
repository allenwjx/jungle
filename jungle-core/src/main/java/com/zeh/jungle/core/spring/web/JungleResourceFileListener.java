package com.zeh.jungle.core.spring.web;

import java.io.FileNotFoundException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.util.ServletContextPropertyUtils;
import org.springframework.web.util.WebUtils;

import com.alibaba.dubbo.common.Constants;

/**
 * 
 * @author allen
 * @version $Id: JungleResourceFileListener.java, v 0.1 2016年3月13日 下午2:38:30 allen Exp $
 */
public class JungleResourceFileListener implements ServletContextListener {
    /***/
    public static final String JUNGLE_CONF_LOCATION         = "jungleConfLocation";

    public static final String DEFAULT_JUNGLE_CONF_LOCATION = "WEB-INF/dubbo.properties";

    /** 
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String location = sce.getServletContext().getInitParameter(JUNGLE_CONF_LOCATION);
        if (StringUtils.isBlank(location)) {
            location = DEFAULT_JUNGLE_CONF_LOCATION;
        }

        try {
            location = ServletContextPropertyUtils.resolvePlaceholders(location, sce.getServletContext());

            if (!ResourceUtils.isUrl(location)) {
                location = WebUtils.getRealPath(sce.getServletContext(), location);

                String osName = System.getProperty("os.name");
                if (osName.toLowerCase().indexOf("windows") >= 0) {
                    location = "/" + location;
                }
            }

            System.setProperty(Constants.DUBBO_PROPERTIES_KEY, location);

            sce.getServletContext().log("Initializing jungle configuration from [" + location + "]");
        } catch (FileNotFoundException ex) {
            throw new IllegalArgumentException("Invalid 'jungleConfLocation' parameter: " + ex.getMessage());
        }
    }

    /** 
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // no-op
    }

}
