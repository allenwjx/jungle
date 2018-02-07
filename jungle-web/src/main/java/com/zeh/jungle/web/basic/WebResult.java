package com.zeh.jungle.web.basic;

import java.io.Serializable;

/**
 * @author hzy24985
 * @version $Id: WebResult.java, v 0.1 2016/10/31 13:34 hzy24985 Exp $
 */
public class WebResult implements Serializable {

    /**
     * The constant serialVersionUID.
     */
    private static final long serialVersionUID = -8185452078060722380L;

    /**
     * The Result.
     */
    private boolean           result           = true;

    /**
     * The Message.
     */
    private String            message;

    /**
     * The error code.
     */
    private String            errorCode;

    /**
     * data
     */

    private Object            obj;

    /**
     * Instantiates a new Result msg.
     */
    public WebResult() {

    }

    /**
     * Instantiates a new Result msg.
     *
     * @param result  the result
     * @param message the message
     */
    public WebResult(boolean result, String message, Object obj) {
        this.result = result;
        this.message = message;
        this.obj = obj;
    }

    /**
     * Instantiates a new Result msg.
     * @param obj
     */
    public WebResult(Object obj) {
        this.result = true;
        this.obj = obj;
    }

    public static WebResult success() {
        return new WebResult(true, "sucess", null);
    }

    public static WebResult success(Object obj) {
        return new WebResult(true, "sucess", obj);
    }

    /**
     * Instantiates a new Result msg.
     *
     * @param result  the result
     * @param message the message
     */
    public WebResult(boolean result, String message) {
        this(result, message, null);
    }

    /**
     * Is result boolean.
     *
     * @return the boolean
     */
    public boolean isResult() {
        return result;
    }

    /**
     * Sets result.
     *
     * @param result the result
     * @return the result
     */
    public WebResult setResult(boolean result) {
        this.result = result;
        return this;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets message.
     *
     * @param message the message
     * @return the message
     */
    public WebResult setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public WebResult setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }
}