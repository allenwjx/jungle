package com.zeh.jungle.mq.consumer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.zeh.jungle.mq.common.DefaultUniformEvent;
import com.zeh.jungle.mq.common.SerializeEnum;
import com.zeh.jungle.mq.common.UniformEvent;
import com.zeh.jungle.mq.common.UniformEventContext;
import com.zeh.jungle.mq.error.MQErrorFactory;
import com.zeh.jungle.mq.exception.MQException;
import com.zeh.jungle.mq.exception.SerializationException;
import com.zeh.jungle.mq.serialize.ByteArraySerialization;
import com.zeh.jungle.utils.common.LoggerUtils;
import com.zeh.jungle.utils.net.IpUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.alibaba.dubbo.common.serialize.ObjectInput;

/**
 * 
 * @author allen
 * @version $Id: DefaultUniformEventSubscriber.java, v 0.1 2016年3月2日 下午4:07:05 allen Exp $
 */
public class DefaultUniformEventSubscriber implements UniformEventSubscriber {

    /** logger */
    private static final Logger         logger                           = LoggerFactory.getLogger(DefaultUniformEventSubscriber.class);

    /** 错误工厂 */
    private static final MQErrorFactory errorFactory                     = MQErrorFactory.getInstance();
    private static final int            DEFAULT_CONSUME_THREAD_MAX       = 64;
    private static final int            DEFAULT_CONSUME_THREAD_MIN       = 20;
    private static final int            DEFAULT_PULL_BATCH_SIZE          = 32;
    private static final int            DEFAULT_PULL_THRESHOLD_FOR_QUEUE = 1000;

    /** RocketMQ consumer */
    private DefaultMQPushConsumer       consumer;

    /** 消息分组 */
    private String                      group;

    /** 命名服务器地址 */
    private String                      nameSrvAddress;

    /** 从队列的哪里开始消费 */
    private ConsumeFromWhere            consumeFromWhere;

    /** 消费模型 */
    private MessageModel                messageModel;

    /** 统一消息事件监听器 */
    private UniformEventMessageListener listener;

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

    /**
     * @param group
     * @param nameSrvAddress
     */
    public DefaultUniformEventSubscriber(String group, String nameSrvAddress) {
        this(group, nameSrvAddress, ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET, MessageModel.CLUSTERING, DEFAULT_CONSUME_THREAD_MAX, DEFAULT_CONSUME_THREAD_MIN,
            DEFAULT_PULL_BATCH_SIZE, 0, DEFAULT_PULL_THRESHOLD_FOR_QUEUE);
    }

    /**
     * @param group
     * @param nameSrvAddress
     * @param consumeFromWhere
     * @param messageModel
     */
    public DefaultUniformEventSubscriber(String group, String nameSrvAddress, ConsumeFromWhere consumeFromWhere, MessageModel messageModel, int consumeThreadMax,
                                         int consumeThreadMin, int pullBatchSize, int pullInterval, int pullThresholdForQueue) {
        this.group = group;
        this.nameSrvAddress = nameSrvAddress;
        this.consumeFromWhere = consumeFromWhere;
        this.messageModel = messageModel;
        this.consumeThreadMax = consumeThreadMax;
        this.consumeThreadMin = consumeThreadMin;
        this.pullBatchSize = pullBatchSize;
        this.pullInterval = pullInterval;
        this.pullThresholdForQueue = pullThresholdForQueue;
        consumer = new DefaultMQPushConsumer(group);
    }

    /** 
     * @see com.zeh.jungle.mq.common.BaseUniformEventProcessor#start()
     */
    @Override
    public void start() throws MQException {
        Assert.notNull(listener, "统一消息事件监听器UniformEventMessageListener未设置");
        consumer.setNamesrvAddr(nameSrvAddress);
        consumer.setConsumeFromWhere(getConsumeFromWhere());
        consumer.setMessageModel(getMessageModel());

        consumer.setConsumeThreadMax(getConsumeThreadMax());
        consumer.setConsumeThreadMin(getConsumeThreadMin());
        consumer.setPullBatchSize(getPullBatchSize());
        consumer.setPullInterval(getPullInterval());
        consumer.setPullThresholdForQueue(getPullThresholdForQueue());

        // register listener
        if (listener.getListenerType() == UniformEventMessageListener.ListenerTypeEnum.CONCURRENTLY) {
            consumer.registerMessageListener(new SofMessageListenerConcurrently(listener));
        } else if (listener.getListenerType() == UniformEventMessageListener.ListenerTypeEnum.ORDERLY) {
            consumer.registerMessageListener(new SofMessageListenerOrderly(listener));
        } else {
            throw new IllegalArgumentException("统一消息事件监听器类型不支持，类型：" + listener.getListenerType());
        }

        // start consumer
        try {
            consumer.start();
            LoggerUtils.info(logger, "Turbo消费者注册成功 nameSrvAddress={}, group={}", nameSrvAddress, group);
        } catch (MQClientException e) {
            throw new MQException(errorFactory.subscribeStartError(group, nameSrvAddress), e);
        }
    }

