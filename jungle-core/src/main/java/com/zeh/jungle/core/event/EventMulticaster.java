package com.zeh.jungle.core.event;

import java.util.Collection;

/**
 * 事件广播接口，负责事件框架生命周期内的事件监听器注册、注销，事件的推送
 *  
 * @author allen
 * @version $Id: EventMulticaster.java, v 0.1 2016年2月27日 下午11:32:39 allen Exp $
 */
public interface EventMulticaster {

    /**
     * 注册事件监听器
     * 
     * @param listener 事件监听器
     */
    void addListener(BaseListener<BaseEvent> listener);

    /**
     * 注销事件监听器
     * 
     * @param listener 事件监听器
     */
    void removeListener(BaseListener<BaseEvent> listener);

    /**
     * 注销所有事件监听器
     */
    void removeAllListeners();

    /**
     * 获取所有注册的事件监听器
     * 
     * @return
     */
    Collection<BaseListener<BaseEvent>> getAllListeners();

    /**
     * 获取所有注册支持该事件的事件监听器
     * @param event
     * @return
     */
    Collection<BaseListener<BaseEvent>> getAllListeners(BaseEvent event);

    /**
     * 广播事件
     * 
     * @param event 事件
     * @param sync 同步或异步广播
     */
    void multicastEvent(BaseEvent event, boolean sync);
}
