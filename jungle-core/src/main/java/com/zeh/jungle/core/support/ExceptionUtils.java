package com.zeh.jungle.core.support;

import com.zeh.jungle.core.exception.JGException;
import com.zeh.jungle.core.exception.JGRuntimeException;
import com.zeh.jungle.utils.common.LoggerUtils;
import com.zeh.jungle.utils.common.ThrowableAnalyzer;
import com.zeh.jungle.utils.page.BaseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author allen
 * @version $Id: ExceptionUtils.java, v 0.1 2016年5月5日 下午4:02:54 allen Exp $
 */
public class ExceptionUtils {
    /**logger*/
    private static final Logger            logger            = LoggerFactory.getLogger(ExceptionUtils.class);
    private static final ThrowableAnalyzer throwableAnalyzer = new ThrowableAnalyzer();

    /***/
    private ExceptionUtils() {
    }

    /**
     * 根据LY异常创建facade返回结果
     * 
     * @param ex LY异常
     * @param clazz {@link BaseResult}子类类型
     * @return {@link BaseResult}子类实例
     */
    public static <T extends BaseResult> T getErrorResult(JGException ex, Class<T> clazz) {
        try {
            T ret = clazz.newInstance();
            ret.setSuccess(false);
            ret.setErrorCode(ex.getErrorCode());
            ret.setErrorMessage(ex.getMessage());
            return ret;
        } catch (InstantiationException e) {
            LoggerUtils.error(logger, "创建错误结果失败", e);
        } catch (IllegalAccessException e) {
            LoggerUtils.error(logger, "创建错误结果失败", e);
        }
        return null;
    }

    /**
     * 根据LY异常创建facade返回结果
     * 
     * @param ex LY异常
     * @param clazz {@link BaseResult}子类类型
     * @return {@link BaseResult}子类实例
     */
    public static <T extends BaseResult> T getErrorResult(JGRuntimeException ex, Class<T> clazz) {
        try {
            T ret = clazz.newInstance();
            ret.setSuccess(false);
            ret.setErrorCode(ex.getErrorCode());
            ret.setErrorMessage(ex.getMessage());
            return ret;
        } catch (InstantiationException e) {
            LoggerUtils.error(logger, "创建错误结果失败", e);
        } catch (IllegalAccessException e) {
            LoggerUtils.error(logger, "创建错误结果失败", e);
        }
        return null;
    }

    /**
     * 根据异常创建facade返回结果
     * 
     * @param ex
     * @param clazz
     * @return
     */
    public static <T extends BaseResult> T getErrorResult(Exception ex, Class<T> clazz) {
        try {
            T ret = clazz.newInstance();
            InternalError err = getError(ex);
            ret.setSuccess(false);
            ret.setErrorCode(err.getErrCode());
            ret.setErrorMessage(err.getErrMsg());
            return ret;
        } catch (InstantiationException e) {
            LoggerUtils.error(logger, "创建错误结果失败", e);
        } catch (IllegalAccessException e) {
            LoggerUtils.error(logger, "创建错误结果失败", e);
        }
        return null;
    }

    private static InternalError getError(Exception e) {
        Throwable[] causeChain = throwableAnalyzer.determineCauseChain(e);
        JGException lye = (JGException) throwableAnalyzer.getFirstThrowableOfType(JGException.class, causeChain);
        JGRuntimeException lyre = (JGRuntimeException) throwableAnalyzer.getFirstThrowableOfType(JGRuntimeException.class, causeChain);

        if (lye != null) {
            return new InternalError(lye.getErrorCode(), lye.getMessage());
        } else if (lyre != null) {
            return new InternalError(lyre.getErrorCode(), lyre.getMessage());
        } else {
            return new InternalError("unknown", e.getMessage());
        }
    }

    private static final class InternalError {
        private String errCode;
        private String errMsg;

        /**
         * @param errCode
         * @param errMsg
         */
        public InternalError(String errCode, String errMsg) {
            super();
            this.errCode = errCode;
            this.errMsg = errMsg;
        }

        /**
         * Getter method for property <tt>errCode</tt>.
         * 
         * @return property value of errCode
         */
        public String getErrCode() {
            return errCode;
        }

        /**
         * Getter method for property <tt>errMsg</tt>.
         * 
         * @return property value of errMsg
         */
        public String getErrMsg() {
            return errMsg;
        }

    }
}