    /** 
     * @see com.zeh.jungle.mq.common.BaseUniformEventProcessor#shutdown()
     */
    @Override
    public void shutdown() throws MQException {
        consumer.shutdown();
    }

    /** 
     * @see com.zeh.jungle.mq.common.BaseUniformEventProcessor#getGroup()
     */
    @Override
    public String getGroup() {
        return group;
    }

    /** 
     * @see com.zeh.jungle.mq.common.BaseUniformEventProcessor#getNameSrvAddress()
     */
    @Override
    public String getNameSrvAddress() {
        return nameSrvAddress;
    }

    /**
     * @see com.zeh.jungle.mq.consumer.UniformEventSubscriber#getConsumeFromWhere()
     */
    @Override
    public ConsumeFromWhere getConsumeFromWhere() {
        if (consumeFromWhere == null) {
            consumeFromWhere = ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET;
        }
        return consumeFromWhere;
    }

    /**
     * @see com.zeh.jungle.mq.consumer.UniformEventSubscriber#getMessageModel()
     */
    @Override
    public MessageModel getMessageModel() {
        if (messageModel == null) {
            messageModel = MessageModel.CLUSTERING;
        }
        return messageModel;
    }

    /** 
     * @see com.zeh.jungle.mq.consumer.UniformEventSubscriber#registerUniformEventMessageListener(com.zeh.jungle.mq.consumer.UniformEventMessageListener)
     */
    @Override
    public void registerUniformEventMessageListener(UniformEventMessageListener listener) {
        this.listener = listener;
    }

    /** 
     * @see com.zeh.jungle.mq.consumer.UniformEventSubscriber#subscribe(String, String)
     */
    @Override
    public void subscribe(String topic, String eventId) throws MQException {
        try {
            consumer.subscribe(topic, eventId);
            LoggerUtils.info(logger, "Turbo 订阅消费者成功 nameSrvAddress={}, group={},topic={}", nameSrvAddress, group, topic);
        } catch (MQClientException e) {
            throw new MQException(errorFactory.subscribeError(topic, eventId), e);
        }
    }

    /** 
     * @see com.zeh.jungle.mq.consumer.UniformEventSubscriber#subscribe(String, String, String)
     */
    @Override
    public void subscribe(String topic, String fullClassName, String filterClassSource) throws MQException {
        try {
            consumer.subscribe(topic, fullClassName, filterClassSource);
        } catch (MQClientException e) {
            throw new MQException(errorFactory.subscribeError(topic, ""), e);
        }
    }

    /** 
     * @see com.zeh.jungle.mq.consumer.UniformEventSubscriber#resume()
     */
    @Override
    public void resume() {
        consumer.resume();
    }

    /** 
     * @see com.zeh.jungle.mq.consumer.UniformEventSubscriber#suspend()
     */
    @Override
    public void suspend() {
        consumer.suspend();
    }

    /**
     * 并发消费消息
     * RocketMQ监听器实现
     * 
     * @author allen
     * @version $Id: DefaultUniformEventSubscriber.java, v 0.1 2016年3月2日 下午4:50:05 allen Exp $
     */
    private class SofMessageListenerConcurrently implements MessageListenerConcurrently {

        /** 统一消息事件监听器 */
        private UniformEventMessageListener listener;

        /**
         * @param listener
         */
        public SofMessageListenerConcurrently(UniformEventMessageListener listener) {
            super();
            this.listener = listener;
        }

        /** 
         * @see org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently#consumeMessage(List, org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext)
         */
        @Override
        public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
            LoggerUtils.info(logger, "[消费者监听器] >>> Incoming ...");
            UniformEventContext ueCtx = new UniformEventContext(context, null);

            if (!checkNeedConsumeMessage(msgs, ueCtx)) {
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }

            try {
                UniformEvent event = resolveMessage(msgs, ueCtx);
                boolean success = listener.onUniformEvent(event, ueCtx);
                if (success) {
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                } else {
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            } catch (SerializationException e) {
                // 序列化失败，放弃该消息处理，通知Broker消费成功
                logger.error(e.getMessage(), e);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            } finally {
                LoggerUtils.info(logger, "[消费者监听器] <<< Exit ...");
            }
        }

        private boolean checkNeedConsumeMessage(List<MessageExt> msgs, UniformEventContext ctx) {
            MessageExt message = msgs.get(0);
            String targetIp = message.getUserProperty(UniformEvent.TARGET_IP);
            if (StringUtils.isBlank(targetIp)) {
                return true;
            }
            String localIp = IpUtils.getLocalHostIp();
            LoggerUtils.warn(logger, "用户设置指定消费服务器消费消息，指定IP={}，消费者服务器IP={}", targetIp, localIp);
            return StringUtils.equals(targetIp, localIp);
        }

    }

