package com.zeh.jungle.core.exception;

import com.zeh.jungle.core.error.JGError;

/**
 * 插件异常定义
 *
 * @author allen
 * @version $Id: ExtensionException.java, v 0.1 2016年2月29日 下午4:18:49 allen Exp $
 */
public class ExtensionException extends JGRuntimeException {

    /**  */
    private static final long serialVersionUID = -3569610405415535346L;

    /**
     * @param error
     * @param cause
     */
    public ExtensionException(JGError error, Throwable cause) {
        super(error, cause);
    }

    /**
     * @param error
     */
    public ExtensionException(JGError error) {
        super(error);
    }

    /**
     * @param cause
     */
    public ExtensionException(Throwable cause) {
        super(cause);
    }

}
