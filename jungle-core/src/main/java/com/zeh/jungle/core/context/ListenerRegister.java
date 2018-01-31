package com.zeh.jungle.core.context;

import com.zeh.jungle.core.event.BaseEvent;
import com.zeh.jungle.core.event.BaseListener;

/**
 * 事件监听器注册接口，系统扫描容器中所有实现了{@link BaseListener}接口的实现类，并注册这些监听器，
 * 
 * @author allen
 * @version $Id: ListenerRegister.java, v 0.1 2016年2月28日 上午12:12:06 allen Exp $
 */
public interface ListenerRegister {

    /**
     * 注册容器中得监听器
     */
    void registerListener();

    /**
     * 注册监听器
     * 
     * @param listener 监听器
     */
    void registerListener(BaseListener<BaseEvent> listener);
}
