package com.zeh.jungle.mq.producer;

import com.zeh.jungle.mq.common.BaseUniformEventProcessor;
import com.zeh.jungle.mq.common.UniformEvent;
import com.zeh.jungle.mq.exception.MQException;

/**
 * 统一消息事件发布器
 * 
 * @author allen
 * @version $Id: UniformEventPublisher.java, v 0.1 2016年3月2日 上午10:46:19 allen Exp $
 */
public interface UniformEventPublisher extends BaseUniformEventProcessor {

    /**
     * 发布统一消息事件
     * 
     * @param event
     * @return
     */
    boolean publishUniformEvent(UniformEvent event) throws MQException;

    /**
     * 发布统一消息事件(oneway方式)
     * 
     * @param event
     * @return
     * @throws MQException
     */
    boolean publishUniformEventOneway(UniformEvent event) throws MQException;

    /**
     * 创建统一消息事件
     * 
     * @param topic 主题
     * @param eventCode 事件码
     * @return
     */
    UniformEvent createUniformEvent(String topic, String eventCode);

    /**
     * 创建统一消息事件
     * 
     * @param topic 主题
     * @param eventCode 事件码
     * @param transactional 事务消息
     * @return
     */
    UniformEvent createUniformEvent(String topic, String eventCode, boolean transactional);

    /**
     * 
     * 
     * @param topic 主题
     * @param eventCode 事件码
     * @param transactional 事务消息
     * @param payload 消息体
     * @return
     */
    UniformEvent createUniformEvent(String topic, String eventCode, boolean transactional, Object payload);

}
