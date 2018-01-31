package com.zeh.jungle.fsm.core;

import com.zeh.jungle.fsm.exception.StateHandleException;

import java.util.Map;

/**
 * 状态上下文
 * 
 * @author allen
 * @version $Id: StateContext.java, v 0.1 2017年9月3日 下午8:23:13 allen Exp $
 */
public interface StateContext<T, BizType, StateValue> {

    /**
     * 获取上下文ID
     * 
     * @return 上下文ID
     */
    String getId();

    /**
     * 获取原业务类型
     * 
     * @return 原业务类型
     */
    BizType getFromBiz();

    /**
     * 获取目标业务类型
     * 
     * @return 目标业务类型
     */
    BizType getToBiz();

    /**
     * 获取当前状态
     * 
     * @return 原状态
     */
    IState<T, BizType, StateValue> getFromState();

    /**
     * 获取要流转到的状态
     * 
     * @return 目标状态
     */
    IState<T, BizType, StateValue> getToState();

    /**
     * 状态设置
     * 
     * @param from 原业务状态
     * @param to 目标业务状态
     * @throws StateHandleException 状态处理异常
     */
    void setState(IState<T, BizType, StateValue> from, IState<T, BizType, StateValue> to) throws StateHandleException;

    /**
     * 获取业务数据
     * 
     * @return 业务数据
     */
    T getData();

    /**
     * 获取扩展参数
     *
     * @return 上下文扩展数据
     */
    Map<String, Object> getProperties();

    /**
     * 添加扩展参数
     *
     * @param key 扩展数据KEY
     * @param value 扩展数据
     */
    void addProperty(String key, Object value);
}
