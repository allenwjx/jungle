package com.zeh.jungle.mq.common;

import com.zeh.jungle.mq.exception.MQException;

/**
 * 
 * @author allen
 * @version $Id: BaseUniformEventProcessor.java, v 0.1 2016年3月2日 下午4:02:59 allen Exp $
 */
public interface BaseUniformEventProcessor {

    /**
     * 启动统一消息处理器
     */
    void start() throws MQException;

    /**
     * 停止统一消息处理器
     */
    void shutdown() throws MQException;

    /**
     * 获取消息分组
     * 
     * @return
     */
    String getGroup();

    /**
     * 获取命名服务器
     * 
     * @return
     */
    String getNameSrvAddress();
}
