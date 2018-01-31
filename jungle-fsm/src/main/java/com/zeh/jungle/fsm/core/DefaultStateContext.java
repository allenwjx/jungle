package com.zeh.jungle.fsm.core;

import java.util.Map;

import com.zeh.jungle.fsm.exception.StateHandleException;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;

/**
 * 默认状态上下文实现
 * 
 * @author allen
 * @version $Id: DefaultStateContext.java, v 0.1 2017年9月3日 下午8:25:12 allen Exp $
 */
public class DefaultStateContext<T, BizType, StateValue> implements StateContext<T, BizType, StateValue> {

    /** 状态上下文ID */
    private String                         id;

    /** 当前状态 */
    private IState<T, BizType, StateValue> fromState;

    /** 流转状态 */
    private IState<T, BizType, StateValue> toState;

    /** 业务数据 */
    private T                              data;

    /** 上下文属性 */
    private Map<String, Object>            properties = Maps.newHashMap();

    /**
     * 
     * @param id 上下文ID
     * @param data 业务数据
     */
    DefaultStateContext(String id, T data) {
        super();
        this.id = id;
        this.data = data;
    }

    /**
     * @see com.zeh.jungle.fsm.core.StateContext#setState(com.zeh.jungle.fsm.core.IState, com.zeh.jungle.fsm.core.IState)
     */
    @Override
    public void setState(IState<T, BizType, StateValue> from, IState<T, BizType, StateValue> to) throws StateHandleException {
        this.fromState = from;
        this.toState = to;
        toState.handle(this);
    }

    /** 
     * @see com.zeh.jungle.fsm.core.StateContext#getFromBiz()
     */
    @Override
    public BizType getFromBiz() {
        return fromState.getBizType();
    }

    /** 
     * @see com.zeh.jungle.fsm.core.StateContext#getToBiz()
     */
    @Override
    public BizType getToBiz() {
        return toState.getBizType();
    }

    /** 
     * @see com.zeh.jungle.fsm.core.StateContext#getFromState()
     */
    @Override
    public IState<T, BizType, StateValue> getFromState() {
        return fromState;
    }

    /** 
     * @see com.zeh.jungle.fsm.core.StateContext#getToState()
     */
    @Override
    public IState<T, BizType, StateValue> getToState() {
        return toState;
    }

    /**
     * @see com.zeh.jungle.fsm.core.StateContext#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * @see com.zeh.jungle.fsm.core.StateContext#getData()
     */
    @Override
    public T getData() {
        return data;
    }

    /**
     * @see com.zeh.jungle.fsm.core.StateContext#getProperties()
     */
    @Override
    public Map<String, Object> getProperties() {
        if (MapUtils.isEmpty(properties)) {
            properties = Maps.newHashMap();
        }
        return properties;
    }

    /**
     * @see com.zeh.jungle.fsm.core.StateContext#addProperty(String, Object)
     */
    @Override
    public void addProperty(String key, Object value) {
        if (StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        getProperties().put(key, value);
    }
}
