package com.zeh.jungle.core.context;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.alibaba.dubbo.common.utils.ConfigUtils;
import com.zeh.jungle.core.configuration.AppConfiguration;
import com.zeh.jungle.core.configuration.AppConfigurationAware;
import com.zeh.jungle.core.configuration.AppConfigurationImpl;
import com.zeh.jungle.core.event.BaseEvent;
import com.zeh.jungle.core.event.BaseListener;
import com.zeh.jungle.core.event.EventMulticaster;
import com.zeh.jungle.core.event.SimpleEventMulticaster;
import com.zeh.jungle.core.spring.bean.Channel;
import com.zeh.jungle.core.spring.bean.ChannelEvent;
import com.zeh.jungle.core.spring.bean.Channels;
import com.zeh.jungle.core.spring.bean.ConsumerBean;
import com.zeh.jungle.mq.consumer.DefaultUniformEventSubscriber;
import com.zeh.jungle.mq.consumer.UniformEventMessageListener;
import com.zeh.jungle.mq.consumer.UniformEventSubscriber;
import com.zeh.jungle.mq.exception.MQException;
import com.zeh.jungle.mq.producer.UniformEventPublisher;
import com.zeh.jungle.utils.common.LoggerUtils;
import com.zeh.jungle.utils.net.IpUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.access.BootstrapException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;

/**
 * 
 * @author allen
 * @version $Id: DefaultJungleContext.java, v 0.1 2016年2月28日 上午12:49:43 allen Exp $
 */
public class DefaultJungleContext implements JungleContext, ApplicationContextAware, BeanPostProcessor {
    /** logger */
    private static final Logger logger                 = LoggerFactory.getLogger(DefaultJungleContext.class);

    private static final String EVENT_MULTICASTER_NAME = "eventMulticaster";

    /** 容器ID */
    private String              id;

    /** 容器实例名称 */
    private String              contextName;

    /** 容器启动时间 */
    private long                startupDate;

    /** Spring上下文 */
    private ApplicationContext  springContext;

    /** 容器运行状态 */
    private boolean             running;

    /** 事件派发器 */
    private EventMulticaster    eventMulticaster;

    /** 应用配置信息 */
    private AppConfiguration    appConfiguration;

