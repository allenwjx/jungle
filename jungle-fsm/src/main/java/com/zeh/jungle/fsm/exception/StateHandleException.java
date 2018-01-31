package com.zeh.jungle.fsm.exception;

import com.zeh.jungle.core.error.JGError;
import com.zeh.jungle.core.exception.JGException;

/**
 * 状态处理异常
 * 
 * @author allen
 * @version $Id: StateHandleException.java, v 0.1 2017年9月3日 下午6:04:00 allen Exp $
 */
public class StateHandleException extends JGException {
    /**  */
    private static final long serialVersionUID = 1L;
    /** ID */
    private String            id;

    /**
     * 构造器
     * 
     * @param error 错误信息
     * @param cause 异常
     */
    public StateHandleException(JGError error, Throwable cause) {
        super(error, cause);
    }

    /**
     * 构造器
     * 
     * @param error 错误信息
     */
    public StateHandleException(JGError error) {
        super(error);
    }

    /**
     * 构造器
     * 
     * @param cause 异常
     */
    public StateHandleException(Throwable cause) {
        super(cause);
    }

    /**
     * 构造器
     * 
     * @param id ID
     * @param error 错误信息
     * @param cause 异常
     */
    public StateHandleException(String id, JGError error, Throwable cause) {
        super(error, cause);
        this.id = id;
    }

    /**
     * 构造器
     * 
     * @param id ID
     * @param error 错误信息
     */
    public StateHandleException(String id, JGError error) {
        super(error);
        this.id = id;
    }

    /**
     * 构造器
     * 
     * @param id ID
     * @param cause 异常
     */
    public StateHandleException(String id, Throwable cause) {
        super(cause);
        this.id = id;
    }

    /**
     * Getter method for property <tt>id</tt>.
     * 
     * @return property value of id
     */
    public String getId() {
        return id;
    }

}