    /**
     * 按序消费消息
     * 
     * @author allen
     * @version $Id: DefaultUniformEventSubscriber.java, v 0.1 2016年3月2日 下午4:52:26 allen Exp $
     */
    private class SofMessageListenerOrderly implements MessageListenerOrderly {
        /** 统一消息事件监听器 */
        private UniformEventMessageListener listener;

        /**
         * @param listener
         */
        public SofMessageListenerOrderly(UniformEventMessageListener listener) {
            super();
            this.listener = listener;
        }

        /** 
         * @see org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly#consumeMessage(List, org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext)
         */
        @Override
        public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
            UniformEventContext ueCtx = new UniformEventContext(null, context);
            try {
                UniformEvent event = resolveMessage(msgs, ueCtx);
                boolean success = listener.onUniformEvent(event, ueCtx);
                if (success) {
                    return ConsumeOrderlyStatus.SUCCESS;
                } else {
                    return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                }
            } catch (SerializationException e) {
                logger.error(e.getMessage(), e);
                return ConsumeOrderlyStatus.SUCCESS;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
            }
        }

    }

    /**
     * 解析RocketMQ投递过来的消息
     * 
     * @param msgs
     * @param ctx
     * @return
     * @throws SerializationException 
     */
    private UniformEvent resolveMessage(List<MessageExt> msgs, UniformEventContext ctx) throws SerializationException {
        if (CollectionUtils.isEmpty(msgs)) {
            throw new IllegalStateException("消息消费失败，未能正确获取队列中得消息，List<MessageExt>.size()=0");
        }

        // 因为SOF框架在生产者端设定了每次只投递一条消息，所以这里只获取第一条消息进行处理
        MessageExt message = msgs.get(0);
        Object payload = deserialize(message);
        UniformEvent event = new DefaultUniformEvent(message.getTopic(), message.getTags());
        event.setPayload(payload);
        event.setId(message.getUserProperty(UniformEvent.EVENT_ID));
        event.setSerialize(SerializeEnum.getEnumByCode(message.getUserProperty(UniformEvent.SERIALIZATION)));
        event.setMessage(message);

        // 扩展属性
        Map<String, String> properties = message.getProperties();
        event.addProperties(properties);
        return event;
    }

    /**
     * 反序列化RocketMQ数据
     * 
     * @param message
     * @return
     * @throws SerializationException
     */
    private Object deserialize(MessageExt message) throws SerializationException {
        String eventId = message.getUserProperty(UniformEvent.EVENT_ID);
        String serializeCode = message.getUserProperty(UniformEvent.SERIALIZATION);
        SerializeEnum serialization = getSerialization(serializeCode);
        if (serialization == null) {
            LoggerUtils.warn(logger, "无法从消息头中找到序列化方式，直接返回二进制数据, topic={}, eventId={}", message.getTopic(), message.getTags());
            return message.getBody();
        }

        LoggerUtils.debug(logger, "消息消费者使用序列化器：{}", serialization.getCode());

        ByteArrayInputStream bais = new ByteArrayInputStream(message.getBody());
        try {
            ObjectInput input = serialization.getSerialize().deserialize(null, bais);
            if (serialization.getSerialize() instanceof ByteArraySerialization) {
                Object payload = input.readBytes();
                return payload;
            } else {
                Object payload = input.readObject();
                return payload;
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new SerializationException(errorFactory.unserializeError(eventId, serializeCode), e);
        } finally {
            IOUtils.closeQuietly(bais);
        }
    }

    /**
     * 获取序列化类型
     * 
     * @param serCode
     * @return
     */
    private SerializeEnum getSerialization(String serCode) {
        if (StringUtils.isBlank(serCode)) {
            return null;
        }
        SerializeEnum serializeType = SerializeEnum.getEnumByCode(serCode);
        return serializeType;
    }

    /**
     * Getter method for property <tt>consumeThreadMax</tt>.
     * 
     * @return property value of consumeThreadMax
     */
    public int getConsumeThreadMax() {
        if (consumeThreadMax == 0) {
            consumeThreadMax = DEFAULT_CONSUME_THREAD_MAX;
        }
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
        if (consumeThreadMin == 0) {
            consumeThreadMin = DEFAULT_CONSUME_THREAD_MIN;
        }
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
        if (pullBatchSize == 0) {
            pullBatchSize = DEFAULT_PULL_BATCH_SIZE;
        }
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
        if (pullThresholdForQueue == 0) {
            pullThresholdForQueue = DEFAULT_PULL_THRESHOLD_FOR_QUEUE;
        }
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
}
