package com.zeh.jungle.core.event;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 事件广播器抽象实现，负责事件监听器的注册，事件监听器的管理，事件的同步、异步广播
 * 
 * @author allen
 * @version $Id: AbstractEventMulticaster.java, v 0.1 2016年2月27日 下午11:34:27 allen Exp $
 */
public abstract class AbstractEventMulticaster implements EventMulticaster {

    /**默认事件监听器容器，保存所有注册的事件监听器*/
    private final ListenerKeeper defaultListenerKeeper = new ListenerKeeper();

    /**锁*/
    private final ReadWriteLock  lock                  = new ReentrantReadWriteLock();

    /**
     * @see com.zeh.jungle.core.event.EventMulticaster#addListener(com.zeh.jungle.core.event.BaseListener)
     */
    @Override
    public void addListener(BaseListener<BaseEvent> listener) {

        try {
            lock.writeLock().lock();
            this.defaultListenerKeeper.listeners.add(listener);
            //            this.keeperCache.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * @see com.zeh.jungle.core.event.EventMulticaster#removeListener(com.zeh.jungle.core.event.BaseListener)
     */
    @Override
    public void removeListener(BaseListener<BaseEvent> listener) {
        try {
            lock.writeLock().lock();
            this.defaultListenerKeeper.listeners.remove(listener);
            //            this.keeperCache.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * @see com.zeh.jungle.core.event.EventMulticaster#removeAllListeners()
     */
    @Override
    public void removeAllListeners() {
        try {
            lock.writeLock().lock();
            this.defaultListenerKeeper.listeners.clear();
            //            this.keeperCache.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * @see com.zeh.jungle.core.event.EventMulticaster#getAllListeners()
     */
    @Override
    public Collection<BaseListener<BaseEvent>> getAllListeners() {
        return this.defaultListenerKeeper.retrieveListeners();
    }

    /**
     * @see com.zeh.jungle.core.event.EventMulticaster#getAllListeners(com.zeh.jungle.core.event.BaseEvent)
     */
    @Override
    public Collection<BaseListener<BaseEvent>> getAllListeners(BaseEvent event) {
        LinkedList<BaseListener<BaseEvent>> allListeners = new LinkedList<BaseListener<BaseEvent>>();
        for (BaseListener<BaseEvent> listener : this.defaultListenerKeeper.listeners) {
            // Check whether the event is suppored by the listener
            if (listener.isSupportEvent(event)) {
                allListeners.add(listener);
            }
        }
        return allListeners;
    }

    /**
     * 事件监听器容器
     * 
     * @author allen
     * @version $Id: AbstractEventMulticaster.java, v 0.1 2016年2月27日 下午11:35:18 allen Exp $
     */
    private class ListenerKeeper {
        /**注册事件容器*/
        public final Set<BaseListener<BaseEvent>> listeners;

        /**
         * 构造方法
         */
        public ListenerKeeper() {
            listeners = new LinkedHashSet<BaseListener<BaseEvent>>();
        }

        /**
         * 获取容器注册的监听器
         * @return
         */
        public Collection<BaseListener<BaseEvent>> retrieveListeners() {
            LinkedList<BaseListener<BaseEvent>> allListeners = new LinkedList<BaseListener<BaseEvent>>();
            for (BaseListener<BaseEvent> listener : this.listeners) {
                allListeners.add(listener);
            }
            return allListeners;
        }
    }
}
