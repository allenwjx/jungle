package com.zeh.jungle.mq.consumer;

import com.zeh.jungle.mq.common.UniformEvent;
import com.zeh.jungle.mq.common.UniformEventContext;
import com.zeh.jungle.mq.exception.MQException;

/**
 * 统一消息事件监听器
 * 
 * @author allen
 * @version $Id: UniformEventMessageListener.java, v 0.1 2016年3月2日 下午4:05:31 allen Exp $
 */
public interface UniformEventMessageListener {
    /**
     * 消费统一消息事件，如果MQ发送的消息头中，不能找到序列化类型，则默认将二进制数据直接返回出来
     * 
     * @param event 统一消息事件
     * @param context 统一消息事件上下文
     * @return 是否成功消费
     * @throws MQException
     */
    boolean onUniformEvent(UniformEvent event, UniformEventContext context) throws MQException;

    /**
     * 获取监听器类型
     * 
     * @return
     */
    ListenerTypeEnum getListenerType();

    /**
     * 对应RocketMQ的MessageListenerConcurrently和MessageListenerOrderly两种监听器类型
     * 
     * @author allen
     * @version $Id: UniformEventMessageListener.java, v 0.1 2016年3月2日 下午4:27:23 allen Exp $
     */
    enum ListenerTypeEnum {
                           CONCURRENTLY, ORDERLY
    }
}
