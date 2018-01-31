package com.zeh.jungle.mq.producer;

import com.zeh.jungle.mq.common.SerializeEnum;
import com.zeh.jungle.mq.exception.MQException;
import org.apache.rocketmq.client.producer.SendCallback;

import java.util.Map;

/**
 * 消息生产者，对UniformEventPublisher进行封装，提供便利的API接口供应用使用
 * 应用也可以完全单独使用UniformEventPublisher进行消息生产投递
 * 
 * @author allen
 * @version $Id: Producer.java, v 0.1 2016年3月2日 上午10:16:14 allen Exp $
 */
public interface Producer {

    /**
     * 消息投递（Oneway方式）
     * 
     * @param topic 主题
     * @param eventCode 事件码
     * @param payload 业务数据
     * @return
     * @throws MQException
     */
    boolean sendOneway(String topic, String eventCode, Object payload) throws MQException;

    /**
     * 消息投递（Oneway方式）
     * 
     * @param topic 主题
     * @param eventCode 事件码
     * @param payload 业务数据
     * @param serialize 数据序列化方式
     * @return
     * @throws MQException
     */
    boolean sendOneway(String topic, String eventCode, Object payload, SerializeEnum serialize) throws MQException;

    /**
     * 消息投递（同步方式）
     * 
     * @param topic 主题
     * @param eventCode 事件码
     * @param payload 业务数据
     * @return
     * @throws MQException
     */
    boolean send(String topic, String eventCode, Object payload) throws MQException;

    /**
     * 消息投递（同步方式）
     * 
     * @param topic 主题
     * @param eventCode 事件码
     * @param payload 业务数据
     * @param timeout 投递超时时间
     * @return
     * @throws MQException
     */
    boolean send(String topic, String eventCode, Object payload, long timeout) throws MQException;

    /**
     * 消息投递（同步方式）
     * 
     * @param topic 主题
     * @param eventCode 事件码
     * @param payload 业务数据
     * @param serialize 数据序列化方式
     * @return
     * @throws MQException
     */
    boolean send(String topic, String eventCode, Object payload, SerializeEnum serialize) throws MQException;

    /**
     * 消息投递（同步方式）
     * 
     * @param topic 主题
     * @param eventCode 事件码
     * @param payload 业务数据
     * @param props 用户自定义属性
     * @return
     * @throws MQException
     */
    boolean send(String topic, String eventCode, Object payload, Map<String, String> props) throws MQException;

    /**
     * 消息投递（同步方式）
     * 
     * @param topic 主题
     * @param eventCode 事件码
     * @param payload 业务数据
     * @param timeout 超时时间
     * @param serialize 数据序列化方式
     * @return
     * @throws MQException
     */
    boolean send(String topic, String eventCode, Object payload, long timeout, SerializeEnum serialize) throws MQException;

    /**
     * 消息投递（同步方式）
     * 
     * @param topic 主题
     * @param eventCode 事件码
     * @param payload 业务数据
     * @param timeout 超时时间
     * @param props 用户自定义属性
     * @return
     * @throws MQException
     */
    boolean send(String topic, String eventCode, Object payload, long timeout, Map<String, String> props) throws MQException;

    /**
     * 消息投递（同步方式）
     * 
     * @param topic 主题
     * @param eventCode 事件码
     * @param payload 业务数据
     * @param props 用户自定义属性
     * @param serialize 数据序列化方式
     * @return
     * @throws MQException
     */
    boolean send(String topic, String eventCode, Object payload, Map<String, String> props, SerializeEnum serialize) throws MQException;

    /**
     * 消息投递（同步方式）
     * 
     * @param topic 主题
     * @param eventCode 事件码
     * @param payload 业务数据
     * @param timeout 超时时间
     * @param props 用户自定义属性
     * @param serialize 数据序列化方式
     * @return
     * @throws MQException
     */
    boolean send(String topic, String eventCode, Object payload, long timeout, Map<String, String> props, SerializeEnum serialize) throws MQException;

