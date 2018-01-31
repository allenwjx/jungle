package com.zeh.jungle.core.configuration;

/**
 * 实现该接口的类，将获取应用配置信息实例
 * 
 * @author allen
 * @version $Id: AppConfigurationAware.java, v 0.1 2016年2月28日 上午4:48:23 allen Exp $
 */
public interface AppConfigurationAware {
    /**
     * 设置应用配置信息
     * 
     * @param appConfiguration
     */
    void setAppConfiguration(AppConfiguration appConfiguration);
}
