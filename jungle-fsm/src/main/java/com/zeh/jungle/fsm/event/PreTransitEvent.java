package com.zeh.jungle.fsm.event;

import com.zeh.jungle.fsm.core.StateContext;

/**
 * 状态变更前事件
 * 
 * @author allen
 * @version $Id: BeginTransitionEvent.java, v 0.1 2017年9月3日 下午9:59:29 allen Exp $
 */
public class PreTransitEvent<T, BizType, StateValue> extends StateEvent<T, BizType, StateValue> {

    /**  */
    private static final long serialVersionUID = 1L;

    /**
     * @param source 事件触发源
     * @param context 状态上下文
     */
    public PreTransitEvent(Object source, StateContext<T, BizType, StateValue> context) {
        super(source, "BEGIN-TRANSIT", context);
    }

}