    /**
     * 构造函数
     */
    public DefaultJungleContext() {
        this.id = this.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(this));
        this.startupDate = System.currentTimeMillis();
        appConfiguration = loadConfiguration();
    }

    /** 
     * @see com.zeh.jungle.core.context.JungleContext#start()
     */
    @Override
    public synchronized boolean start() {
        if (running) {
            LoggerUtils.warn(logger, "Jungle容器已初始化...");
            return running;
        }
        LoggerUtils.info(logger, "Jungle容器初始化...");
        try {
            initialize();
            setupJungleContext();
            registerListener();
            onContextStartup();
            onProducerStartup();
            onConsumerStartup();
            running = true;
        } catch (Exception e) {
            LoggerUtils.error(logger, e.getMessage(), e);
            running = false;
        }
        return running;
    }

    /**
     * @see com.zeh.jungle.core.context.JungleContext#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /** 
     * @see com.zeh.jungle.core.context.JungleContext#getAppName()
     */
    @Override
    public String getAppName() {
        return appConfiguration.getPropertyValue("dubbo.application.name");
    }

    /** 
     * @see com.zeh.jungle.core.context.JungleContext#getContextName()
     */
    @Override
    public String getContextName() {
        contextName = getAppName() + "@" + id;
        return contextName;
    }

    /** 
     * @see com.zeh.jungle.core.context.JungleContext#getStartupDate()
     */
    @Override
    public long getStartupDate() {
        return startupDate;
    }

    /** 
     * @see com.zeh.jungle.core.context.JungleContext#shutdown()
     */
    @Override
    public void shutdown() {
        onContextShutdown();
    }

    /** 
     * @see com.zeh.jungle.core.context.JungleContext#isRunning()
     */
    @Override
    public boolean isRunning() {
        return running;
    }

    /** 
     * @see com.zeh.jungle.core.context.JungleContext#fireEvent(com.zeh.jungle.core.event.BaseEvent, boolean)
     */
    @Override
    public void fireEvent(BaseEvent event, boolean sync) {
        Assert.notNull(event, "Event instance is cannot be null");
        getEventMulticaster().multicastEvent(event, sync);
    }

    /** 
     * @see com.zeh.jungle.core.context.JungleContext#getEventMulticaster()
     */
    @Override
    public EventMulticaster getEventMulticaster() {
        if (eventMulticaster == null) {
            // Get EventMulticaster from spring container
            eventMulticaster = springContext.getBean(EVENT_MULTICASTER_NAME, EventMulticaster.class);

            // Create default EventMulticaster if bean is not prepared
            if (eventMulticaster == null) {
                eventMulticaster = new SimpleEventMulticaster();
            }
        }
        return eventMulticaster;
    }

    /** 
     * @see com.zeh.jungle.core.context.JungleContext#setEventMulticaster(com.zeh.jungle.core.event.EventMulticaster)
     */
    @Override
    public void setEventMulticaster(EventMulticaster eventMulticaster) {
        this.eventMulticaster = eventMulticaster;
    }

    /** 
     * @see com.zeh.jungle.core.context.ListenerRegister#registerListener()
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void registerListener() {
        LoggerUtils.info(logger, "[Jungle] Register listeners ...");
        Map<String, BaseListener> beans = springContext.getBeansOfType(BaseListener.class, false, true);
        if (beans != null) {
            for (BaseListener bean : beans.values()) {
                LoggerUtils.info(logger, "[Jungle] Executing listener register {}", bean.getClass().getName());
                getEventMulticaster().addListener(bean);
            }
        }
    }

    /** 
     * @see com.zeh.jungle.core.context.ListenerRegister#registerListener(com.zeh.jungle.core.event.BaseListener)
     */
    @Override
    public void registerListener(BaseListener<BaseEvent> listener) {
        if (listener != null) {
            getEventMulticaster().addListener(listener);
        }
    }

    /** 
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.springContext = applicationContext;
    }

    /**
     * @see com.zeh.jungle.core.context.JungleContext#getAppConfiguration()
     */
    @Override
    public AppConfiguration getAppConfiguration() {
        return appConfiguration;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        // no-op
        return bean;
    }

    /**
     * AppConfiguration setup when container initialized
     * 
     * @see BeanPostProcessor#postProcessAfterInitialization(Object, String)
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (AppConfigurationAware.class.isAssignableFrom(bean.getClass())) {
            AppConfigurationAware.class.cast(bean).setAppConfiguration(appConfiguration);
            LoggerUtils.info(logger, "[Jungle] Set application configuration to bean {}", bean.getClass().getName());
        }
        return bean;
    }

    /**
     * 执行容器初始化
     */
    protected void initialize() {
        LoggerUtils.info(logger, "[Jungle] Context is initializing ...");
        Map<String, JungleContextInitializationAware> beans = springContext.getBeansOfType(JungleContextInitializationAware.class, false, true);
        if (beans != null) {
            for (JungleContextInitializationAware bean : beans.values()) {
                LoggerUtils.info(logger, "[Jungle] Executing bean initialization {}", bean.getClass().getName());
                bean.init();
            }
        }
    }

    /**
     * 注入SOF上下文
     */
    protected void setupJungleContext() {
        LoggerUtils.info(logger, "[Jungle] Setup Jungle context ...");
        Map<String, JungleContextAware> beans = springContext.getBeansOfType(JungleContextAware.class, false, true);
        if (beans != null) {
            for (JungleContextAware bean : beans.values()) {
                LoggerUtils.info(logger, "[Jungle] Set Jungle context to bean {}", bean.getClass().getName());
                bean.setJungleContext(this);
            }
        }
    }

    /**
     * 执行容器启动后处理
     */
    protected void onContextStartup() {
        LoggerUtils.info(logger, "[Jungle] Executing Jungle context startup callback ...");
        Map<String, StartupCallback> beans = springContext.getBeansOfType(StartupCallback.class, false, true);
        if (beans != null) {
            List<StartupCallback> callbacks = Lists.newArrayList();
            callbacks.addAll(beans.values());
            Collections.sort(callbacks, new Comparator<StartupCallback>() {
                @Override
                public int compare(StartupCallback o1, StartupCallback o2) {
                    if (o1.getOrder() > o2.getOrder()) {
                        return 1;
                    } else if (o1.getOrder() < o2.getOrder()) {
                        return -1;
                    }
                    return 0;
                }
            });
            for (StartupCallback bean : callbacks) {
                LoggerUtils.info(logger, "[Jungle] Executing startup callback {}", bean.getClass().getName());
                bean.startup(this);
            }
        }
    }

    /**
     * 执行MQ生产者启动
     */
    protected void onProducerStartup() {
        LoggerUtils.info(logger, "[Jungle] Executing producer startup for RocketMQ ...");
        Map<String, UniformEventPublisher> beans = springContext.getBeansOfType(UniformEventPublisher.class, false, true);
        if (beans != null) {
            try {
                for (UniformEventPublisher bean : beans.values()) {
                    bean.start();
                }
            } catch (MQException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

    /**
     * 执行MQ消费者启动
     */
    protected void onConsumerStartup() {
        LoggerUtils.info(logger, "[Jungle] Executing consumer startup for RocketMQ ...");
        Map<String, ConsumerBean> beans = springContext.getBeansOfType(ConsumerBean.class, false, true);
        if (beans != null) {
            try {
                for (ConsumerBean bean : beans.values()) {
                    // 创建消费者
                    UniformEventSubscriber subscriber = createUniformEventSubscriber(bean);
                    bean.setUniformEventSubscriber(subscriber);

                    // 注册监听器
                    UniformEventMessageListener listener = bean.getListener();
                    Assert.notNull(listener, "消息消费者的监听器未设定，group：" + bean.getGroup());
                    subscriber.registerUniformEventMessageListener(listener);

                    // 订阅主题
                    subscribe(bean, subscriber);

                    // 启动消费者
                    subscriber.start();
                }
            } catch (MQException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

    /**
     * 执行容器关闭前处理
     */
    protected void onContextShutdown() {
        LoggerUtils.info(logger, "[Jungle] Executing Jungle context shutdown callback ...");
        Map<String, ShutdownCallback> beans = springContext.getBeansOfType(ShutdownCallback.class, false, true);
        if (beans != null) {
            for (ShutdownCallback bean : beans.values()) {
                LoggerUtils.info(logger, "[Jungle] Executing shutdown callback {}", bean.getClass().getName());
                bean.shutdown(this);
            }
        }
    }

    /**
     * Load application configuration
     * 
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected AppConfiguration loadConfiguration() {
        Properties props = ConfigUtils.getProperties();
        AppConfiguration configuration = new AppConfigurationImpl(((Map) props));
        configuration.addProperty(AppConfiguration.SYS_IP, IpUtils.getLocalHostIp());
        configuration.addProperty(AppConfiguration.SYS_HOST_NAME, IpUtils.getLocalHostName());
        printConfiguration(configuration);
        return configuration;
    }

    /**
     * 输出配置信息
     * 
     * @param conf
     */
    private void printConfiguration(AppConfiguration conf) {
        for (Map.Entry<String, String> prop : conf.getConfig().entrySet()) {
            LoggerUtils.info(logger, "Application configuration:{}={}", prop.getKey(), prop.getValue());
        }
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
