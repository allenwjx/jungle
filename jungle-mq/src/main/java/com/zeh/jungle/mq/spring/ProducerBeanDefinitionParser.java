package com.zeh.jungle.mq.spring;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

import com.alibaba.dubbo.common.utils.ConfigUtils;
import com.zeh.jungle.mq.producer.DefaultUniformEventPublisher;

/**
 * 
 * @author allen
 * @version $Id: ProducerBeanDefinitionParser.java, v 0.1 2016年3月3日 下午2:20:52 allen Exp $
 */
public class ProducerBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
    /** xml attr: 生产者分组 */
    private static final String GROUP_ELEMENT                      = "group";
    /** xml attr: 消息命名服务器地址 */
    private static final String NAME_SRV_ADDR_ELEMENT              = "nameSrvAddress";
    /** xml attr: 是否重试其他broker */
    private static final String RETRY_ANOTHER_BROKER_WHEN_NO_STORE = "failover";
    /** xml attr: 重试次数 */
    private static final String RETRY_TIMES_WHEN_SEND_FAIL         = "retry";
    /** xml attr: 消息投递超时时间 */
    private static final String TIME_OUT                           = "timeout";
    /** xml attr: 线程池 */
    private static final String CLIENT_CALLBACK_POOL_SIZE          = "poolSize";
    /** xml attr: 消息大小 */
    private static final String MAX_MESSAGE_SIZE                   = "maxMessageSize";
    private static final int    DEFAULT_MAX_MESSAGE_SIZE           = 128 * 1024;

    /**
     * @see AbstractSingleBeanDefinitionParser#getBeanClass(Element)
     */
    @Override
    protected Class<DefaultUniformEventPublisher> getBeanClass(Element element) {
        return DefaultUniformEventPublisher.class;
    }

    /**
     * @see AbstractSingleBeanDefinitionParser#getBeanClassName(Element)
     */
    @Override
    protected String getBeanClassName(Element element) {
        return DefaultUniformEventPublisher.class.getName();
    }

    /**
     * @see AbstractSingleBeanDefinitionParser#doParse(Element, BeanDefinitionBuilder)
     */
    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        String group = element.getAttribute(GROUP_ELEMENT);
        String nameSrvAddress = element.getAttribute(NAME_SRV_ADDR_ELEMENT);
        String failover = element.getAttribute(RETRY_ANOTHER_BROKER_WHEN_NO_STORE);
        String retry = element.getAttribute(RETRY_TIMES_WHEN_SEND_FAIL);
        String timeout = element.getAttribute(TIME_OUT);
        String poolSize = element.getAttribute(CLIENT_CALLBACK_POOL_SIZE);
        String maxMessageSize = element.getAttribute(MAX_MESSAGE_SIZE);

        if (StringUtils.isBlank(nameSrvAddress)) {
            Properties props = ConfigUtils.getProperties();
            nameSrvAddress = props.getProperty("mq.nameSrvAddress");
            if (StringUtils.isBlank(nameSrvAddress)) {
                throw new IllegalStateException("TurboMQ name server address is not provided");
            }
        }

        builder.addConstructorArgValue(group);
        builder.addConstructorArgValue(nameSrvAddress);
        builder.addPropertyValue("retryAnotherBrokerWhenNotStore", StringUtils.defaultIfBlank(failover, "true"));
        builder.addPropertyValue("retryTimesWhenSendFailed", StringUtils.defaultIfBlank(retry, "2"));
        builder.addPropertyValue("timeout", StringUtils.defaultIfBlank(timeout, "3000"));
        builder.addPropertyValue("maxMessageSize", StringUtils.defaultIfBlank(maxMessageSize, String.valueOf(DEFAULT_MAX_MESSAGE_SIZE)));
        builder.addPropertyValue("clientCallbackExecutorThreads", StringUtils.defaultIfBlank(poolSize, String.valueOf(Runtime.getRuntime().availableProcessors())));
    }
}
