package com.zeh.jungle.core.monitor;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicInteger;

import com.zeh.jungle.core.context.JungleContext;
import com.zeh.jungle.core.context.ShutdownCallback;
import com.zeh.jungle.core.context.StartupCallback;
import com.zeh.jungle.utils.common.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 系统启动发生时做，监控jvm状态
 * 
 * @author allen
 * @version $Id: JvmMonitor.java, v 0.1 2016年2月28日 上午3:17:26 allen Exp $
 */
public class JvmMonitor implements StartupCallback, ShutdownCallback {
    /** logger */
    private static final Logger monitorLogger = LoggerFactory.getLogger(JvmMonitor.class);

    private MonitorThread       monitor;

    /** 
     * @see com.zeh.jungle.core.context.StartupCallback#startup(com.zeh.jungle.core.context.JungleContext)
     */
    @Override
    public void startup(JungleContext context) {
        LoggerUtils.info(monitorLogger, "Startup JVM monitor ...");
        monitor = new MonitorThread();
        monitor.setDaemon(true);
        monitor.setName("JVM INFO");
        monitor.start();
    }

    /** 
     * @see com.zeh.jungle.core.context.Order#getOrder()
     */
    @Override
    public int getOrder() {
        return StartupCallback.DEFAULT_ORDER;
    }

    /** 
     * @see com.zeh.jungle.core.context.ShutdownCallback#shutdown(com.zeh.jungle.core.context.JungleContext)
     */
    @Override
    public void shutdown(JungleContext context) {
        LoggerUtils.info(monitorLogger, "Destroy JVM monitor ...");
        if (monitor != null) {
            monitor.shutdown();
        }
    }

    /**
     * Monitor thread
     * 
     * @author allen
     * @version $Id: JvmMonitor.java, v 0.1 2016年3月18日 下午2:00:20 allen Exp $
     */
    private static class MonitorThread extends Thread {
        /** logger */
        protected static Logger      logger      = LoggerFactory.getLogger("JVM-MONITOR-DIGEST-LOGGER");

        /** */
        private static final String  SPLITTER    = ",";

        /** */
        private static DecimalFormat decimalformat;

        /** 内存监控 */
        private static MemoryMXBean  memoryMXBean;

        /** 线程监控 */
        private static ThreadMXBean  threadMXBean;

        /** maxDirectedMemory */
        private static Field         maxDirectedMemoryField;

        /** reservedDirected */
        private static Field         reservedDirectedMemoryField;

        /** */
        private AtomicInteger        headerCount = new AtomicInteger(0);

        @Override
        public void run() {
            try {
                decimalformat = new DecimalFormat("#.##");
                memoryMXBean = ManagementFactory.getMemoryMXBean();
                threadMXBean = ManagementFactory.getThreadMXBean();
                Class<?> clz = Class.forName("java.nio.Bits");
                maxDirectedMemoryField = clz.getDeclaredField("maxMemory");
                maxDirectedMemoryField.setAccessible(true);
                reservedDirectedMemoryField = clz.getDeclaredField("reservedMemory");
                reservedDirectedMemoryField.setAccessible(true);
                while (true) {
                    printHeaderPeriodically(20);
                    logger.info(getJVMInfo());
                    sleep(30000);
                }
            } catch (Exception e) {
            }
        }

        public void shutdown() {
            this.interrupt();
        }

        /**
         * 打印日志头信息
         * 
         * @param period
         */
        private void printHeaderPeriodically(int period) {
            if (headerCount.get() == period) {
                headerCount.set(0);
                logger.info(getHeader());
            } else {
                headerCount.incrementAndGet();
            }
        }

        /**
         * 日志头
         * 
         * @return
         */
        private String getHeader() {
            return "(MemoryUsed" + SPLITTER + "MemoryMax)" + "(CurrentThreadCpuTime" + SPLITTER + "ThreadCount" + SPLITTER + "TotalStartedThreadCount)"
                   + "(ReservedDirectMemorySize" + SPLITTER + "MaxDirectMemorySize)[The unit of memory size is MB]";
        }

        /**
         * 获取JVM信息
         * 
         * @return
         */
        private String getJVMInfo() {
            MemoryUsage memoryUsage = memoryMXBean.getHeapMemoryUsage();
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            sb.append(formatValue(memoryUsage.getUsed())).append(SPLITTER);
            sb.append(formatValue(memoryUsage.getMax()));
            sb.append(")");

            sb.append("(");
            sb.append(formatNanosecond(threadMXBean.getCurrentThreadCpuTime())).append(SPLITTER);
            sb.append(threadMXBean.getDaemonThreadCount()).append(SPLITTER);
            sb.append(threadMXBean.getThreadCount()).append(SPLITTER);
            sb.append(threadMXBean.getTotalStartedThreadCount());
            sb.append(")");

            sb.append(this.getDirectedMemoryInfo());

            return sb.toString();
        }

        /**
         * 获取 jvm 堆内存之外的内存信息 参考：http://www.kdgregory.com/index.php?page=java.byteBuffer
         * 
         * @return
         */
        private String getDirectedMemoryInfo() {
            StringBuilder sb = new StringBuilder();

            Long maxMemoryValue = 0l, reservedMemoryValue = 0l;
            try {
                maxMemoryValue = (Long) maxDirectedMemoryField.get(null);
                reservedMemoryValue = (Long) reservedDirectedMemoryField.get(null);
            } catch (Exception e) {
                // ignore
            }
            sb.append("(");
            sb.append(formatValue(reservedMemoryValue));
            sb.append(",");
            sb.append(formatValue(maxMemoryValue));
            sb.append(")");
            return sb.toString();
        }

        /**
         * 将字节单位转为M，保留小数点两位
         * 
         * @param value
         * @return
         */
        private static String formatValue(long value) {
            Double tempValue = new Double(value) / 1024 / 1024;
            return decimalformat.format(tempValue);
        }

        /**
         * 格式化纳秒
         * 
         * @param value
         * @return
         */
        private String formatNanosecond(long value) {
            Double tempValue = new Double(value) / 1000000000;
            return decimalformat.format(tempValue);
        }

    }

}
