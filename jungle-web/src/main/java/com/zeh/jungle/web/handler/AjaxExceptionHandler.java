package com.zeh.jungle.web.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import com.zeh.jungle.core.exception.JGException;
import com.zeh.jungle.core.exception.JGRuntimeException;
import com.zeh.jungle.utils.common.ThrowableAnalyzer;
import com.zeh.jungle.utils.page.SingleResult;

/**
 * @author hzy24985
 * @version $Id: AjaxExceptionHandler.java, v 0.1 2016/10/31 13:40 hzy24985 Exp $
 */
public class AjaxExceptionHandler extends SimpleMappingExceptionResolver {
    /** logger. */
    private static final Logger logger            = LoggerFactory.getLogger(AjaxExceptionHandler.class);
    /** 异常分析器 */
    private ThrowableAnalyzer   throwableAnalyzer = new ThrowableAnalyzer();

    /**
     * 处理ajax异常，其他异常使用默认的异常处理,会使用配置的exception页面.
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @return
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            SingleResult returnValue = createErrorMessage(ex);
            ModelAndView mav = new ModelAndView();
            mav.setView(new MappingJacksonJsonView());
            mav.addObject("errorMessage", returnValue.getErrorMessage());
            mav.addObject("success", returnValue.isSuccess());
            mav.addObject("errorCode", returnValue.getErrorCode());
            return mav;
        } else {
            request.setAttribute("ex", ex);
            return super.resolveException(request, response, handler, ex);
        }
    }

    /**
     * 创建错误信息
     *
     * @param e
     * @return
     */
    private SingleResult createErrorMessage(Exception e) {
        String errorCode;
        String errorMsg;
        Throwable[] causeChain = throwableAnalyzer.determineCauseChain(e);
        JGException lye = (JGException) throwableAnalyzer.getFirstThrowableOfType(JGException.class, causeChain);
        JGRuntimeException lyre = (JGRuntimeException) throwableAnalyzer.getFirstThrowableOfType(JGRuntimeException.class, causeChain);
        if (lye != null) {
            errorCode = lye.getErrorCode();
            errorMsg = lye.getMessage();
        } else if (lyre != null) {
            errorCode = lyre.getErrorCode();
            errorMsg = lyre.getMessage();
        } else {
            errorCode = "-1";
            errorMsg = e.getMessage();
        }
        SingleResult msg = new SingleResult(null, errorCode, errorMsg);
        return msg;

    }
}