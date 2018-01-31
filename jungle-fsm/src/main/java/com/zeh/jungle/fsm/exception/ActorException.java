package com.zeh.jungle.fsm.exception;

import com.zeh.jungle.core.error.JGError;
import com.zeh.jungle.core.exception.JGException;

/**
 * 
 * @author allen
 * @version $Id: ActorException.java, v 0.1 2017年9月13日 下午4:31:47 allen Exp $
 */
public class ActorException extends JGException {

    /**  */
    private static final long serialVersionUID = 1L;

    /**
     * 构造器
     * 
     * @param error 错误信息
     * @param cause 异常
     */
    public ActorException(JGError error, Throwable cause) {
        super(error, cause);
    }

    /**
     * 构造器
     * 
     * @param error 错误信息
     */
    public ActorException(JGError error) {
        super(error);
    }

    /**
     * 构造器
     * 
     * @param cause 异常
     */
    public ActorException(Throwable cause) {
        super(cause);
    }

}
