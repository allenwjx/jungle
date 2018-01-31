package com.zeh.jungle.fsm;

import com.zeh.jungle.fsm.actor.Actor;
import com.zeh.jungle.fsm.core.StateDelegate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * @author allen
 * @create $ ID: SpringFSMBootstrap, 18/1/15 16:24 allen Exp $
 * @since 1.0.0
 */
public class SpringFSMBootstrap extends AbstractFSMBootstrap implements ApplicationContextAware {

    /** Spring context */
    private ApplicationContext springContext;

    /**
     * Provider the state delegates collection that search from system
     *
     * @return State delegate collection
     */
    @Override
    protected Map<String, StateDelegate> loadAllStateDelegates() {
        return springContext.getBeansOfType(StateDelegate.class, false, true);
    }

    /**
     * Provider the state actor collection that search from system
     *
     * @return State actor collection
     */
    @Override
    protected Map<String, Actor> loadAllActors() {
        return springContext.getBeansOfType(Actor.class, false, true);
    }

    /**
     * Set the spring context
     *
     * @param applicationContext spring context
     * @throws BeansException bean exception
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.springContext = applicationContext;
    }
}
