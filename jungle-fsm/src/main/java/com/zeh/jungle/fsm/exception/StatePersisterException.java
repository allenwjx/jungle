package com.zeh.jungle.fsm.exception;

import com.zeh.jungle.core.error.JGError;
import com.zeh.jungle.core.exception.JGException;

/**
 * 状态持久化异常
 * @author llf32758
 * @version Id: StatePersisterException, v 0.1 2017/9/14 0:49 llf32758 Exp $
 */
public class StatePersisterException extends JGException {
    /**  */
    private static final long serialVersionUID = 1L;

    /**
     * 构造器
     * 
     * @param error 错误信息
     * @param cause 异常
     */
    public StatePersisterException(JGError error, Throwable cause) {
        super(error, cause);
    }

    /**
     * 构造器
     * 
     * @param error 错误信息
     */
    public StatePersisterException(JGError error) {
        super(error);
    }

    /**
     * 构造器
     * 
     * @param cause 异常
     */
    public StatePersisterException(Throwable cause) {
        super(cause);
    }

}
