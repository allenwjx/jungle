package com.zeh.jungle.fsm.core;

/**
 * 状态字符串转化器
 * 
 * @author allen
 * @version $Id: StateToString.java, v 0.1 2017年9月3日 下午9:38:46 allen Exp $
 */
public interface StateToString<T, BizType, StateValue> {

    /**
     * State to String
     * 
     * @param state 状态
     * @return 状态字符串描述
     */
    String stateToString(IState<T, BizType, StateValue> state);

    /**
     * transfer biz type to string
     * 
     * @param biz 业务类型
     * @return 业务字符串描述
     */
    String bizToString(BizType biz);
}
