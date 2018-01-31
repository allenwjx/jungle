package com.zeh.jungle.mq.consumer;

import com.zeh.jungle.mq.common.BaseUniformEventProcessor;
import com.zeh.jungle.mq.exception.MQException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

/**
 * 统一消息事件订阅者
 * 
 * @author allen
 * @version $Id: UniformEventSubscriber.java, v 0.1 2016年3月2日 下午3:52:53 allen Exp $
 */
public interface UniformEventSubscriber extends BaseUniformEventProcessor {

    /**
     * 获取从队列的哪里开始消费
     * 
     * @return
     */
    ConsumeFromWhere getConsumeFromWhere();

    /**
     * 获取消费模型
     * 
     * @return
     */
    MessageModel getMessageModel();

    /**
     * 注册统一消息事件监听器
     * 
     * @param listener
     */
    void registerUniformEventMessageListener(UniformEventMessageListener listener);

    /**
     * 订阅
     * 
     * @param topic
     * @param eventId
     */
    void subscribe(final String topic, final String eventId) throws MQException;

    /**
     * 订阅
     * 
     * @param topic
     * @param fullClassName
     * @param filterClassSource
     */
    void subscribe(final String topic, final String fullClassName, final String filterClassSource) throws MQException;

    /**
     * 恢复
     */
    void resume();

    /**
     * 挂起
     */
    void suspend();
}
