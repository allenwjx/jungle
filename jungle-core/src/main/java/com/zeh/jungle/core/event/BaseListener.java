package com.zeh.jungle.core.event;

import java.util.EventListener;

/**
 * 事件监听器
 * 
 * @author allen
 * @version $Id: BaseListener.java, v 0.1 2016年2月27日 下午11:29:40 allen Exp $
 */
public interface BaseListener<E extends BaseEvent> extends EventListener {

    /**
     * 事件处理接口
     * 
     * @param event 事件
     */
    void onEvent(E event);

    /**
     * 是否支持处理该事件类型
     * 
     * @param event 事件
     * @return
     */
    boolean isSupportEvent(BaseEvent event);
}
