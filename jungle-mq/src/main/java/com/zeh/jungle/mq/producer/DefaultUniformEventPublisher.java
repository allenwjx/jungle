package com.zeh.jungle.mq.producer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import com.zeh.jungle.core.context.JungleContext;
import com.zeh.jungle.core.context.ShutdownCallback;
import com.zeh.jungle.mq.common.DefaultUniformEvent;
import com.zeh.jungle.mq.common.UniformEvent;
import com.zeh.jungle.mq.error.MQErrorFactory;
import com.zeh.jungle.mq.exception.MQException;
import com.zeh.jungle.mq.serialize.ByteArraySerialization;
import com.zeh.jungle.mq.serialize.DefaultByteArraySerialization;
import com.zeh.jungle.utils.common.LoggerUtils;
import org.apache.commons.io.IOUtils;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.common.serialize.ObjectOutput;
import com.alibaba.dubbo.common.serialize.Serialization;

/**
 * 
 * @author allen
 * @version $Id: DefaultUniformEventPublisher.java, v 0.1 2016年3月2日 上午10:51:01 allen Exp $
 */
public class DefaultUniformEventPublisher implements UniformEventPublisher, ShutdownCallback {

    /** logger */
    private static final Logger   logger                        = LoggerFactory.getLogger(DefaultUniformEventPublisher.class);

    /** 错误工厂 */
    protected MQErrorFactory      errorFactory                  = MQErrorFactory.getInstance();

    /** TurboMQ消息生产者 */
    private DefaultMQProducer     producer;

    /** TurboMQ事务性消息生产者 */
    private TransactionMQProducer transactionalProducer;

    /** 消息分组 */
    private String                group;

    /** 命名服务器地址 */
    private String                nameSrvAddress;

    /** 当消息投递失败，是否重试其他broker */
    private boolean               retryAnotherBrokerWhenNotStore;

    /** 重试次数 */
    private int                   retryTimesWhenSendFailed      = 2;

    /** 消息投递超时时间 */
    private int                   timeout                       = 3000;

    /** 消息体大小 */
    private int                   maxMessageSize                = 128 * 1024;

    /** 客户端回调线程数 */
    private int                   clientCallbackExecutorThreads = Runtime.getRuntime().availableProcessors();

    /**
     * @param group
     */
    public DefaultUniformEventPublisher(String group, String nameSrvAddress) {
        this.group = group;
        this.nameSrvAddress = nameSrvAddress;
        this.producer = new DefaultMQProducer(group);
        this.transactionalProducer = new TransactionMQProducer(group + UniformEvent.TX_GROUP_SUFFIX);
    }

    @Override
    public void start() throws MQException {
        try {
            producer.setNamesrvAddr(nameSrvAddress);
            producer.setRetryAnotherBrokerWhenNotStoreOK(retryAnotherBrokerWhenNotStore);
            producer.setRetryTimesWhenSendFailed(retryTimesWhenSendFailed);
            producer.setSendMsgTimeout(timeout);
            producer.setClientCallbackExecutorThreads(clientCallbackExecutorThreads);
            producer.setMaxMessageSize(maxMessageSize);

            transactionalProducer.setNamesrvAddr(nameSrvAddress);
            transactionalProducer.setRetryAnotherBrokerWhenNotStoreOK(retryAnotherBrokerWhenNotStore);
            transactionalProducer.setRetryTimesWhenSendFailed(retryTimesWhenSendFailed);
            transactionalProducer.setSendMsgTimeout(timeout);
            transactionalProducer.setClientCallbackExecutorThreads(clientCallbackExecutorThreads);
            transactionalProducer.setMaxMessageSize(maxMessageSize);

            producer.start();
            transactionalProducer.start();
            LoggerUtils.info(logger, "启动TurboMQ生产者，Group={}, NameServer={}", this.getGroup(), this.getNameSrvAddress());
        } catch (MQClientException e) {
            throw new MQException(errorFactory.producerStartError(group, nameSrvAddress), e);
        }
    }

