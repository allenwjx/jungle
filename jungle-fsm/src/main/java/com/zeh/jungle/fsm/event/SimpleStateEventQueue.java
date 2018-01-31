package com.zeh.jungle.fsm.event;

import com.zeh.jungle.fsm.core.StateToString;

/**
 * @author allen
 * @create $ ID: SimpleStateEventQueue, 18/1/12 14:49 allen Exp $
 * @since 1.0.0
 */
public class SimpleStateEventQueue<T, BizType, StateValue> extends ConcurrentStateEventQueue<T, BizType, StateValue> {

    /** 状态转字符串处理器 */
    private StateToString<T, BizType, StateValue> stateToString;

    /** 状态有效校验器 */
    private ActorRegistryValidator                actorRegistryValidator;

    /**
     * 获取状态字符串转化器
     *
     * @return 状态字符串转化器
     */
    @Override
    protected StateToString<T, BizType, StateValue> getStateToString() {
        return stateToString;
    }

    /**
     * 获取Actor注册校验器
     *
     * @return Actor注册校验器
     */
    @Override
    protected ActorRegistryValidator getActorRegistryValidator() {
        return actorRegistryValidator;
    }

    /**
     * Setter for StateToString
     * 
     * @param stateToString state to string
     */
    public void setStateToString(StateToString<T, BizType, StateValue> stateToString) {
        this.stateToString = stateToString;
    }

    /**
     * Setter for ActorRegistryValidator
     * 
     * @param actorRegistryValidator actor registry validator
     */
    public void setActorRegistryValidator(ActorRegistryValidator actorRegistryValidator) {
        this.actorRegistryValidator = actorRegistryValidator;
    }
}
