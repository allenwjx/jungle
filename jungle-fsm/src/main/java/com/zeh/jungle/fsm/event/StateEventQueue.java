package com.zeh.jungle.fsm.event;

import com.zeh.jungle.fsm.actor.Actor;

import java.util.Collection;

/**
 * 事件队列
 * @author allen
 * @version $Id: StateEventQueue.java, v 0.1 2017年9月3日 下午2:56:53 allen Exp $
 */
public interface StateEventQueue<T, BizType, StateValue> extends EventMulticaster<T, BizType, StateValue>, StateEventRegistration<T, BizType, StateValue> {

    /**
     * 获取所有注册的事件监听器
     * 
     * @return 获取所有状态执行者
     */
    Collection<Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue>> getAllActors();

    /**
     * 获取所有注册支持该事件的事件监听器
     * 
     * @param event 状态事件
     * @return 状态事件关联的状态执行者
     */
    Collection<Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue>> getAllActors(StateEvent<T, BizType, StateValue> event);
}