    /**
     * @see com.zeh.jungle.mq.common.BaseUniformEventProcessor#shutdown()
     */
    @Override
    public void shutdown() throws MQException {
        producer.shutdown();
        transactionalProducer.shutdown();
        LoggerUtils.info(logger, "停止TurboMQ生产者，Group={}, NameServer={}", this.getGroup(), this.getNameSrvAddress());
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
     * @see com.zeh.jungle.mq.producer.UniformEventPublisher#createUniformEvent(String, String)
     */
    @Override
    public UniformEvent createUniformEvent(String topic, String eventCode) {
        UniformEvent e = new DefaultUniformEvent(topic, eventCode);
        return e;
    }

    /** 
     * @see com.zeh.jungle.mq.producer.UniformEventPublisher#createUniformEvent(String, String, boolean)
     */
    @Override
    public UniformEvent createUniformEvent(String topic, String eventCode, boolean transactional) {
        UniformEvent e = new DefaultUniformEvent(topic, eventCode);
        e.setTransactional(transactional);
        return e;
    }

    /** 
     * @see com.zeh.jungle.mq.producer.UniformEventPublisher#createUniformEvent(String, String, boolean, Object)
     */
    @Override
    public UniformEvent createUniformEvent(String topic, String eventCode, boolean transactional, Object payload) {
        UniformEvent e = new DefaultUniformEvent(topic, eventCode);
        e.setTransactional(transactional);
        e.setPayload(payload);
        return e;
    }

    /** 
     * @see com.zeh.jungle.mq.producer.UniformEventPublisher#publishUniformEvent(com.zeh.jungle.mq.common.UniformEvent)
     */
    @Override
    public boolean publishUniformEvent(UniformEvent event) throws MQException {
        if (event == null) {
            throw new IllegalArgumentException("UniformEvent is null");
        }

        // 消息序列化
        Message message = createTurboMessage(event);

        // 消息投递
        try {
            LoggerUtils.info(logger, "消息发送，统一事件：{}，TurboMQ消息：{}", event, message);
            return doPublishUniformEvent(event, message);
        } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
            throw new MQException(errorFactory.producerError(event.getId()), e);
        }
    }

    /**
     * @see com.zeh.jungle.mq.producer.UniformEventPublisher#publishUniformEventOneway(com.zeh.jungle.mq.common.UniformEvent)
     */
    @Override
    public boolean publishUniformEventOneway(UniformEvent event) throws MQException {
        if (event == null) {
            throw new IllegalArgumentException("UniformEvent is null");
        }

        // 消息序列化
        Message message = createTurboMessage(event);

        // 消息投递
        try {
            LoggerUtils.info(logger, "Oneway消息发送，统一事件：{}，TurboMQ消息：{}", event, message);
            producer.sendOneway(message);
            LoggerUtils.info(logger, "Oneway消息发送成功，事件id：{}", event.getId());
            return true;
        } catch (MQClientException | RemotingException | InterruptedException e) {
            throw new MQException(errorFactory.producerError(event.getId()), e);
        }
    }

