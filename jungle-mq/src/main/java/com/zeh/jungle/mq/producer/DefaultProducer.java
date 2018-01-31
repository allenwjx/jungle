package com.zeh.jungle.mq.producer;

import java.util.Map;

import com.zeh.jungle.mq.common.SerializeEnum;
import com.zeh.jungle.mq.common.UniformEvent;
import com.zeh.jungle.mq.exception.MQException;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.SendCallback;
import org.springframework.util.Assert;

/**
 * 消息生产者默认实现
 * 
 * @author allen
 * @version $Id: DefaultProducer.java, v 0.1 2016年3月2日 上午10:20:16 allen Exp $
 */
public class DefaultProducer implements Producer {

    /** 统一消息事件发布器 */
    private UniformEventPublisher uniformEventPublisher;

    /** 
     * @see com.zeh.jungle.mq.producer.Producer#sendOneway(String, String, Object)
     */
    @Override
    public boolean sendOneway(String topic, String eventCode, Object payload) throws MQException {
        return sendOneway(topic, eventCode, payload, SerializeEnum.KRYO);
    }

    /** 
     * @see com.zeh.jungle.mq.producer.Producer#sendOneway(String, String, Object, com.zeh.jungle.mq.common.SerializeEnum)
     */
    @Override
    public boolean sendOneway(String topic, String eventCode, Object payload, SerializeEnum serialize) throws MQException {
        Assert.notNull(StringUtils.isNotBlank(topic), "Topic is required");
        Assert.notNull(StringUtils.isNotBlank(eventCode), "Event code is required");
        Assert.notNull(serialize, "SerializeEnum is required");
        Assert.notNull(payload, "Payload cannot be null");
        UniformEvent event = uniformEventPublisher.createUniformEvent(topic, eventCode, false, payload);
        event.setSerialize(serialize);
        return uniformEventPublisher.publishUniformEventOneway(event);
    }

    /** 
     * @see com.zeh.jungle.mq.producer.Producer#send(String, String, Object)
     */
    @Override
    public boolean send(String topic, String eventCode, Object payload) throws MQException {
        return send(topic, eventCode, payload, -1, null, SerializeEnum.KRYO);
    }

    /** 
     * @see com.zeh.jungle.mq.producer.Producer#send(String, String, Object, long)
     */
    @Override
    public boolean send(String topic, String eventCode, Object payload, long timeout) throws MQException {
        return send(topic, eventCode, payload, timeout, null, SerializeEnum.KRYO);
    }

    /** 
     * @see com.zeh.jungle.mq.producer.Producer#send(String, String, Object, com.zeh.jungle.mq.common.SerializeEnum)
     */
    @Override
    public boolean send(String topic, String eventCode, Object payload, SerializeEnum serialize) throws MQException {
        return send(topic, eventCode, payload, -1, null, serialize);
    }

    /** 
     * @see com.zeh.jungle.mq.producer.Producer#send(String, String, Object, Map)
     */
    @Override
    public boolean send(String topic, String eventCode, Object payload, Map<String, String> props) throws MQException {
        return send(topic, eventCode, payload, -1, props, SerializeEnum.KRYO);
    }

    /** 
     * @see com.zeh.jungle.mq.producer.Producer#send(String, String, Object, long, com.zeh.jungle.mq.common.SerializeEnum)
     */
    @Override
    public boolean send(String topic, String eventCode, Object payload, long timeout, SerializeEnum serialize) throws MQException {
        return send(topic, eventCode, payload, timeout, null, serialize);
    }

    /** 
     * @see com.zeh.jungle.mq.producer.Producer#send(String, String, Object, long, Map)
     */
    @Override
    public boolean send(String topic, String eventCode, Object payload, long timeout, Map<String, String> props) throws MQException {
        return send(topic, eventCode, payload, timeout, props, SerializeEnum.KRYO);
    }

    /** 
     * @see com.zeh.jungle.mq.producer.Producer#send(String, String, Object, Map, com.zeh.jungle.mq.common.SerializeEnum)
     */
    @Override
    public boolean send(String topic, String eventCode, Object payload, Map<String, String> props, SerializeEnum serialize) throws MQException {
        return send(topic, eventCode, payload, -1, props, serialize);
    }

    /** 
     * @see com.zeh.jungle.mq.producer.Producer#send(String, String, Object, long, Map, com.zeh.jungle.mq.common.SerializeEnum)
     */
    @Override
    public boolean send(String topic, String eventCode, Object payload, long timeout, Map<String, String> props, SerializeEnum serialize) throws MQException {
        Assert.notNull(StringUtils.isNotBlank(topic), "Topic is required");
        Assert.notNull(StringUtils.isNotBlank(eventCode), "Event code is required");
        Assert.notNull(payload, "Payload cannot be null");
        Assert.notNull(serialize, "SerializeEnum cannot be null");
        UniformEvent event = uniformEventPublisher.createUniformEvent(topic, eventCode, false, payload);
        event.setTimeout(timeout);
        event.setSerialize(serialize);
        event.addProperties(props);
        return uniformEventPublisher.publishUniformEvent(event);
    }

