package com.zeh.jungle.core.spring.bean;

import com.zeh.jungle.core.context.JungleContext;
import com.zeh.jungle.core.context.ShutdownCallback;
import com.zeh.jungle.mq.consumer.UniformEventMessageListener;
import com.zeh.jungle.mq.consumer.UniformEventSubscriber;
import com.zeh.jungle.mq.exception.MQException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author allen
 * @version $Id: ConsumerBean.java, v 0.1 2016年3月3日 下午5:10:51 allen Exp $
 */
public final class ConsumerBean implements ShutdownCallback {
    /** logger */
    private static final Logger         logger = LoggerFactory.getLogger(ConsumerBean.class);

    /** 消费者组 */
    private String                      group;

    /** 消息命名服务器 */
    private String                      nameSrvAddress;

    /** 消费方式 */
    private String                      consumeFromWhere;

    /** 消费模式，点对点、广播 */
    private String                      messageModel;

    /** 统一消息事件监听器 */
    private UniformEventMessageListener listener;

    /** 消费者订阅的Channel，Channel由主题、事件id组成 */
    private Channels                    channels;

    /** 统一事件发布器 */
    private UniformEventSubscriber      uniformEventSubscriber;

    /** 最大消费线程数 */
    private int                         consumeThreadMax;

    /** 最小消费线程数 */
    private int                         consumeThreadMin;

    /** 一次拉取消息数 */
    private int                         pullBatchSize;

    /** 拉取消息间隔时间 */
    private int                         pullInterval;

    /** 拉取消息阀值 */
    private int                         pullThresholdForQueue;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getNameSrvAddress() {
        return nameSrvAddress;
    }

    public void setNameSrvAddress(String nameSrvAddress) {
        this.nameSrvAddress = nameSrvAddress;
    }

    public UniformEventMessageListener getListener() {
        return listener;
    }

    public void setListener(UniformEventMessageListener listener) {
        this.listener = listener;
    }

    public Channels getChannels() {
        return channels;
    }

    public void setChannels(Channels channels) {
        this.channels = channels;
    }

    public UniformEventSubscriber getUniformEventSubscriber() {
        return uniformEventSubscriber;
    }

    public void setUniformEventSubscriber(UniformEventSubscriber uniformEventSubscriber) {
        this.uniformEventSubscriber = uniformEventSubscriber;
    }

    public String getConsumeFromWhere() {
        return consumeFromWhere;
    }

    public void setConsumeFromWhere(String consumeFromWhere) {
        this.consumeFromWhere = consumeFromWhere;
    }

    public String getMessageModel() {
        return messageModel;
    }

    public void setMessageModel(String messageModel) {
        this.messageModel = messageModel;
    }

    /**
     * Getter method for property <tt>consumeThreadMax</tt>.
     * 
     * @return property value of consumeThreadMax
     */
    public int getConsumeThreadMax() {
        return consumeThreadMax;
    }

    /**
     * Setter method for property <tt>consumeThreadMax</tt>.
     * 
     * @param consumeThreadMax value to be assigned to property consumeThreadMax
     */
    public void setConsumeThreadMax(int consumeThreadMax) {
        this.consumeThreadMax = consumeThreadMax;
    }

    /**
     * Getter method for property <tt>consumeThreadMin</tt>.
     * 
     * @return property value of consumeThreadMin
     */
    public int getConsumeThreadMin() {
        return consumeThreadMin;
    }

    /**
     * Setter method for property <tt>consumeThreadMin</tt>.
     * 
     * @param consumeThreadMin value to be assigned to property consumeThreadMin
     */
    public void setConsumeThreadMin(int consumeThreadMin) {
        this.consumeThreadMin = consumeThreadMin;
    }

    /**
     * Getter method for property <tt>pullBatchSize</tt>.
     * 
     * @return property value of pullBatchSize
     */
    public int getPullBatchSize() {
        return pullBatchSize;
    }

    /**
     * Setter method for property <tt>pullBatchSize</tt>.
     * 
     * @param pullBatchSize value to be assigned to property pullBatchSize
     */
    public void setPullBatchSize(int pullBatchSize) {
        this.pullBatchSize = pullBatchSize;
    }

    /**
     * Getter method for property <tt>pullInterval</tt>.
     * 
     * @return property value of pullInterval
     */
    public int getPullInterval() {
        return pullInterval;
    }

    /**
     * Setter method for property <tt>pullInterval</tt>.
     * 
     * @param pullInterval value to be assigned to property pullInterval
     */
    public void setPullInterval(int pullInterval) {
        this.pullInterval = pullInterval;
    }

    /**
     * Getter method for property <tt>pullThresholdForQueue</tt>.
     * 
     * @return property value of pullThresholdForQueue
     */
    public int getPullThresholdForQueue() {
        return pullThresholdForQueue;
    }

    /**
     * Setter method for property <tt>pullThresholdForQueue</tt>.
     * 
     * @param pullThresholdForQueue value to be assigned to property pullThresholdForQueue
     */
    public void setPullThresholdForQueue(int pullThresholdForQueue) {
        this.pullThresholdForQueue = pullThresholdForQueue;
    }

    /** 
     * @see com.zeh.jungle.core.context.ShutdownCallback#shutdown(com.zeh.jungle.core.context.JungleContext)
     */
    @Override
    public void shutdown(JungleContext context) {
        if (uniformEventSubscriber != null) {
            try {
                uniformEventSubscriber.shutdown();
            } catch (MQException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

}
