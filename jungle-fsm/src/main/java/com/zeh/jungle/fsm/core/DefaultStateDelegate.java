package com.zeh.jungle.fsm.core;

import java.util.Map;

import com.zeh.jungle.fsm.event.StateEventQueue;
import com.zeh.jungle.fsm.exception.StateHandleException;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author allen
 * @create $ ID: DefaultStateDelegate, 18/1/12 14:13 allen Exp $
 * @since 1.0.0
 */
public class DefaultStateDelegate<Model extends ModelWrapper<T, MBiz, MState>, T, MBiz, MState> implements StateDelegate<Model, T, MBiz, MState> {

    /** 状态上下文工厂 */
    private StateContextFactory<T, MBiz, MState> stateContextFactory;

    /** 状态持久化服务 */
    private StatePersister<T, MBiz, MState>      statePersister;

    /** 状态事件队列 */
    private StateEventQueue<T, MBiz, MState>     stateEventQueue;

    /**
     * 业务状态驱动
     *
     * @param model      业务数据
     * @param toBiz      目标业务类型
     * @param toState    目标状态
     * @param properties 扩展属性
     * @throws StateHandleException 状态处理异常
     */
    @Override
    public void setState(Model model, MBiz toBiz, MState toState, Map<String, Object> properties) throws StateHandleException {
        String id = model.getId();
        if (StringUtils.isBlank(id)) {
            throw new IllegalArgumentException("Model id cannot be null");
        }

        if (stateContextFactory == null) {
            stateContextFactory = new DefaultStateContextFactory<>();
        }

        StateContext<T, MBiz, MState> context = stateContextFactory.createContext(id, model.getModel());
        if (MapUtils.isNotEmpty(properties)) {
            context.getProperties().putAll(properties);
        }

        // create state
        IState<T, MBiz, MState> from = stateContextFactory.createState(model.getState(), model.getBizType(), statePersister, stateEventQueue);
        IState<T, MBiz, MState> to = stateContextFactory.createState(toState, toBiz, statePersister, stateEventQueue);

        // set order state
        context.setState(from, to);
    }

    /**
     * 业务状态驱动，相同业务类型
     *
     * @param model   业务数据
     * @param toState 目标状态
     * @throws StateHandleException 状态处理异常
     */
    @Override
    public void setState(Model model, MState toState) throws StateHandleException {
        // 原，目标业务类型相同
        setState(model, model.getBizType(), toState, null);
    }

    /**
     * Setter for stateContextFactory
     * 
     * @param stateContextFactory state context factory
     */
    @Override
    public void setStateContextFactory(StateContextFactory<T, MBiz, MState> stateContextFactory) {
        this.stateContextFactory = stateContextFactory;
    }

    /**
     * Setter for statePersister
     * 
     * @param statePersister state persister
     */
    @Override
    public void setStatePersister(StatePersister<T, MBiz, MState> statePersister) {
        this.statePersister = statePersister;
    }

    /**
     * Setter for stateEventQueue
     * 
     * @param stateEventQueue state event queue
     */
    @Override
    public void setStateEventQueue(StateEventQueue<T, MBiz, MState> stateEventQueue) {
        this.stateEventQueue = stateEventQueue;
    }

    /**
     * 获取状态事件队列
     *
     * @return 状态事件队列
     */
    @Override
    public StateEventQueue<T, MBiz, MState> getStateEventQueue() {
        return this.stateEventQueue;
    }
}
