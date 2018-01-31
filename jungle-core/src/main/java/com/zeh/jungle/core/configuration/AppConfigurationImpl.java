package com.zeh.jungle.core.configuration;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author allen
 * @version $Id: AppConfigurationImpl.java, v 0.1 2016年2月28日 上午4:33:53 allen Exp $
 */
public class AppConfigurationImpl implements AppConfiguration {
    /** 系统变量的前缀 */
    private static final String SYSTEM_PROPERTY_PREFIX = "__jungle__";

    /** 系统内部配置 */
    private Map<String, String> internalConfig         = new LinkedHashMap<String, String>();

    /** 应用配置 */
    private Map<String, String> appConfig              = new LinkedHashMap<String, String>();

    /**
     * 构造方法
     */
    public AppConfigurationImpl() {
        loadSystemConfig();
    }

    /**
     * 构造方法
     * 
     * @param appConfig
     */
    public AppConfigurationImpl(Map<String, String> appConfig) {
        this.appConfig = appConfig;
        loadSystemConfig();
    }

    /** 
     * @see com.zeh.jungle.core.configuration.AppConfiguration#getConfig()
     */
    @Override
    public Map<String, String> getConfig() {
        // 对map进行clone，保证map的安全性
        Map<String, String> map = new HashMap<String, String>();
        map.putAll(internalConfig);
        map.putAll(appConfig);
        return map;
    }

    /** 
     * @see com.zeh.jungle.core.configuration.AppConfiguration#getPropertyValue(String)
     */
    @Override
    public String getPropertyValue(String key) {
        String value = appConfig.get(key);
        if (StringUtils.isBlank(value)) {
            value = internalConfig.get(key);
        }
        return value;
    }

    /** 
     * @see com.zeh.jungle.core.configuration.AppConfiguration#getPropertyValue(String, String)
     */
    @Override
    public String getPropertyValue(String key, String defaultValue) {
        String value = getPropertyValue(key);
        if (StringUtils.isBlank(value)) {
            value = defaultValue;
        }
        return value;
    }

    /**
     * @see com.zeh.jungle.core.configuration.AppConfiguration#addProperty(String, String)
     */
    @Override
    public void addProperty(String key, String value) {
        appConfig.put(key, value);
    }

    /** 
     * @see com.zeh.jungle.core.configuration.AppConfiguration#getSysAppName()
     */
    @Override
    public String getSysAppName() {
        return getPropertyValue(SYS_APP_NAME);
    }

    /** 
     * @see com.zeh.jungle.core.configuration.AppConfiguration#getSysIp()
     */
    @Override
    public String getSysIp() {
        return getPropertyValue(SYS_IP);
    }

    /** 
     * @see com.zeh.jungle.core.configuration.AppConfiguration#getSysRunMode()
     */
    @Override
    public String getSysRunMode() {
        return getPropertyValue(SYS_RUN_MODE);
    }

    /** 
     * @see com.zeh.jungle.core.configuration.AppConfiguration#getSysHostName()
     */
    @Override
    public String getSysHostName() {
        return getPropertyValue(SYS_HOST_NAME);
    }

    /**
     * 加载系统配置
     */
    private void loadSystemConfig() {
        Properties properties = System.getProperties();
        if (properties == null) {
            return;
        }

        for (Object key : properties.keySet()) {
            String name = key.toString().trim();
            if (name.length() == 0) {
                continue;
            }

            if (name.startsWith(SYSTEM_PROPERTY_PREFIX)) {
                String value = properties.get(key).toString().trim();
                String shortKey = name.substring(SYSTEM_PROPERTY_PREFIX.length());
                internalConfig.put(shortKey, value);
            }
        }
    }

}
