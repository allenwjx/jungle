package com.zeh.jungle.mq.spring;

import java.util.List;

import com.zeh.jungle.mq.spring.bean.Channel;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;


/**
 * 
 * @author allen
 * @version $Id: ChannelBeanDefinitionParser.java, v 0.1 2016年3月3日 下午6:45:17 allen Exp $
 */
public class ChannelBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected Class<Channel> getBeanClass(Element element) {
        return Channel.class;
    }

    @Override
    protected String getBeanClassName(Element element) {
        return Channel.class.getName();
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        builder.addPropertyValue("topic", element.getAttribute("topic"));

        ParserContext nestedCtx = new ParserContext(parserContext.getReaderContext(), parserContext.getDelegate(), builder.getBeanDefinition());
        List<Element> eventEles = DomUtils.getChildElementsByTagName(element, "event");
        ManagedList<Object> nestedEles = new ManagedList<Object>();
        if (CollectionUtils.isNotEmpty(eventEles)) {
            ChannelEventBeanDefinitionParser channelEventBeanDefinitionParser = new ChannelEventBeanDefinitionParser();
            for (Element eventElement : eventEles) {
                nestedEles.add(channelEventBeanDefinitionParser.parse(eventElement, nestedCtx));
            }
            builder.addPropertyValue("events", nestedEles);
        }
    }

}
