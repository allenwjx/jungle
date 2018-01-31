package com.zeh.jungle.client.exception;

/**
 * 
 * @author allen
 * @version $Id: HttpInvokeException.java, v 0.1 2016年5月19日 下午1:20:19 allen Exp $
 */
public class HttpInvokeException extends RuntimeException {

    /**  */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public HttpInvokeException() {
        super();
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public HttpInvokeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * @param message
     * @param cause
     */
    public HttpInvokeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public HttpInvokeException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public HttpInvokeException(Throwable cause) {
        super(cause);
    }

}
