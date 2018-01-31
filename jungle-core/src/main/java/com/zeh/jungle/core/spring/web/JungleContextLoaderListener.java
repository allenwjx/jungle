package com.zeh.jungle.core.spring.web;

import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import com.sun.javafx.font.Metrics;
import com.zeh.jungle.core.context.JungleContext;
import com.zeh.jungle.core.exception.JGException;
import com.zeh.jungle.core.exception.JGRuntimeException;
import com.zeh.jungle.utils.common.DateUtil;
import com.zeh.jungle.utils.common.LoggerUtils;
import com.zeh.jungle.utils.common.ThrowableAnalyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.alibaba.dubbo.config.ProtocolConfig;

/**
 * 
 * @author allen
 * @version $Id: JungleContextLoaderListener.java, v 0.1 2016年3月13日 下午12:53:04 allen Exp $
 */
public class JungleContextLoaderListener extends ContextLoaderListener {

    /** logger */
    private static final Logger logger            = LoggerFactory.getLogger(JungleContextLoaderListener.class);

    /** Jungle容器上下文 */
    private JungleContext       jungleContext;

    /** */
    private ThrowableAnalyzer   throwableAnalyzer = new ThrowableAnalyzer();

    /** 
     * @see ContextLoader#customizeContext(javax.servlet.ServletContext, ConfigurableWebApplicationContext)
     */
    @Override
    protected void customizeContext(ServletContext sc, ConfigurableWebApplicationContext wac) {
        super.customizeContext(sc, wac);
        XmlWebApplicationContext context = XmlWebApplicationContext.class.cast(wac);
        context.setAllowBeanDefinitionOverriding(false);
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        LoggerUtils.info(logger, "Launching Jungle application >>>");

        // loading spring context
        super.contextInitialized(event);
        try {
            jungleContext = ContextLoader.getCurrentWebApplicationContext().getBean("jungleContext", JungleContext.class);
            if (!jungleContext.start()) {
                throw new RuntimeException("Failed to init Jungle container");
            }
            printStartupMessage(jungleContext);
        } catch (Exception e) {
            shutdownApplication(e);
            // Stop application
            throw new RuntimeException(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        shutdownApplication();
        ProtocolConfig.destroyAll();
        super.contextDestroyed(event);
    }

    /**
     * Shutdown iProcess application when error occurred
     * 
     * @param e
     */
    private void shutdownApplication(Throwable e) {
        // Process exception
        String errorCode;
        String errorMsg;
        Throwable[] causeChain = throwableAnalyzer.determineCauseChain(e);
        JGException jge = (JGException) throwableAnalyzer.getFirstThrowableOfType(JGException.class, causeChain);
        JGRuntimeException jgre = (JGRuntimeException) throwableAnalyzer.getFirstThrowableOfType(JGRuntimeException.class, causeChain);

        if (jge != null) {
            errorCode = jge.getErrorCode();
            errorMsg = jge.getMessage();
        } else if (jgre != null) {
            errorCode = jgre.getErrorCode();
            errorMsg = jgre.getMessage();
        } else {
            errorCode = "-1";
            errorMsg = e.getMessage();
        }

        // Print failure message
        printStartupFailMessage(errorCode, errorMsg, e);

        // Shutdown application
        shutdownApplication();
    }

    /**
     * 关闭应用
     * 
     * @return
     */
    protected boolean shutdownApplication() {
        LoggerUtils.info(logger, "Shutdown application ...");

        if (jungleContext != null) {
            // 清理资源
            jungleContext.shutdown();
        }
        printShutdownMessage(jungleContext);
        jungleContext = null;
        return true;
    }

    /**
     * print
     * 
     * @param jungleContext
     */
    private void printStartupMessage(JungleContext jungleContext) {
        LoggerUtils.info(logger, "********************************************************************************");
        LoggerUtils.info(logger, "* Jungle container has launched.");
        LoggerUtils.info(logger, "* Jungle id: {}.", jungleContext.getId());
        LoggerUtils.info(logger, "* Jungle name: {}.", jungleContext.getContextName());
        LoggerUtils.info(logger, "* Application name: {}.", jungleContext.getAppName());
        LoggerUtils.info(logger, "* Host name: {}.", jungleContext.getAppConfiguration().getSysHostName());
        LoggerUtils.info(logger, "* IP: {}.", jungleContext.getAppConfiguration().getSysIp());
        LoggerUtils.info(logger, "* Run mode: {}.", jungleContext.getAppConfiguration().getSysRunMode());
        LoggerUtils.info(logger, "* Launched date: {}.", DateUtil.getDateString(new Date(jungleContext.getStartupDate())));
        LoggerUtils.info(logger, "********************************************************************************");
    }

    /**
     * print
     * 
     * @param jungleContext
     */
    private void printShutdownMessage(JungleContext jungleContext) {
        LoggerUtils.info(logger, "********************************************************************************");
        LoggerUtils.info(logger, "* Jungle container has shutdown.");
        LoggerUtils.info(logger, "* Shutdown date: {}.", DateUtil.getDateString(new Date()));
        LoggerUtils.info(logger, "********************************************************************************");
    }

    /**
     * print failure error
     * 
     * @param errorCode
     * @param errorMsg
     * @param e
     */
    private void printStartupFailMessage(String errorCode, String errorMsg, Throwable e) {
        LoggerUtils.error(logger, "********************************************************************************");
        LoggerUtils.error(logger, "* Jungle container startup failed.");
        LoggerUtils.error(logger, "* Error code: {}.", errorCode);
        LoggerUtils.error(logger, "* Error message: {}.", e, errorMsg);
        LoggerUtils.error(logger, "* Failure date: {}.", DateUtil.getDateString(new Date()));
        LoggerUtils.error(logger, "********************************************************************************");
    }

}
