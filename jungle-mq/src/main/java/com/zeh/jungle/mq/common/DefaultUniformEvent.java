package com.zeh.jungle.mq.common;

import java.util.Map;

import com.zeh.jungle.utils.common.UUID;
import org.apache.commons.collections.MapUtils;

import com.google.common.collect.Maps;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.common.message.Message;

/**
 * 统一消息事件默认实现 
 * 
 * @author allen
 * @version $Id: DefaultUniformEvent.java, v 0.1 2016年3月2日 上午10:38:22 allen Exp $
 */
public class DefaultUniformEvent implements UniformEvent {
    /**  */
    private static final long   serialVersionUID = -5122272782126873814L;

    /** 消息ID */
    private String              id;

    /** 消息主题 */
    private String              topic;

    /** 消息事件码 */
    private String              eventCode;

    /** 消息体 */
    private Object              payload;

    /** 是否是事务消息 */
    private boolean             transactional;

    /** 序列化方式 */
    private SerializeEnum       serialize;

    /** 发送超时时间 */
    private long                timeout;

    private SendCallback        callback;

    /** 用户级别消息附加属性 */
    private Map<String, String> userProps        = Maps.newLinkedHashMap();

    /** 本地事务校验模型 */
    private TransactionModel    transactionModel;

    /** 原生消息 */
    private Message             message;

    /** 延时消息等级 */
    private int                 delayTimeLevel;

    /**
     * @param topic
     * @param eventCode
     */
    public DefaultUniformEvent(String topic, String eventCode) {
        this.topic = topic;
        this.eventCode = eventCode;
        this.id = UUID.generateFormatedTimeBasedUUID();
    }

    /** 
     * @see com.zeh.jungle.core.Identifiable#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /** 
     * @see com.zeh.jungle.core.Identifiable#setId(java.io.Serializable)
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /** 
     * @see com.zeh.jungle.mq.common.UniformEvent#getTopic()
     */
    @Override
    public String getTopic() {
        return topic;
    }

    /** 
     * @see com.zeh.jungle.mq.common.UniformEvent#getEventCode()
     */
    @Override
    public String getEventCode() {
        return eventCode;
    }

    /** 
     * @see com.zeh.jungle.mq.common.UniformEvent#setPayload(Object)
     */
    @Override
    public void setPayload(Object payload) {
        this.payload = payload;
    }

    /**
     * @return
     */
    @Override
    public Object getPayload() {
        return payload;
    }

    /** 
     * @see com.zeh.jungle.mq.common.UniformEvent#addProperty(String, String)
     */
    @Override
    public void addProperty(String propKey, String propVal) {
        userProps.put(propKey, propVal);
    }

    /** 
     * @see com.zeh.jungle.mq.common.UniformEvent#removeProperty(String)
     */
    @Override
    public void removeProperty(String propKey) {
        userProps.remove(propKey);
    }

    /** 
     * @see com.zeh.jungle.mq.common.UniformEvent#addProperties(Map)
     */
    @Override
    public void addProperties(Map<String, String> properties) {
        if (MapUtils.isNotEmpty(properties)) {
            userProps.putAll(properties);
        }
    }

    /**
     * @param key
     * @return
     */
    @Override
    public String getProperty(String key) {
        return userProps.get(key);
    }

    /**
     * 
     * @return
     */
    @Override
    public Map<String, String> getProperties() {
        return userProps;
    }

    /** 
     * @see com.zeh.jungle.mq.common.UniformEvent#setTransactional(boolean)
     */
    @Override
    public void setTransactional(boolean transactional) {
        this.transactional = transactional;
    }

    /** 
     * @see com.zeh.jungle.mq.common.UniformEvent#isTransactional()
     */
    @Override
    public boolean isTransactional() {
        return transactional;
    }

    /**
     * @see com.zeh.jungle.mq.common.UniformEvent#setSerialize(com.zeh.jungle.mq.common.SerializeEnum)
     */
    @Override
    public void setSerialize(SerializeEnum serialize) {
        this.serialize = serialize;
    }

    /**
     * @see com.zeh.jungle.mq.common.UniformEvent#getSerialize()
     */
    @Override
    public SerializeEnum getSerialize() {
        if (serialize == null) {
            // default to kryo
            serialize = SerializeEnum.KRYO;
        }
        return serialize;
    }

    /**
     * @see com.zeh.jungle.mq.common.UniformEvent#setTimeout(long)
     */
    @Override
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    /**
     * @see com.zeh.jungle.mq.common.UniformEvent#getTimeout()
     */
    @Override
    public long getTimeout() {
        timeout = timeout <= 0 ? 3000L : timeout;
        return timeout;
    }

    /**
     * @see com.zeh.jungle.mq.common.UniformEvent#setSendCallback(org.apache.rocketmq.client.producer.SendCallback)
     */
    @Override
    public void setSendCallback(SendCallback callback) {
        this.callback = callback;
    }

    /**
     * @see com.zeh.jungle.mq.common.UniformEvent#getSendCallback()
     */
    @Override
    public SendCallback getSendCallback() {
        return callback;
    }

    /**
     * @see com.zeh.jungle.mq.common.UniformEvent#getTransactionModel()
     */
    @Override
    public TransactionModel getTransactionModel() {
        return transactionModel;
    }

    /**
     * @see com.zeh.jungle.mq.common.UniformEvent#setTransactionModel(com.zeh.jungle.mq.common.TransactionModel)
     */
    @Override
    public void setTransactionModel(TransactionModel transactionModel) {
        this.transactionModel = transactionModel;
    }

    /** 
     * @see com.zeh.jungle.mq.common.UniformEvent#getMessage()
     */
    @Override
    public Message getMessage() {
        return message;
    }

    /** 
     * @see com.zeh.jungle.mq.common.UniformEvent#setMessage(org.apache.rocketmq.common.message.Message)
     */
    @Override
    public void setMessage(Message message) {
        this.message = message;
    }

    /** 
     * @see com.zeh.jungle.mq.common.UniformEvent#setDelayTimeLevel(int)
     */
    @Override
    public void setDelayTimeLevel(int level) {
        this.delayTimeLevel = level;
    }

    /** 
     * @see com.zeh.jungle.mq.common.UniformEvent#getDelayTimeLevel()
     */
    @Override
    public int getDelayTimeLevel() {
        return delayTimeLevel;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(id).append(",");
        sb.append(topic).append(",");
        sb.append(eventCode).append(",");
        sb.append(serialize).append(",");
        sb.append(transactional).append(",");
        sb.append(payload).append(",");
        sb.append(userProps);
        return sb.toString();
    }

}
