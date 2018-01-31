package com.zeh.jungle.fsm.event;

import java.util.EventObject;

import com.zeh.jungle.fsm.core.IState;
import com.zeh.jungle.fsm.core.StateContext;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 状态事件
 * 
 * @author allen
 * @version $Id: StateEvent.java, v 0.1 2017年9月3日 下午2:38:22 allen Exp $
 */
public abstract class StateEvent<T, BizType, StateValue> extends EventObject {
    /**  */
    private static final long                    serialVersionUID = 1L;

    /** 事件id */
    private final String                         id;

    /** 事件产生时间 */
    private final long                           timestamp;

    /** 状态上下文 */
    private StateContext<T, BizType, StateValue> context;

    /**
     * @param source 事件源
     * @param id 事件ID
     * @param context 状态上下文
     */
    public StateEvent(Object source, String id, StateContext<T, BizType, StateValue> context) {
        super(source);
        this.id = id;
        this.context = context;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Getter method for property <tt>id</tt>.
     * 
     * @return property value of id
     */
    public String getId() {
        return id;
    }

    /**
     * Getter method for property <tt>timestamp</tt>.
     * 
     * @return property value of timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Getter method for property <tt>context</tt>.
     * 
     * @return property value of context
     */
    public StateContext<T, BizType, StateValue> getContext() {
        return context;
    }

    /**
     * Get data
     * 
     * @return
     */
    public T getData() {
        return context.getData();
    }

    /**
     * Get original state
     * 
     * @return
     */
    public IState<T, BizType, StateValue> getFromState() {
        return context.getFromState();
    }

    /**
     * Get target state
     * 
     * @return
     */
    public IState<T, BizType, StateValue> getToState() {
        return context.getToState();
    }

    /**
     * Get original biz
     * 
     * @return
     */
    public BizType getFromBiz() {
        return context.getFromBiz();
    }

    /**
     * Get target biz
     * 
     * @return
     */
    public BizType getToBiz() {
        return context.getToBiz();
    }

    /** 
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
