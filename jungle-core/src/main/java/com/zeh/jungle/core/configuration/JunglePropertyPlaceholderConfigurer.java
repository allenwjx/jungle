package com.zeh.jungle.core.configuration;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import com.zeh.jungle.core.context.JungleContext;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * 重载PropertyPlaceholderConfigurer加载资源文件方法，默认PropertyPlaceholderConfigurer从资源文件中读取配置信息,
 * 框架在启动时，默认已经从dubbo.properties文件中加载了配置信息，应用无需再从资源中读取配置信息，
 * 通过AppConfiguration来获取配置信息
 *
 * @author allen
 * @version $Id: JunglePropertyPlaceholderConfigurer.java, v 0.1 2016年2月29日 下午1:31:14 allen Exp $
 */
public class JunglePropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    /** Jungle上下文 */
    private JungleContext context;

    /**
     * 从{@link AppConfiguration}中加载配置信息
     * 
     * @see org.springframework.core.io.support.PropertiesLoaderSupport#mergeProperties()
     */
    @Override
    protected Properties mergeProperties() throws IOException {
        Map<String, String> conf = context.getAppConfiguration().getConfig();
        Properties props = new Properties();
        for (Map.Entry<String, String> prop : conf.entrySet()) {
            props.setProperty(prop.getKey(), prop.getValue());
        }
        return props;
    }

    public void setContext(JungleContext context) {
        this.context = context;
    }

}
