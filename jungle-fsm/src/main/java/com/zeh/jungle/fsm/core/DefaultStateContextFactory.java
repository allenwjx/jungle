package com.zeh.jungle.fsm.core;

import com.zeh.jungle.fsm.event.StateEventQueue;

/**
 * 缺省状态上下文工厂
 * 
 * @author allen
 * @version $Id: DefaultStateContextFactory.java, v 0.1 2017年9月3日 下午10:34:45 allen Exp $
 */
public class DefaultStateContextFactory<T, BizType, StateValue> implements StateContextFactory<T, BizType, StateValue> {

    /** 
     * @see com.zeh.jungle.fsm.core.StateContextFactory#createContext(String, Object)
     */
    @Override
    public StateContext<T, BizType, StateValue> createContext(String id, T data) {
        return new DefaultStateContext<>(id, data);
    }

    /** 
     * @see com.zeh.jungle.fsm.core.StateContextFactory#createState(Object, Object, com.zeh.jungle.fsm.core.StatePersister, com.zeh.jungle.fsm.event.StateEventQueue)
     */
    @Override
    public IState<T, BizType, StateValue> createState(StateValue stateValue, BizType bizType, StatePersister<T, BizType, StateValue> statePersister,
                                                      StateEventQueue<T, BizType, StateValue> stateEventQueue) {
        return new DefaultState<>(stateValue, bizType, statePersister, stateEventQueue);
    }

}
