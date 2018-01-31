package com.zeh.jungle.fsm.core;

import com.zeh.jungle.core.error.AbstractErrorFactory;
import com.zeh.jungle.core.error.JGError;
import com.zeh.jungle.core.exception.JGException;
import com.zeh.jungle.core.exception.JGRuntimeException;
import com.zeh.jungle.fsm.FSMConstants;
import com.zeh.jungle.fsm.event.PostTransitEvent;
import com.zeh.jungle.fsm.event.PreTransitEvent;
import com.zeh.jungle.fsm.event.StateEventQueue;
import com.zeh.jungle.fsm.exception.StateHandleException;
import com.zeh.jungle.utils.common.ThrowableAnalyzer;
import org.apache.commons.lang3.StringUtils;

/**
 * 缺省状态实现
 * 
 * @author allen
 * @version $Id: DefaultState.java, v 0.1 2017年9月3日 下午8:27:56 allen Exp $
 */
public class DefaultState<T, BizType, StateValue> implements IState<T, BizType, StateValue> {

    /** 状态值 */
    private StateValue                              stateValue;

    /** 业务类型 */
    private BizType                                 bizType;

    /** StatePersister */
    private StatePersister<T, BizType, StateValue>  statePersister;

    /** State event queue */
    private StateEventQueue<T, BizType, StateValue> stateEventQueue;

    /** The Throwable analyzer */
    private final ThrowableAnalyzer                 throwableAnalyzer = new ThrowableAnalyzer();

    /**
     * @param stateValue 状态值
     * @param bizType 业务类型
     * @param statePersister 状态持久化服务
     * @param stateEventQueue 状态事件队列
     */
    DefaultState(StateValue stateValue, BizType bizType, StatePersister<T, BizType, StateValue> statePersister, StateEventQueue<T, BizType, StateValue> stateEventQueue) {
        super();
        this.stateValue = stateValue;
        this.bizType = bizType;
        this.statePersister = statePersister;
        this.stateEventQueue = stateEventQueue;
    }

    /** 
     * @see com.zeh.jungle.fsm.core.IState#getStateValue()
     */
    @Override
    public StateValue getStateValue() {
        return stateValue;
    }

    /** 
     * @see com.zeh.jungle.fsm.core.IState#handle(com.zeh.jungle.fsm.core.StateContext)
     */
    @Override
    public void handle(StateContext<T, BizType, StateValue> context) throws StateHandleException {
        T data = context.getData();
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }

        try {
            // 触发状态变更前事件
            PreTransitEvent<T, BizType, StateValue> preEvent = new PreTransitEvent<>(this, context);
            stateEventQueue.multicast(preEvent);

            // persist update state
            statePersister.updateState(data, bizType, stateValue);

            // 触发状态变更后事件
            PostTransitEvent<T, BizType, StateValue> postEvent = new PostTransitEvent<>(this, context);
            stateEventQueue.multicast(postEvent);
        } catch (Exception e) {
            JGError error = resolveLYError(e);
            throw new StateHandleException(context.getId(), error, e);
        }
    }

    /** 
     * @see com.zeh.jungle.fsm.core.IState#getBizType()
     */
    @Override
    public BizType getBizType() {
        return bizType;
    }

    /**
     * 解析异常
     *
     * @param e 异常
     * @return 异常
     */
    private JGError resolveLYError(Exception e) {
        Throwable[] causeChain = throwableAnalyzer.determineCauseChain(e);
        JGException lye = (JGException) throwableAnalyzer.getFirstThrowableOfType(JGException.class, causeChain);
        JGRuntimeException lyre = (JGRuntimeException) throwableAnalyzer.getFirstThrowableOfType(JGRuntimeException.class, causeChain);
        String code = FSMConstants.FSM_ERROR;
        String msg;
        if (lye != null) {
            msg = lye.getMessage();
            code = lye.getErrorCode();
        } else if (lyre != null) {
            msg = lyre.getMessage();
            code = lyre.getErrorCode();
        } else if (StringUtils.isNotBlank(e.getMessage())) {
            msg = e.getMessage();
        } else {
            msg = "状态机处理异常, 异常类型: " + e.getClass().getName();
        }

        return AbstractErrorFactory.createStaticError(msg, code);
    }
}
