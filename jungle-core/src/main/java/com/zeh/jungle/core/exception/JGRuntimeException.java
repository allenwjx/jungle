package com.zeh.jungle.core.exception;

import com.zeh.jungle.core.error.AbstractErrorFactory;
import com.zeh.jungle.core.error.JGError;
import com.zeh.jungle.utils.common.ExceptionHelper;

/**
 * 运行时基础异常，所有的运行时异常定义都应继承该异常
 * 
 * @author allen
 * @version $Id: JGRuntimeException.java, v 0.1 2016年2月26日 下午3:14:18 allen Exp $
 */
public class JGRuntimeException extends RuntimeException {
    /**  */
    private static final long serialVersionUID = 1L;

    /**错误描述*/
    private String            message;

    /**错误消息对象*/
    private JGError           i18nError;

    /**
     * 
     * @param error
     */
    public JGRuntimeException(JGError error) {
        super();
        setError(error);
    }

    /**
     * 
     * @param error
     * @param cause
     */
    public JGRuntimeException(JGError error, Throwable cause) {
        super(ExceptionHelper.unwrap(cause));
        setError(error);
    }

    /**
     * 
     * @param cause
     */
    public JGRuntimeException(Throwable cause) {
        super(ExceptionHelper.unwrap(cause));
        if (cause != null) {
            setError(AbstractErrorFactory.createStaticError(cause.getMessage() + " (" + cause.getClass().getName() + ")", JGError.DEFAULT_ERROR_CODE));
        }
    }

    /**
     * 获取该异常的错误消息
     * 
     * @return 错误消息
     */
    public JGError getError() {
        return i18nError;
    }

    /**
     * 获取错误消息的错误码
     * 
     * @return
     */
    public String getErrorCode() {
        return i18nError == null ? JGError.DEFAULT_ERROR_CODE : i18nError.getCode();
    }

    /**
     * @return
     * @see Throwable#getMessage()
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * 在原错误信息基础上，添加额外错误描述
     * 
     * @param s
     */
    protected void appendMessage(String s) {
        message += s;
    }

    /**
     * 预处理消息
     * 
     * @param s
     */
    protected void prependMessage(String s) {
        message = message + ". " + s;
    }

    /**
     * 设置异常的错误消息
     * 
     * 一般来说，一条错误消息针对资源文件中的一条配置记录
     * 错误码＝错误描述
     * 
     * @param error 资源文件描述的错误消息
     */
    protected void setError(JGError error) {
        this.message = error.getMessage();
        this.i18nError = error;
    }
}
