package com.zeh.jungle.fsm.actor;

import com.zeh.jungle.fsm.event.StateEvent;

/**
 * @author allen
 * @create $ ID: AbstractPreActor, 18/1/12 14:09 allen Exp $
 * @since 1.0.0
 */
public abstract class AbstractPreActor<E extends StateEvent<T, BizType, StateValue>, T, BizType, StateValue> implements Actor<E, T, BizType, StateValue> {

    /**
     * Actor类型
     *
     * @return ActorType
     */
    @Override
    public ActorType getActorType() {
        return ActorType.PRE;
    }

    /**
     * 获取顺序
     *
     * @return 顺序值
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
