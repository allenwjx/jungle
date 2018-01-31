package com.zeh.jungle.fsm.core;

import com.zeh.jungle.fsm.event.StateEventQueue;

/**
 * 状态上下文工厂
 * 
 * @author allen
 * @version $Id: StateContextFactory.java, v 0.1 2017年9月3日 下午10:33:16 allen Exp $
 */
public interface StateContextFactory<T, BizType, StateValue> {

    /**
     * 创建状态上下文
     * 
     * @param id 上下文ID
     * @param data 上下文业务数据
     * @return state context
     */
    StateContext<T, BizType, StateValue> createContext(String id, T data);

    /**
     * Create state
     * 
     * @param stateValue 状态
     * @param bizType 业务类型
     * @param statePersister 状态持久化服务
     * @param stateEventQueue 状态事件队列
     * @return state
     */
    IState<T, BizType, StateValue> createState(StateValue stateValue, BizType bizType, StatePersister<T, BizType, StateValue> statePersister,
                                               StateEventQueue<T, BizType, StateValue> stateEventQueue);
}