    /**
     * 消息投递（同步方式）
     * 
     * @param topic 主题
     * @param eventCode 事件码
     * @param payload 业务数据
     * @param timeout 超时时间
     * @param props 用户自定义属性
     * @param serialize 数据序列化方式
     * @param delayTimeLevel 延时消息等级
     * @return
     * @throws MQException
     */
    boolean send(String topic, String eventCode, Object payload, long timeout, int delayTimeLevel, Map<String, String> props, SerializeEnum serialize) throws MQException;

    /**
     * 消息投递（异步回调）
     * 
     * @param topic 主题
     * @param eventCode 事件码
     * @param payload 业务数据
     * @param sendCallback 投递回调函数
     * @return
     * @throws MQException
     */
    boolean send(String topic, String eventCode, Object payload, SendCallback sendCallback) throws MQException;

    /**
     * 消息投递（异步回调）
     * 
     * @param topic 主题
     * @param eventCode 事件码
     * @param payload 业务数据
     * @param timeout 超时时间
     * @param sendCallback 发送回调函数
     * @return
     * @throws MQException
     */
    boolean send(String topic, String eventCode, Object payload, long timeout, SendCallback sendCallback) throws MQException;

    /**
     * 消息投递（异步回调）
     * 
     * @param topic 主题
     * @param eventCode 事件码
     * @param payload 业务数据
     * @param serialize 序列化方式
     * @param sendCallback 发送回调函数
     * @return
     * @throws MQException
     */
    boolean send(String topic, String eventCode, Object payload, SerializeEnum serialize, SendCallback sendCallback) throws MQException;

    /**
     * 消息投递（异步回调）
     * 
     * @param topic 主题
     * @param eventCode 事件码
     * @param payload 业务数据
     * @param props 自定义属性
     * @param sendCallback 发送回调函数
     * @return
     * @throws MQException
     */
    boolean send(String topic, String eventCode, Object payload, Map<String, String> props, SendCallback sendCallback) throws MQException;

    /**
     * 消息投递（异步回调）
     * 
     * @param topic 主题
     * @param eventCode 事件码
     * @param payload 业务数据
     * @param timeout 超时时间
     * @param serialize 序列化方式
     * @param sendCallback 发送回调函数
     * @return
     * @throws MQException
     */
    boolean send(String topic, String eventCode, Object payload, long timeout, SerializeEnum serialize, SendCallback sendCallback) throws MQException;

    /**
     * 消息投递（异步回调）
     * 
     * @param topic 主题
     * @param eventCode 事件码
     * @param payload 业务数据
     * @param timeout 超时时间
     * @param props 自定义属性
     * @param sendCallback 发送回调函数
     * @return
     * @throws MQException
     */
    boolean send(String topic, String eventCode, Object payload, long timeout, Map<String, String> props, SendCallback sendCallback) throws MQException;

    /**
     * 消息投递（异步回调）
     * 
     * @param topic 主题
     * @param eventCode 事件码
     * @param payload 业务数据
     * @param props 自定义属性
     * @param serialize 序列化方式
     * @param sendCallback 发送回调函数
     * @return
     * @throws MQException
     */
    boolean send(String topic, String eventCode, Object payload, Map<String, String> props, SerializeEnum serialize, SendCallback sendCallback) throws MQException;

    /**
     * 消息投递（异步回调）
     * 
     * @param topic 主题
     * @param eventCode 事件码
     * @param payload 业务数据
     * @param timeout 超时时间
     * @param props 自定义属性
     * @param serialize 序列化方式
     * @param sendCallback 发送回调函数
     * @return
     * @throws MQException
     */
    boolean send(String topic, String eventCode, Object payload, long timeout, Map<String, String> props, SerializeEnum serialize, SendCallback sendCallback) throws MQException;

    /**
     * 设置UniformEventPublisher
     * 
     * @param uniformEventPublisher
     */
    void setUniformEventPublisher(UniformEventPublisher uniformEventPublisher);

}
