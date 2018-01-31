package com.zeh.jungle.mq.spring.support;

import com.google.common.collect.Lists;
import com.zeh.jungle.core.configuration.AppConfiguration;
import com.zeh.jungle.core.context.JungleContext;
import com.zeh.jungle.core.context.ShutdownCallback;
import com.zeh.jungle.core.context.StartupCallback;
import com.zeh.jungle.mq.consumer.DefaultUniformEventSubscriber;
import com.zeh.jungle.mq.consumer.UniformEventMessageListener;
import com.zeh.jungle.mq.consumer.UniformEventSubscriber;
import com.zeh.jungle.mq.exception.MQException;
import com.zeh.jungle.mq.spring.bean.Channel;
import com.zeh.jungle.mq.spring.bean.ChannelEvent;
import com.zeh.jungle.mq.spring.bean.Channels;
import com.zeh.jungle.mq.spring.bean.ConsumerBean;
import com.zeh.jungle.utils.common.LoggerUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.access.BootstrapException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

/**
 * @author allen
 * @create $ ID: ConsumerStartupCallback, 18/1/31 17:41 allen Exp $
 * @since 1.0.0
 */
@Service
public class ConsumerStartupCallback implements StartupCallback, ShutdownCallback {
    /** logger */
    private static final Logger                       LOGGER      = LoggerFactory.getLogger(ConsumerStartupCallback.class);
    /** 消费者 */
    private static final List<UniformEventSubscriber> subscribers = Lists.newArrayList();

    /**
     * 执行容器启动后的后处理操作
     *
     * @param context Jungle上下文
     */
    @Override
    public void startup(JungleContext context) {
        LoggerUtils.info(LOGGER, "[Jungle] Executing consumer startup for RocketMQ ...");
        Map<String, ConsumerBean> beans = context.getSpringContext().getBeansOfType(ConsumerBean.class, false, true);
        if (beans != null) {
            try {
                for (ConsumerBean bean : beans.values()) {
                    // 创建消费者
                    UniformEventSubscriber subscriber = createUniformEventSubscriber(context, bean);
                    bean.setUniformEventSubscriber(subscriber);

                    // 注册监听器
                    UniformEventMessageListener listener = bean.getListener();
                    Assert.notNull(listener, "消息消费者的监听器未设定，group：" + bean.getGroup());
                    subscriber.registerUniformEventMessageListener(listener);

                    // 订阅主题
                    subscribe(bean, subscriber);

                    // 启动消费者
                    subscriber.start();
                    subscribers.add(subscriber);
                }
            } catch (MQException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

    /**
     * 获取顺序
     *
     * @return
     */
    @Override
    public int getOrder() {
        return 1;
    }

    /**
     * 在容器销毁前执行前处理操作
     *
     * @param context 上下文
     */
    @Override
    public void shutdown(JungleContext context) {
        LoggerUtils.info(LOGGER, "[Jungle] Executing consumer shutdown for RocketMQ ...");
        for (UniformEventSubscriber subscriber : subscribers) {
            try {
                subscriber.shutdown();
            } catch (MQException e) {
                LoggerUtils.error(LOGGER, e.getMessage(), e);
            }
        }
    }

    /**
     * 创建UniformEventSubscriber
     *
     * @param context
     * @param cb
     * @return
     */
    private UniformEventSubscriber createUniformEventSubscriber(JungleContext context, ConsumerBean cb) {
        Assert.notNull(StringUtils.isNotBlank(cb.getGroup()), "消费者未设定消息分组");
        String nameSrvAddr = getNameSrvAddr(context, cb);
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
                    LoggerUtils.error(LOGGER, e.getMessage(), e);
                    throw new BootstrapException(e.getMessage(), e);
                }
            }

        }
    }

    /**
     * 获取消息命名服务地址
     * 
     * @param context
     * @param cb
     * @return
     */
    private String getNameSrvAddr(JungleContext context, ConsumerBean cb) {
        String nameSrvAddr = cb.getNameSrvAddress();
        if (StringUtils.isNotBlank(nameSrvAddr)) {
            return nameSrvAddr;
        }

        nameSrvAddr = context.getAppConfiguration().getPropertyValue(AppConfiguration.MQ_NAME_SERVER_ADDR);
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
