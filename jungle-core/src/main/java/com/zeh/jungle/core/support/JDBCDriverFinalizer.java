package com.zeh.jungle.core.support;

import java.lang.reflect.Method;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import com.zeh.jungle.core.context.JungleContext;
import com.zeh.jungle.core.context.ShutdownCallback;
import com.zeh.jungle.utils.common.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author allen
 * @version $Id: JDBCDriverFinalizer.java, v 0.1 2016年3月18日 下午6:42:52 allen Exp $
 */
public class JDBCDriverFinalizer implements ShutdownCallback {
    /** logger */
    private static final Logger  logger  = LoggerFactory.getLogger(JDBCDriverFinalizer.class);

    /**
     * List of information on threads required to stop.  This list may be
     * expanded as necessary.
     */
    private List<JDBCThreadInfo> threads = Arrays.asList(
        // Special cleanup for MySQL JDBC Connector.
        new JDBCThreadInfo("com.mysql.jdbc.AbandonedConnectionCleanupThread", "Abandoned connection cleanup thread", "shutdown"));

    /** 
     * @see com.zeh.jungle.core.context.ShutdownCallback#shutdown(com.zeh.jungle.core.context.JungleContext)
     */
    @Override
    public void shutdown(JungleContext context) {
        // Deregister all drivers.
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver d = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(d);
                LoggerUtils.info(logger, "Driver {} deregistered", d);
            } catch (SQLException e) {
                LoggerUtils.warn(logger, "Failed to deregister driver {}", e, d);
            }
        }

        // Handle remaining threads.
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);
        for (Thread thread : threadArray) {
            for (JDBCThreadInfo threadInfo : this.threads) {
                processThread(thread, threadInfo);
            }
        }
    }

    /**
     * 关闭JDBC线程
     * 
     * @param thread
     * @param threadInfo
     */
    private void processThread(Thread thread, JDBCThreadInfo threadInfo) {
        if (thread.getName().contains(threadInfo.getCue())) {
            synchronized (thread) {
                try {
                    Class<?> cls = Class.forName(threadInfo.getName());
                    if (cls != null) {
                        Method mth = cls.getMethod(threadInfo.getStop());
                        if (mth != null) {
                            mth.invoke(null);
                            LoggerUtils.info(logger, "Connection cleanup thread {} shutdown successfully.", threadInfo.getName());
                        }
                    }
                } catch (Throwable thr) {
                    LoggerUtils.info(logger, "Failed to shutdown connection cleanup thread {}: ", threadInfo.getName(), thr.getMessage());
                    thr.printStackTrace();
                }
            }
        }
    }

    private class JDBCThreadInfo {
        /**
         * Name of the thread's initiating class.
         */
        private final String name;

        /**
         * Cue identifying the thread.
         */
        private final String cue;

        /**
         * Name of the method to stop the thread.
         */
        private final String stop;

        /**
         * Basic constructor.
         * @param n Name of the thread's initiating class.
         * @param c Cue identifying the thread.
         * @param s Name of the method to stop the thread.
         */
        JDBCThreadInfo(final String n, final String c, final String s) {
            this.name = n;
            this.cue = c;
            this.stop = s;
        }

        /**
         * @return the name
         */
        public String getName() {
            return this.name;
        }

        /**
         * @return the cue
         */
        public String getCue() {
            return this.cue;
        }

        /**
         * @return the stop
         */
        public String getStop() {
            return this.stop;
        }
    }

}
