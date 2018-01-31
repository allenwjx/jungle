package com.zeh.jungle.fsm.core;

import com.zeh.jungle.fsm.exception.StateHandleException;

/**
 * 状态
 * 
 * @author allen
 * @version $Id: IState.java, v 0.1 2017年9月3日 下午8:21:43 allen Exp $
 */
public interface IState<T, BizType, StateValue> {

    /**
     * 获取状态
     * 
     * @return 状态值
     */
    StateValue getStateValue();

    /**
     * 获取业务类型
     * 
     * @return 状态关联的业务类型
     */
    BizType getBizType();

    /**
     * 当业务状态变更时，处理相关业务逻辑
     * 
     * @param context 状态上下文
     */
    void handle(StateContext<T, BizType, StateValue> context) throws StateHandleException;
}
