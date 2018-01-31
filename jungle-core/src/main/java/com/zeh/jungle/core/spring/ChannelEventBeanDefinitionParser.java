package com.zeh.jungle.core.spring;

import com.zeh.jungle.core.spring.bean.ChannelEvent;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * 
 * @author allen
 * @version $Id: ChannelEventBeanDefinitionParser.java, v 0.1 2016年3月3日 下午7:08:49 allen Exp $
 */
public class ChannelEventBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected Class<ChannelEvent> getBeanClass(Element element) {
        return ChannelEvent.class;
    }

    @Override
    protected String getBeanClassName(Element element) {
        return ChannelEvent.class.getName();
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        builder.addPropertyValue("eventCode", element.getAttribute("eventCode"));
        builder.addPropertyValue("eventType", element.getAttribute("eventType"));
    }

}
