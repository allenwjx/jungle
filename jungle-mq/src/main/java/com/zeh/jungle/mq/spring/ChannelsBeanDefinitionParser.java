package com.zeh.jungle.mq.spring;

import java.util.List;

import com.zeh.jungle.mq.spring.bean.Channels;
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
 * @version $Id: ChannelsBeanDefinitionParser.java, v 0.1 2016年3月3日 下午6:36:42 allen Exp $
 */
public class ChannelsBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected Class<Channels> getBeanClass(Element element) {
        return Channels.class;
    }

    @Override
    protected String getBeanClassName(Element element) {
        return Channels.class.getName();
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        ParserContext nestedCtx = new ParserContext(parserContext.getReaderContext(), parserContext.getDelegate(), builder.getBeanDefinition());
        List<Element> channelEles = DomUtils.getChildElementsByTagName(element, "channel");
        ManagedList<Object> nestedEles = new ManagedList<Object>();
        if (CollectionUtils.isNotEmpty(channelEles)) {
            ChannelBeanDefinitionParser channelBeanDefinitionParser = new ChannelBeanDefinitionParser();
            for (Element channelEle : channelEles) {
                nestedEles.add(channelBeanDefinitionParser.parse(channelEle, nestedCtx));
            }
            builder.addPropertyValue("channels", nestedEles);
        }
    }

}
