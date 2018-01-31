package com.zeh.jungle.dal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * * 数据路由上下文Holder<br/>
 * base on ThreadLocal
 * @author allen
 * @version $Id: DataRoutingContextHolder.java, v 0.1 2016年4月29日 下午2:10:46 wb30644 Exp $
 */
public class DataRoutingContextHolder {
    private static final Logger              LOG           = LoggerFactory.getLogger(DataRoutingContextHolder.class);

    private static final ThreadLocal<Object> contextHolder = new ThreadLocal<Object>();

    /**
     * 设置当前线程的数据路由标识上下文<br/>
     * 可自由切换
     * 
     * @param <T>
     * @param context
     */
    public static <T> void setContext(T context) {
        contextHolder.set(context);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Data Routing >> " + context);
        }
    }

    /**
     * 获取当前线程的数据路由标识上下文
     */
    @SuppressWarnings("unchecked")
    public static <T> T getContext() {
        return (T) contextHolder.get();
    }

    /**
     * 清空当前线程的数据路由标识上下文
     */
    public static void clearContext() {
        contextHolder.remove();
    }
}