    /** 
     * @see com.zeh.jungle.mq.producer.Producer#send(String, String, Object, org.apache.rocketmq.client.producer.SendCallback)
     */
    @Override
    public boolean send(String topic, String eventCode, Object payload, SendCallback sendCallback) throws MQException {
        return send(topic, eventCode, payload, -1, null, null, sendCallback);
    }

    /** 
     * @see com.zeh.jungle.mq.producer.Producer#send(String, String, Object, long, org.apache.rocketmq.client.producer.SendCallback)
     */
    @Override
    public boolean send(String topic, String eventCode, Object payload, long timeout, SendCallback sendCallback) throws MQException {
        return send(topic, eventCode, payload, timeout, null, null, sendCallback);
    }

    /** 
     * @see com.zeh.jungle.mq.producer.Producer#send(String, String, Object, com.zeh.jungle.mq.common.SerializeEnum, org.apache.rocketmq.client.producer.SendCallback)
     */
    @Override
    public boolean send(String topic, String eventCode, Object payload, SerializeEnum serialize, SendCallback sendCallback) throws MQException {
        return send(topic, eventCode, payload, -1, null, serialize, sendCallback);
    }

    /** 
     * @see com.zeh.jungle.mq.producer.Producer#send(String, String, Object, Map, org.apache.rocketmq.client.producer.SendCallback)
     */
    @Override
    public boolean send(String topic, String eventCode, Object payload, Map<String, String> props, SendCallback sendCallback) throws MQException {
        return send(topic, eventCode, payload, -1, props, null, sendCallback);
    }

    /** 
     * @see com.zeh.jungle.mq.producer.Producer#send(String, String, Object, long, com.zeh.jungle.mq.common.SerializeEnum, org.apache.rocketmq.client.producer.SendCallback)
     */
    @Override
    public boolean send(String topic, String eventCode, Object payload, long timeout, SerializeEnum serialize, SendCallback sendCallback) throws MQException {
        return send(topic, eventCode, payload, timeout, null, serialize, sendCallback);
    }

    /** 
     * @see com.zeh.jungle.mq.producer.Producer#send(String, String, Object, long, Map, org.apache.rocketmq.client.producer.SendCallback)
     */
    @Override
    public boolean send(String topic, String eventCode, Object payload, long timeout, Map<String, String> props, SendCallback sendCallback) throws MQException {
        return send(topic, eventCode, payload, timeout, props, null, sendCallback);
    }

    /** 
     * @see com.zeh.jungle.mq.producer.Producer#send(String, String, Object, Map, com.zeh.jungle.mq.common.SerializeEnum, org.apache.rocketmq.client.producer.SendCallback)
     */
    @Override
    public boolean send(String topic, String eventCode, Object payload, Map<String, String> props, SerializeEnum serialize, SendCallback sendCallback) throws MQException {
        return send(topic, eventCode, payload, -1, props, serialize, sendCallback);
    }

    /** 
     * @see com.zeh.jungle.mq.producer.Producer#send(String, String, Object, long, Map, com.zeh.jungle.mq.common.SerializeEnum, org.apache.rocketmq.client.producer.SendCallback)
     */
    @Override
    public boolean send(String topic, String eventCode, Object payload, long timeout, Map<String, String> props, SerializeEnum serialize,
                        SendCallback sendCallback) throws MQException {
        Assert.notNull(StringUtils.isNotBlank(topic), "Topic is required");
        Assert.notNull(StringUtils.isNotBlank(eventCode), "Event code is required");
        Assert.notNull(payload, "Payload cannot be null");
        Assert.notNull(sendCallback, "SendCallback cannot be null");
        UniformEvent event = uniformEventPublisher.createUniformEvent(topic, eventCode, false, payload);
        event.setTimeout(timeout);
        event.setSerialize(serialize);
        event.setSendCallback(sendCallback);
        event.addProperties(props);
        return uniformEventPublisher.publishUniformEvent(event);
    }

    /** 
     * @see com.zeh.jungle.mq.producer.Producer#send(String, String, Object, long, int, Map, com.zeh.jungle.mq.common.SerializeEnum)
     */
    @Override
    public boolean send(String topic, String eventCode, Object payload, long timeout, int delayTimeLevel, Map<String, String> props, SerializeEnum serialize) throws MQException {
        Assert.notNull(StringUtils.isNotBlank(topic), "Topic is required");
        Assert.notNull(StringUtils.isNotBlank(eventCode), "Event code is required");
        Assert.notNull(payload, "Payload cannot be null");
        Assert.notNull(serialize, "SerializeEnum cannot be null");
        UniformEvent event = uniformEventPublisher.createUniformEvent(topic, eventCode, false, payload);
        event.setTimeout(timeout);
        event.setSerialize(serialize);
        event.setDelayTimeLevel(delayTimeLevel);
        event.addProperties(props);
        return uniformEventPublisher.publishUniformEvent(event);
    }

    /**
     * Setter for UniformEventPublisher
     * 
     * @param uniformEventPublisher
     */
    @Override
    public void setUniformEventPublisher(UniformEventPublisher uniformEventPublisher) {
        this.uniformEventPublisher = uniformEventPublisher;
    }
}
