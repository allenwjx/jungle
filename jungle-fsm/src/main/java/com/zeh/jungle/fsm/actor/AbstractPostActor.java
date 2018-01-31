package com.zeh.jungle.fsm.actor;

import com.zeh.jungle.fsm.event.StateEvent;

/**
 * @author allen
 * @create $ ID: AbstractPostActor, 18/1/12 14:08 allen Exp $
 * @since 1.0.0
 */
public abstract class AbstractPostActor<E extends StateEvent<T, BizType, StateValue>, T, BizType, StateValue> implements Actor<E, T, BizType, StateValue> {

    /**
     * Actor类型
     *
     * @return Actor type
     */
    @Override
    public ActorType getActorType() {
        return ActorType.POST;
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
