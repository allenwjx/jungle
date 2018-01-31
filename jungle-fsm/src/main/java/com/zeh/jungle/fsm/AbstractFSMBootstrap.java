package com.zeh.jungle.fsm;

import java.util.Map;

import com.zeh.jungle.fsm.actor.Actor;
import com.zeh.jungle.fsm.annotation.State;
import com.zeh.jungle.fsm.annotation.StatesDelegate;
import com.zeh.jungle.fsm.core.StateDelegate;
import com.zeh.jungle.fsm.core.StatesManager;
import com.zeh.jungle.utils.common.AopTargetUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author allen
 * @create $ ID: AbstractFSMBootstrap, 18/1/15 13:42 allen Exp $
 * @since 1.0.0
 */
public abstract class AbstractFSMBootstrap implements FSMBootstrap {
    /** logger */
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    /** 状态机管理器 */
    private StatesManager  statesManager;

    /**
     * 状态机初始化、启动方法
     */
    @Override
    public void startup() {
        if (statesManager == null) {
            throw new IllegalStateException("StatesManager instance cannot be null");
        }
        // 注册状态机委托
        registerStateDelegates();
        // 注册状态执行器
        registerActors();
    }

    /**
     * Provider the state delegates collection that search from system
     * 
     * @return State delegate collection
     */
    protected abstract Map<String, StateDelegate> loadAllStateDelegates();

    /**
     * Provider the state actor collection that search from system
     * 
     * @return State actor collection
     */
    protected abstract Map<String, Actor> loadAllActors();

    /**
     * Register the state delegates
     */
    private void registerStateDelegates() {
        Map<String, StateDelegate> delegates = loadAllStateDelegates();
        if (MapUtils.isEmpty(delegates)) {
            LOGGER.warn("Cannot load state delegates, delegates is null");
            return;
        }
        for (StateDelegate delegate : delegates.values()) {
            StateDelegate proxy = (StateDelegate) AopTargetUtils.getTarget(delegate);
            StatesDelegate annotation = proxy.getClass().getDeclaredAnnotation(StatesDelegate.class);
            if (annotation == null) {
                throw new IllegalStateException("StatesDelegate annotation should be place on this class " + delegate.getClass().getName());
            }
            Class stateClazz = annotation.state();
            Class bizTypeClazz = annotation.bizType();
            String name = annotation.name();
            statesManager.register(name, stateClazz, bizTypeClazz, delegate);
        }
    }

    /**
     * Register state actors
     */
    private void registerActors() {
        Map<String, Actor> actors = loadAllActors();
        if (MapUtils.isEmpty(actors)) {
            LOGGER.warn("Cannot load state actors, actors is null");
            return;
        }
        for (Actor actor : actors.values()) {
            Actor proxy = (Actor) AopTargetUtils.getTarget(actor);
            State anno = proxy.getClass().getDeclaredAnnotation(State.class);
            if (anno == null) {
                throw new IllegalStateException("State annotation should be place on this class " + actor.getClass().getName());
            }

            // 将Actor注册至匹配的状态事件容器中
            String name = anno.delegate();
            StateDelegate delegate = statesManager.getDelegateByName(name);
            delegate.getStateEventQueue().register(actor);
        }
    }

    /**
     * The setter for StatesManager
     * 
     * @param statesManager 状态机管理器
     */
    public void setStatesManager(StatesManager statesManager) {
        this.statesManager = statesManager;
    }

}
