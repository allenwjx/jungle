package com.zeh.jungle.fsm.event;

/**
 * 状态事件广播器，系统内通过状态事件
 * 
 * @author allen
 * @version $Id: EventMulticaster.java, v 0.1 2017年9月3日 下午3:01:10 allen Exp $
 */
public interface EventMulticaster<T, BizType, StateValue> {

    /**
     * 广播事件
     * 
     * @param event 事件
     */
    void multicast(StateEvent<T, BizType, StateValue> event);
}
