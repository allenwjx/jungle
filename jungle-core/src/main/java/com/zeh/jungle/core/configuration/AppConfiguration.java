package com.zeh.jungle.core.configuration;

import java.util.Map;

/**
 * 应用配置信息
 * 
 * @author allen
 * @version $Id: AppConfiguration.java, v 0.1 2016年2月28日 上午4:27:14 allen Exp $
 */
public interface AppConfiguration {

    /** 系统主机名称 */
    String SYS_HOST_NAME       = "sys.host.name";

    /** 系统ip地址 */
    String SYS_IP              = "sys.ip";

    /** 应用名称 */
    String SYS_APP_NAME        = "app.name";

    /** 应用运行模式 */
    String SYS_RUN_MODE        = "sof-env";

    /** 消息命名服务器地址 */
    String MQ_NAME_SERVER_ADDR = "mq.namesrv.addr";

    /**
     * 获取应用配置信息
     *
     * @return 应用配置信息
     */
    Map<String, String> getConfig();

    /**
     * 获取系统参数
     * 
     * @param key 应用参数key
     * @return 应用参数值
     */
    String getPropertyValue(String key);

    /**
     * 获取系统参数，不存在则使用默认值
     *
     * @param key 应用参数key
     * @param defaultValue more值
     * @return 应用参数值
     */
    String getPropertyValue(String key, String defaultValue);

    /**
     * 添加配置信息
     * 
     * @param key 配置信息字段
     * @param value 配置信息值
     */
    void addProperty(String key, String value);

    /**
     * 获取应用名称
     * 
     * @return 应用名称
     */
    String getSysAppName();

    /**
     * 获取系统ip地址
     * 
     * @return 系统ip地址
     */
    String getSysIp();

    /**
     * 获取系统运行模式
     * 
     * @return 系统运行模式
     */
    String getSysRunMode();

    /**
     * 获取系统主机名称
     * 
     * @return 系统主机名称
     */
    String getSysHostName();
}
