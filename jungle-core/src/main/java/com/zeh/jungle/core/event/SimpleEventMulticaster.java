package com.zeh.jungle.core.event;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 简单事件广播器实现，同步、异步广播事件，并从事件监听器注册容器中，获取正确的监听器进行事件处理
 * 
 * @author allen
 * @version $Id: SimpleEventMulticaster.java, v 0.1 2016年2月27日 下午11:38:47 allen Exp $
 */
public class SimpleEventMulticaster extends AbstractEventMulticaster {
    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(SimpleEventMulticaster.class);

    /**
     * Asynchronous executor 
     */
    private Executor            taskExecutor;

    /**
     * @see com.zeh.jungle.core.event.EventMulticaster#multicastEvent(com.zeh.jungle.core.event.BaseEvent, boolean)
     */
    @Override
    public void multicastEvent(final BaseEvent event, boolean sync) {
        for (final BaseListener<BaseEvent> listener : getAllListeners(event)) {
            Executor executor = getTaskExecutor();

            if (!sync) {
                // async
                executor.execute(new AnonymityEventHandler(event, listener));
            } else {
                // sync
                try {
                    listener.onEvent(event);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 获取任务执行器
     * 
     * @return
     */
    public Executor getTaskExecutor() {
        if (taskExecutor == null) {
            ThreadFactory threadFactory = new NamedThreadFactory("jungle-event", true);
            taskExecutor = new ThreadPoolExecutor(16, 128, 5L, TimeUnit.MINUTES, new LinkedBlockingDeque<Runnable>(1024), threadFactory, new ThreadPoolExecutor.AbortPolicy());
        }
        return taskExecutor;
    }

    /**
     * 设置任务执行器
     * 
     * @param taskExecutor
     */
    public void setTaskExecutor(Executor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    /**
     * 异步事件处理
     * 
     * @author allen
     * @version $Id: SimpleEventMulticaster.java, v 0.1 2016年2月27日 下午11:57:42 allen Exp $
     */
    private final class AnonymityEventHandler implements Runnable {
        /** 事件 */
        private BaseEvent               event;

        /** 事件监听器 */
        private BaseListener<BaseEvent> listener;

        /**
         * 构造函数
         * 
         * @param event 事件
         * @param listener 事件监听器
         */
        public AnonymityEventHandler(BaseEvent event, BaseListener<BaseEvent> listener) {
            super();
            this.event = event;
            this.listener = listener;
        }

        /**
         * 事件处理
         * 
         * @see Runnable#run()
         */
        @Override
        public void run() {
            try {
                listener.onEvent(event);
            } catch (Exception e) {
                logger.error("[Process a event, failure] event: " + event.getClass().getName() + ":" + event.getId(), e);
            }
        }
    }

    /**
     * @author allen
     * @version $Id: SimpleEventMulticaster.java, v 0.1 2016年3月18日 下午8:13:13 allen Exp $
     */
    private static class NamedThreadFactory implements ThreadFactory {

        private final AtomicInteger mThreadNum = new AtomicInteger(1);

        private final String        mPrefix;

        private final boolean       mDaemo;

        private final ThreadGroup   mGroup;

        public NamedThreadFactory(String prefix, boolean daemo) {
            mPrefix = prefix + "-thread-";
            mDaemo = daemo;
            SecurityManager s = System.getSecurityManager();
            mGroup = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
        }

        @Override
        public Thread newThread(Runnable runnable) {
            String name = mPrefix + mThreadNum.getAndIncrement();
            Thread ret = new Thread(mGroup, runnable, name, 0);
            ret.setDaemon(mDaemo);
            return ret;
        }
    }
}
