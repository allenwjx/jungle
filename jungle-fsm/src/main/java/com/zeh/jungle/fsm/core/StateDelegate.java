package com.zeh.jungle.fsm.core;

import com.zeh.jungle.fsm.event.StateEventQueue;
import com.zeh.jungle.fsm.exception.StateHandleException;

import java.util.Map;

/**
 * 状态委托代理
 * 
 * @author allen
 * @version $Id: StateDelegate.java, v 0.1 2017年9月3日 下午10:30:52 allen Exp $
 */
public interface StateDelegate<Model extends ModelWrapper<T, MBiz, MState>, T, MBiz, MState> {

    /**
     * 业务状态驱动
     * 
     * @param model         业务数据
     * @param toBiz         目标业务类型
     * @param toState       目标状态
     * @param properties    扩展属性
     * @throws StateHandleException 状态处理异常
     */
    void setState(Model model, MBiz toBiz, MState toState, Map<String, Object> properties) throws StateHandleException;

    /**
     * 业务状态驱动，同业务类型
     * 
     * @param model 业务数据
     * @param toState 目标状态
     * @throws StateHandleException 状态处理异常
     */
    void setState(Model model, MState toState) throws StateHandleException;

    /**
     * 设置状态机上下文工厂
     * 
     * @param stateContextFactory 状态机上下文工厂
     */
    void setStateContextFactory(StateContextFactory<T, MBiz, MState> stateContextFactory);

    /**
     * 设置状态持久化处理器
     * 
     * @param statePersister 状态持久化处理器
     */
    void setStatePersister(StatePersister<T, MBiz, MState> statePersister);

    /**
     * 设置状态事件队列
     * 
     * @param stateEventQueue 状态事件队列
     */
    void setStateEventQueue(StateEventQueue<T, MBiz, MState> stateEventQueue);

    /**
     * 获取状态事件队列
     * 
     * @return 状态事件队列
     */
    StateEventQueue<T, MBiz, MState> getStateEventQueue();
}
