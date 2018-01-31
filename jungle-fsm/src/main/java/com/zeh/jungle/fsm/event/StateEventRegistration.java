package com.zeh.jungle.fsm.event;

import com.zeh.jungle.fsm.actor.Actor;

/**
 * 状态事件监听器注册
 * 
 * @author allen
 * @version $Id: StateEventRegistration.java, v 0.1 2017年9月3日 下午3:02:18 allen Exp $
 */
public interface StateEventRegistration<T, BizType, StateValue> {

    /**
     * 注册事件监听器
     * 
     * @param actor 事件监听器
     */
    void register(Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue> actor);

    /**
     * 注销事件监听器
     * 
     * @param actor 事件监听器
     */
    void unregister(Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue> actor);

    /**
     * 注销所有事件监听器
     */
    void unregisterAll();
}
