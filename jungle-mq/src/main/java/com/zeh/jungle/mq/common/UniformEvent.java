package com.zeh.jungle.mq.common;

import com.zeh.jungle.core.Identifiable;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.common.message.Message;

import java.util.Map;

/**
 * 统一事件模型，封装消息中间件的消息载体
 * 
 * @author allen
 * @version $Id: UniformEvent.java, v 0.1 2016年3月2日 上午10:28:45 allen Exp $
 */
public interface UniformEvent extends Identifiable<String> {
    /** 事务消息分组后缀 */
    static final String TX_GROUP_SUFFIX = "_tx";

    /** 消息扩展属性key：序列化方式 */
    static final String SERIALIZATION   = "jg_serialization";

    /** 消息扩展属性key：事件ID */
    static final String EVENT_ID        = "jg_event_id";

    /** 消息扩展属性，key：指定消费者IP */
    static final String TARGET_IP       = "jg_target_ip";

    /**
     * 获取主题
     * 
     * @return
     */
    String getTopic();

    /**
     * 获取事件码
     * 
     * @return
     */
    String getEventCode();

    /**
     * 设置消息载体
     * 
     * @param payload
     */
    void setPayload(Object payload);

    /**
     * 获取消息载体
     * 
     * @return
     */
    Object getPayload();

    /**
     * 设置消息附加属性，用户级别的属性，如果添加的属性是系统属性，会抛出异常
     * 
     * @param propKey
     * @param propVal
     */
    void addProperty(String propKey, String propVal);

    /**
     * 删除消息附加属性，用户级别的属性，如果删除的属性是系统属性，会抛出异常
     * 
     * @param propKey
     */
    void removeProperty(String propKey);

    /**
     * 添加消息附加属性，用户级别的属性，如果添加的属性是系统属性，会抛出异常
     * 
     * @param properties
     */
    void addProperties(Map<String, String> properties);

    /**
     * 获取用户定义的扩展属性
     * 
     * @param key
     * @return
     */
    String getProperty(String key);

    /**
     * 获取用户定义的扩展属性
     * 
     * @return
     */
    Map<String, String> getProperties();

    /**
     * 设置是否是事务性消息
     * 
     * @param transactional
     */
    void setTransactional(boolean transactional);

    /**
     * 设置序列化方式
     * 
     * @param serialize
     * @return
     */
    void setSerialize(SerializeEnum serialize);

    /**
     * 获取序列化方式
     * 
     * @return
     */
    SerializeEnum getSerialize();

    /**
     * 设置发送超时时间
     * 
     * @param timeout
     */
    void setTimeout(long timeout);

    /**
     * 获取发送超时时间
     * 
     * @return
     */
    long getTimeout();

    /**
     * 设置回调方法，仅同步方式有效
     * 
     * @param callback
     */
    void setSendCallback(SendCallback callback);

    /**
     * 获取回调方法，仅同步方式有效
     * 
     * @return
     */
    SendCallback getSendCallback();

    /**
     * 是否是事务性消息
     * 
     * @return
     */
    boolean isTransactional();

    /**
     * 获取TransactionModel
     * 
     * @return
     */
    TransactionModel getTransactionModel();

    /**
     * 设置TransactionModel
     * 
     * @param transactionModel
     */
    void setTransactionModel(TransactionModel transactionModel);

    /**
     * 获取MQ原生消息
     * 
     * @return
     */
    Message getMessage();

    /**
     * 设置MQ原生消息
     * 
     * @param message
     */
    void setMessage(Message message);

    /**
     * 设置延时消息等级
     * 
     * @param level
     */
    void setDelayTimeLevel(int level);

    /**
     * 获取延时消息等级
     * 
     * @return
     */
    int getDelayTimeLevel();
}
