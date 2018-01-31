package com.zeh.jungle.core.context;

import com.zeh.jungle.core.configuration.AppConfiguration;
import com.zeh.jungle.core.event.BaseEvent;
import com.zeh.jungle.core.event.EventMulticaster;

/**
 * 容器上下文
 * 
 * @author allen
 * @version $Id: JungleContext.java, v 0.1 2016年2月26日 下午5:16:55 allen Exp $
 */
public interface JungleContext extends ListenerRegister {

    /**
     * 获取容器ID
     * 
     * @return
     */
    String getId();

    /**
     * 获取应用名称
     * 
     * @return 应用名称
     */
    String getAppName();

    /**
     * 获取容器运行时名称
     * 
     * @return 容器运行时名称
     */
    String getContextName();

    /**
     * 获取容器启动时间
     * 
     * @return 容器启动时间
     */
    long getStartupDate();

    /**
     * 容器启动
     */
    boolean init();

    /**
     * 容器关闭
     */
    void shutdown();

    /**
     * 容器是否运行
     * 
     * @return
     */
    boolean isRunning();

    /**
     * 事件投递
     * 
     * @param event 事件
     * @param sync 是否同步消息
     */
    void fireEvent(BaseEvent event, boolean sync);

    /**
     * 获取系统注册的事件广播器
     * 
     * @return 事件广播器
     */
    EventMulticaster getEventMulticaster();

    /**
     * 设置事件广播器
     * 
     * @param eventMulticaster 事件广播器
     */
    void setEventMulticaster(EventMulticaster eventMulticaster);

    /**
     * 获取应用配置
     * 
     * @return 应用配置
     */
    AppConfiguration getAppConfiguration();
}