    /** 
     * @see com.zeh.jungle.core.context.ShutdownCallback#shutdown(com.zeh.jungle.core.context.JungleContext)
     */
    @Override
    public void shutdown(JungleContext context) {
        try {
            this.shutdown();
        } catch (MQException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Getter method for property <tt>retryAnotherBrokerWhenNotStore</tt>.
     * 
     * @return property value of retryAnotherBrokerWhenNotStore
     */
    public boolean isRetryAnotherBrokerWhenNotStore() {
        return retryAnotherBrokerWhenNotStore;
    }

    /**
     * Setter method for property <tt>retryAnotherBrokerWhenNotStore</tt>.
     * 
     * @param retryAnotherBrokerWhenNotStore value to be assigned to property retryAnotherBrokerWhenNotStore
     */
    public void setRetryAnotherBrokerWhenNotStore(boolean retryAnotherBrokerWhenNotStore) {
        this.retryAnotherBrokerWhenNotStore = retryAnotherBrokerWhenNotStore;
    }

    /**
     * Getter method for property <tt>retryTimesWhenSendFailed</tt>.
     * 
     * @return property value of retryTimesWhenSendFailed
     */
    public int getRetryTimesWhenSendFailed() {
        return retryTimesWhenSendFailed;
    }

    /**
     * Setter method for property <tt>retryTimesWhenSendFailed</tt>.
     * 
     * @param retryTimesWhenSendFailed value to be assigned to property retryTimesWhenSendFailed
     */
    public void setRetryTimesWhenSendFailed(int retryTimesWhenSendFailed) {
        this.retryTimesWhenSendFailed = retryTimesWhenSendFailed;
    }

    /**
     * Getter method for property <tt>timeout</tt>.
     * 
     * @return property value of timeout
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Setter method for property <tt>timeout</tt>.
     * 
     * @param timeout value to be assigned to property timeout
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * Getter method for property <tt>maxMessageSize</tt>.
     * 
     * @return property value of maxMessageSize
     */
    public int getMaxMessageSize() {
        return maxMessageSize;
    }

    /**
     * Setter method for property <tt>maxMessageSize</tt>.
     * 
     * @param maxMessageSize value to be assigned to property maxMessageSize
     */
    public void setMaxMessageSize(int maxMessageSize) {
        this.maxMessageSize = maxMessageSize;
    }

    /**
     * Getter method for property <tt>clientCallbackExecutorThreads</tt>.
     * 
     * @return property value of clientCallbackExecutorThreads
     */
    public int getClientCallbackExecutorThreads() {
        return clientCallbackExecutorThreads;
    }

    /**
     * Setter method for property <tt>clientCallbackExecutorThreads</tt>.
     * 
     * @param clientCallbackExecutorThreads value to be assigned to property clientCallbackExecutorThreads
     */
    public void setClientCallbackExecutorThreads(int clientCallbackExecutorThreads) {
        this.clientCallbackExecutorThreads = clientCallbackExecutorThreads;
    }

    /**
     * 执行消息投递
     * 
     * @param event
     * @param message
     * @return
     * @throws MQClientException
     * @throws RemotingException
     * @throws MQBrokerException
     * @throws InterruptedException
     */
    protected boolean doPublishUniformEvent(UniformEvent event, Message message) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
        SendCallback callback = event.getSendCallback();

        // do publish event
        if (callback != null) {
            // async
            producer.send(message, callback, event.getTimeout());
            LoggerUtils.info(logger, "异步消息发送成功，事件id：{}", event.getId());
            return true;
        }

        // sync
        SendResult sendResult = doSendMessage(event, message);
        if (sendResult == null) {
            LoggerUtils.error(logger, "同步消息发送失败，事件id：{}", event.getId());
            return false;
        }
        if (sendResult.getSendStatus() != SendStatus.SEND_OK) {
            LoggerUtils.error(logger, "同步消息发送失败，事件id：{}，TurboMQ消息ID：{}，错误代码", event.getId(), sendResult.getMsgId(), sendResult.getSendStatus());
            return false;
        }
        LoggerUtils.info(logger, "同步消息发送成功，事件id：{}，TurboMQ消息ID：{}", event.getId(), sendResult.getMsgId());
        return true;
    }

    /**
     * 发送消息
     * 
     * @param event
     * @param message
     * @return
     * @throws MQClientException
     * @throws RemotingException
     * @throws MQBrokerException
     * @throws InterruptedException
     */
    private SendResult doSendMessage(UniformEvent event, Message message) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
        if (event.isTransactional()) {
            return transactionalProducer.sendMessageInTransaction(message, event.getTransactionModel().getTranExecuter(), event.getTransactionModel().getArgs());
        }
        return producer.send(message, event.getTimeout());
    }

    /**
     * Create TurboMQ message
     * 
     * @param event 统一消息事件
     * @return
     * @throws MQException 
     */
    private Message createTurboMessage(UniformEvent event) throws MQException {
        try {
            // 序列化数据
            byte[] data = serialize(event);
            Message message = new Message(event.getTopic(), event.getEventCode(), data);

            // 设置延时消息等级
            if (event.getDelayTimeLevel() > 0) {
                message.setDelayTimeLevel(event.getDelayTimeLevel());
            }
            // 写入用户定义的消息扩展属性
            for (Map.Entry<String, String> prop : event.getProperties().entrySet()) {
                message.putUserProperty(prop.getKey(), prop.getValue());
            }

            // 写入序列化方式
            message.putUserProperty(UniformEvent.SERIALIZATION, event.getSerialize().getCode());
            message.putUserProperty(UniformEvent.EVENT_ID, event.getId());
            return message;
        } catch (IOException | InstantiationException | IllegalAccessException e) {
            throw new MQException(errorFactory.serializeError(event.getId(), event.getSerialize().getCode()), e);
        }
    }

    /**
     * 序列化消息为二进制数据
     * 
     * @param event 统一消息事件
     * @return 序列化后的二进制数据
     * @throws IOException
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */
    private byte[] serialize(UniformEvent event) throws IOException, InstantiationException, IllegalAccessException {
        Serialization serialization = event.getSerialize().getSerialize();
        LoggerUtils.debug(logger, "消息生产者使用序列化器：{}", event.getSerialize().getCode());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutput objOutput = serialization.serialize(null, baos);
            if (serialization instanceof ByteArraySerialization || serialization instanceof DefaultByteArraySerialization) {
                objOutput.writeBytes((byte[]) event.getPayload());
            } else {
                objOutput.writeObject(event.getPayload());
            }
            objOutput.flushBuffer();
            byte[] data = baos.toByteArray();
            return data;
        } finally {
            IOUtils.closeQuietly(baos);
        }
    }
}
