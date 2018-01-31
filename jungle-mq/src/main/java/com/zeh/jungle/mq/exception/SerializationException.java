package com.zeh.jungle.mq.exception;

import com.zeh.jungle.core.error.JGError;

/**
 * 
 * @author allen
 * @version $Id: SerializationException.java, v 0.1 2018年1月30日 下午2:56:53 allen Exp $
 */
public class SerializationException extends MQException {

    /**  */
    private static final long serialVersionUID = 1L;

    /**
     * @param error
     * @param cause
     */
    public SerializationException(JGError error, Throwable cause) {
        super(error, cause);
    }

    /**
     * @param error
     */
    public SerializationException(JGError error) {
        super(error);
    }

    /**
     * @param cause
     */
    public SerializationException(Throwable cause) {
        super(cause);
    }

}
