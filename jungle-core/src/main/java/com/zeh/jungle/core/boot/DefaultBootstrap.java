package com.zeh.jungle.core.boot;

import java.util.Date;
import java.util.List;

import com.zeh.jungle.core.context.JungleContext;
import com.zeh.jungle.core.exception.JGException;
import com.zeh.jungle.core.exception.JGRuntimeException;
import com.zeh.jungle.core.extension.DefaultExtensionLoader;
import com.zeh.jungle.core.extension.ExtensionLoader;
import com.zeh.jungle.utils.common.DateUtil;
import com.zeh.jungle.utils.common.LoggerUtils;
import com.zeh.jungle.utils.common.ThrowableAnalyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.container.spring.SpringContainer;
import com.google.common.collect.Lists;

/**
 * 
 * @author allen
 * @version $Id: DefaultBootstrap.java, v 0.1 2016年2月28日 上午4:57:38 allen Exp $
 */
public class DefaultBootstrap implements JungleBootstrap {
    /** LOGGER */
    private static final Logger    LOGGER            = LoggerFactory.getLogger(DefaultBootstrap.class);

    /** 插件加载器 */
    private ExtensionLoader        extensionLoader   = new DefaultExtensionLoader();

    /** Shutdown hooker */
    private JungleShutdownListener shutdownListener;

    /** 容器上下文 */
    private JungleContext          jungleContext;

    /** */
    private ThrowableAnalyzer      throwableAnalyzer = new ThrowableAnalyzer();

    /** 容器是否运行中 */
    private boolean                running           = true;

    /** 扩展插件 */
    private List<JungleBootstrap>  boots             = Lists.newArrayList();

    /**
     * 应用启动
     * 
     * @param args
     * @return
     */
    @Override
    public synchronized boolean startup(String[] args) {
        LoggerUtils.info(LOGGER, "Launching Jungle application >>>");
        registerShutdownListener();
        try {
            boots = extensionLoader.loadExtension();
            executeExtension(args, boots);
            jungleContext = SpringContainer.getContext().getBean("jungleContext", JungleContext.class);
            printStartupMessage(jungleContext);
        } catch (Exception e) {
            shutdownApplication(e);
            System.exit(1);
        }

        synchronized (DefaultBootstrap.class) {
            while (running) {
                try {
                    DefaultBootstrap.class.wait();
                } catch (Throwable e) {
                }
            }
        }
        return true;
    }

    /** 
     * @see com.zeh.jungle.core.boot.JungleBootstrap#shutdown()
     */
    @Override
    public synchronized boolean shutdown() {
        return shutdownApplication();
    }

    /**
     * 启动插件
     * 
     * @param args
     * @param boots
     */
    private void executeExtension(String[] args, List<JungleBootstrap> boots) {
        for (JungleBootstrap boot : boots) {
            LoggerUtils.info(LOGGER, "Executing extension {}", boot.getClass().getName());
            if (!boot.startup(args)) {
                throw new RuntimeException("Failed to start container");
            }
        }
    }

    /**
     * 注册停机监听器
     */
    private void registerShutdownListener() {
        if (shutdownListener == null) {
            shutdownListener = new JungleShutdownListener();
        } else {
            Runtime.getRuntime().removeShutdownHook(shutdownListener);
        }
        Runtime.getRuntime().addShutdownHook(shutdownListener);
    }

    /**
     * 注销停机监听器
     */
    private void unRegisterShutdownListener() {
        if (shutdownListener != null) {
            Runtime.getRuntime().removeShutdownHook(shutdownListener);
        }
    }

    /**
     * Shutdown iProcess application when error occurred
     * 
     * @param e
     */
    private void shutdownApplication(Throwable e) {
        // Process exception
        String errorCode = "";
        String errorMsg = "";

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
        LoggerUtils.info(LOGGER, "Shutdown application ...");

        if (jungleContext != null) {
            // 清理资源
            jungleContext.shutdown();
        }
        unRegisterShutdownListener();
        printShutdownMessage(jungleContext);
        jungleContext = null;
        return true;
    }

    /**
     * 停机监听器
     * 
     * @author allen
     * @version $Id: JungleShutdownListener.java, v 0.1 2016年2月28日 上午6:46:13 allen Exp $
     */
    private class JungleShutdownListener extends Thread {

        @Override
        public void run() {
            try {
                for (JungleBootstrap boot : boots) {
                    try {
                        boot.shutdown();
                    } catch (Throwable t) {
                        LOGGER.error(t.getMessage(), t);
                    }
                }
                shutdownApplication();
            } finally {
                synchronized (DefaultBootstrap.class) {
                    running = false;
                    DefaultBootstrap.class.notify();
                }
            }
        }

    }

    /**
     * print
     * 
     * @param jungleContext
     */
    private void printStartupMessage(JungleContext jungleContext) {
        LoggerUtils.info(LOGGER, "********************************************************************************");
        LoggerUtils.info(LOGGER, "* Jungle container has launched.");
        LoggerUtils.info(LOGGER, "* Jungle id: {}.", jungleContext.getId());
        LoggerUtils.info(LOGGER, "* Jungle name: {}.", jungleContext.getContextName());
        LoggerUtils.info(LOGGER, "* Application name: {}.", jungleContext.getAppName());
        LoggerUtils.info(LOGGER, "* Host name: {}.", jungleContext.getAppConfiguration().getSysHostName());
        LoggerUtils.info(LOGGER, "* IP: {}.", jungleContext.getAppConfiguration().getSysIp());
        LoggerUtils.info(LOGGER, "* Run mode: {}.", jungleContext.getAppConfiguration().getSysRunMode());
        LoggerUtils.info(LOGGER, "* Launched date: {}.", DateUtil.getDateString(new Date(jungleContext.getStartupDate())));
        LoggerUtils.info(LOGGER, "********************************************************************************");
    }

    /**
     * print
     * 
     * @param jungleContext
     */
    private void printShutdownMessage(JungleContext jungleContext) {
        LoggerUtils.info(LOGGER, "********************************************************************************");
        LoggerUtils.info(LOGGER, "* Jungle container has shutdown.");
        LoggerUtils.info(LOGGER, "* Shutdown date: {}.", DateUtil.getDateString(new Date()));
        LoggerUtils.info(LOGGER, "********************************************************************************");
    }

    /**
     * print failure error
     * 
     * @param errorCode
     * @param errorMsg
     * @param e
     */
    private void printStartupFailMessage(String errorCode, String errorMsg, Throwable e) {
        LoggerUtils.error(LOGGER, "********************************************************************************");
        LoggerUtils.error(LOGGER, "* Jungle container startup failed.");
        LoggerUtils.error(LOGGER, "* Error code: {}.", errorCode);
        LoggerUtils.error(LOGGER, "* Error message: {}.", e, errorMsg);
        LoggerUtils.error(LOGGER, "* Failure date: {}.", DateUtil.getDateString(new Date()));
        LoggerUtils.error(LOGGER, "********************************************************************************");
    }
}
