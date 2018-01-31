package com.zeh.jungle.mq.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Jungle namespace parser registry
 * 
 * @author allen
 * @version $Id: JungleNamespaceHandler.java, v 0.1 2016年3月3日 下午2:07:37 allen Exp $
 */
public class JungleNamespaceHandler extends NamespaceHandlerSupport {

    /** 
     * @see org.springframework.beans.factory.xml.NamespaceHandler#init()
     */
    @Override
    public void init() {
        registerBeanDefinitionParser("consumer", new ConsumerBeanDefinitionParser());
        registerBeanDefinitionParser("channels", new ChannelsBeanDefinitionParser());
        registerBeanDefinitionParser("channel", new ChannelBeanDefinitionParser());
        registerBeanDefinitionParser("event", new ChannelEventBeanDefinitionParser());
        registerBeanDefinitionParser("publisher", new ProducerBeanDefinitionParser());

    }

}
