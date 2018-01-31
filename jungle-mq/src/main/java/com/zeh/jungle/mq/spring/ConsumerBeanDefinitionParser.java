package com.zeh.jungle.mq.spring;

import com.zeh.jungle.mq.spring.bean.ConsumerBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;


/**
 * 
 * @author allen
 * @version $Id: ConsumerBeanDefinitionParser.java, v 0.1 2016年3月3日 下午2:18:18 allen Exp $
 */
public class ConsumerBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
    private static final String DEFAULT_CONSUME_THREAD_MAX       = "64";
    private static final String DEFAULT_CONSUME_THREAD_MIN       = "20";
    private static final String DEFAULT_PULL_BATCH_SIZE          = "32";
    private static final String DEFAULT_PULL_INTERVAL            = "0";
    private static final String DEFAULT_PULL_THRESHOLD_FOR_QUEUE = "1000";

    /**
     * @see AbstractSingleBeanDefinitionParser#getBeanClass(Element)
     */
    @Override
    protected Class<ConsumerBean> getBeanClass(Element element) {
        return ConsumerBean.class;
    }

    /**
     * @see AbstractSingleBeanDefinitionParser#getBeanClassName(Element)
     */
    @Override
    protected String getBeanClassName(Element element) {
        return ConsumerBean.class.getName();
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        builder.addPropertyValue("group", element.getAttribute("group"));
        builder.addPropertyValue("nameSrvAddress", element.getAttribute("nameSrvAddress"));
        builder.addPropertyValue("consumeFromWhere", element.getAttribute("consumeFromWhere"));
        builder.addPropertyValue("messageModel", element.getAttribute("messageModel"));
        builder.addPropertyValue("consumeThreadMax", StringUtils.defaultIfBlank(element.getAttribute("consumeThreadMax"), DEFAULT_CONSUME_THREAD_MAX));
        builder.addPropertyValue("consumeThreadMin", StringUtils.defaultIfBlank(element.getAttribute("consumeThreadMin"), DEFAULT_CONSUME_THREAD_MIN));
        builder.addPropertyValue("pullBatchSize", StringUtils.defaultIfBlank(element.getAttribute("pullBatchSize"), DEFAULT_PULL_BATCH_SIZE));
        builder.addPropertyValue("pullInterval", StringUtils.defaultIfBlank(element.getAttribute("pullInterval"), DEFAULT_PULL_INTERVAL));
        builder.addPropertyValue("pullThresholdForQueue", StringUtils.defaultIfBlank(element.getAttribute("pullThresholdForQueue"), DEFAULT_PULL_THRESHOLD_FOR_QUEUE));

        ParserContext nestedCtx = new ParserContext(parserContext.getReaderContext(), parserContext.getDelegate(), builder.getBeanDefinition());

        // Listener
        Element listenerEle = DomUtils.getChildElementByTagName(element, "listener");
        if (listenerEle != null) {
            builder.addPropertyReference("listener", listenerEle.getAttribute("ref"));
        }

        // Channels
        Element channelsEle = DomUtils.getChildElementByTagName(element, "channels");
        if (channelsEle != null) {
            ChannelsBeanDefinitionParser channelsBeanDefinitionParser = new ChannelsBeanDefinitionParser();
            builder.addPropertyValue("channels", channelsBeanDefinitionParser.parse(channelsEle, nestedCtx));
        }
    }
}
