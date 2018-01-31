package com.zeh.jungle.mq.exception;

import com.zeh.jungle.core.error.JGError;
import com.zeh.jungle.core.exception.JGException;

/**
 * 
 * @author allen
 * @version $Id: MQException.java, v 0.1 2016年3月2日 上午10:17:18 allen Exp $
 */
public class MQException extends JGException {

    /**  */
    private static final long serialVersionUID = 1L;

    /**
     * @param error
     * @param cause
     */
    public MQException(JGError error, Throwable cause) {
        super(error, cause);
    }

    /**
     * @param error
     */
    public MQException(JGError error) {
        super(error);
    }

    /**
     * @param cause
     */
    public MQException(Throwable cause) {
        super(cause);
    }

}
