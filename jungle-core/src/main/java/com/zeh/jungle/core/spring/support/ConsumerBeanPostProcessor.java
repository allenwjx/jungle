package com.zeh.jungle.core.spring.support;

import java.util.List;

import com.zeh.jungle.core.spring.bean.Channel;
import com.zeh.jungle.core.spring.bean.ChannelEvent;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.access.BootstrapException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.Assert;

import com.zeh.jungle.core.configuration.AppConfiguration;
import com.zeh.jungle.core.configuration.AppConfigurationAware;
import com.zeh.jungle.core.spring.bean.Channels;
import com.zeh.jungle.core.spring.bean.ConsumerBean;
import com.zeh.jungle.mq.consumer.DefaultUniformEventSubscriber;
import com.zeh.jungle.mq.consumer.UniformEventMessageListener;
import com.zeh.jungle.mq.consumer.UniformEventSubscriber;
import com.zeh.jungle.mq.exception.MQException;
import com.zeh.jungle.utils.common.LoggerUtils;

/**
 * 
 * @author allen
 * @version $Id: ConsumerBeanPostProcessor.java, v 0.1 2016年3月3日 下午8:09:14 allen Exp $
 */
public class ConsumerBeanPostProcessor implements BeanPostProcessor, AppConfigurationAware {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerBeanPostProcessor.class);

    /** Application configuraiton */
    private AppConfiguration    appConfiguration;

    /** 
     * @see BeanPostProcessor#postProcessBeforeInitialization(Object, String)
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /** 
     * @see BeanPostProcessor#postProcessAfterInitialization(Object, String)
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (ConsumerBean.class.isAssignableFrom(bean.getClass())) {
            ConsumerBean cb = ConsumerBean.class.cast(bean);

            // 创建消费者
            UniformEventSubscriber subscriber = createUniformEventSubscriber(cb);
            cb.setUniformEventSubscriber(subscriber);

            // 注册监听器
            UniformEventMessageListener listener = cb.getListener();
            Assert.notNull(listener, "消息消费者的监听器未设定，group：" + cb.getGroup());
            subscriber.registerUniformEventMessageListener(listener);

            // 订阅主题
            subscribe(cb, subscriber);

            // 启动消费者
            try {
                subscriber.start();
            } catch (MQException e) {
                LoggerUtils.error(logger, e.getMessage(), e);
                throw new BootstrapException(e.getMessage(), e);
            }
        }
        return bean;
    }

    /** 
     * @see com.zeh.jungle.core.configuration.AppConfigurationAware#setAppConfiguration(com.zeh.jungle.core.configuration.AppConfiguration)
     */
    @Override
    public void setAppConfiguration(AppConfiguration appConfiguration) {
        this.appConfiguration = appConfiguration;
    }

    /**
     * 创建UniformEventSubscriber
     * 
     * @param cb
     * @return
     */
    private UniformEventSubscriber createUniformEventSubscriber(ConsumerBean cb) {
        Assert.notNull(StringUtils.isNotBlank(cb.getGroup()), "消费者未设定消息分组");
        String nameSrvAddr = getNameSrvAddr(cb);
        ConsumeFromWhere cfw = getConsumeFromWhere(cb.getConsumeFromWhere());
        MessageModel mm = getMessageModel(cb.getMessageModel());
        UniformEventSubscriber subscriber = new DefaultUniformEventSubscriber(cb.getGroup(), nameSrvAddr, cfw, mm, cb.getConsumeThreadMax(), cb.getConsumeThreadMin(),
            cb.getPullBatchSize(), cb.getPullInterval(), cb.getPullThresholdForQueue());
        return subscriber;
    }

    /**
     * 消息订阅
     * 
     * @param cb
     * @param subscriber
     */
    private void subscribe(ConsumerBean cb, UniformEventSubscriber subscriber) {
        Channels channels = cb.getChannels();
        Assert.notNull(channels, "消费者未订阅主题，group：" + cb.getGroup());
        List<Channel> eventChannels = channels.getChannels();
        Assert.notNull(CollectionUtils.isNotEmpty(eventChannels), "消费者未订阅主题，group：" + cb.getGroup());

        for (Channel eventChannel : eventChannels) {
            String topic = eventChannel.getTopic();
            Assert.notNull(StringUtils.isNotBlank(topic), "消费者未订阅主题，group：" + cb.getGroup());

            List<ChannelEvent> channelEvents = eventChannel.getEvents();
            Assert.notNull(CollectionUtils.isNotEmpty(channelEvents), "消费者未订阅任何事件信息，group：" + cb.getGroup());

            // 开始订阅
            for (ChannelEvent channelEvent : channelEvents) {
                String eventCode = channelEvent.getEventCode();
                Assert.notNull(StringUtils.isNotBlank(eventCode), "消费者事件ID未设定，group：" + cb.getGroup());
                try {
                    subscriber.subscribe(topic, eventCode);
                } catch (MQException e) {
                    LoggerUtils.error(logger, e.getMessage(), e);
                    throw new BootstrapException(e.getMessage(), e);
                }
            }

        }
    }

    /**
     * 获取消息命名服务地址
     * 
     * @param cb
     * @return
     */
    private String getNameSrvAddr(ConsumerBean cb) {
        String nameSrvAddr = cb.getNameSrvAddress();
        if (StringUtils.isNotBlank(nameSrvAddr)) {
            return nameSrvAddr;
        }

        nameSrvAddr = appConfiguration.getPropertyValue(AppConfiguration.MQ_NAME_SERVER_ADDR);
        Assert.notNull(StringUtils.isNotBlank(nameSrvAddr), "消息命名服务器地址未设定，group：" + cb.getGroup());
        return nameSrvAddr;
    }

    /**
     * 
     * @param code
     * @return
     */
    private ConsumeFromWhere getConsumeFromWhere(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        return ConsumeFromWhere.valueOf(code);
    }

    /**
     * @param code
     * @return
     */
    private MessageModel getMessageModel(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        return MessageModel.valueOf(code);
    }

}
