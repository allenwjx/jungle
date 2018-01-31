package com.zeh.jungle.client.factory;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Source;

import com.zeh.jungle.client.converter.StringHttpCharsetMessageConverter;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.util.ClassUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 根据编码字符集生成相应消息转换器工厂
 * 
 * @author allen
 * @version $Id: MessageConverterFactory.java, v 0.1 2016年6月16日 下午9:12:48 wb30644 Exp $
 */
public class MessageConverterFactory {
    private final boolean                               jaxb2Present       = ClassUtils.isPresent("javax.xml.bind.Binder", MessageConverterFactory.class.getClassLoader());

    private final boolean                               jackson2Present    = ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper",
        MessageConverterFactory.class.getClassLoader()) && ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator", MessageConverterFactory.class.getClassLoader());

    private final boolean                               jacksonPresent     = ClassUtils.isPresent("org.codehaus.jackson.map.ObjectMapper",
        MessageConverterFactory.class.getClassLoader()) && ClassUtils.isPresent("org.codehaus.jackson.JsonGenerator", MessageConverterFactory.class.getClassLoader());

    private final List<HttpMessageConverter<?>>         DEFAULT_CONVERTERS = Lists.newArrayList();

    private final Map<Charset, HttpMessageConverter<?>> CHARSET_CONVERTERS = Maps.newHashMap();

    private final HttpMessageConverter<?>               DEFAULT_CHARSET_CONVERTER;

    /**
     * 
     */
    public MessageConverterFactory() {
        DEFAULT_CHARSET_CONVERTER = new StringHttpCharsetMessageConverter();
        DEFAULT_CONVERTERS.add(new ByteArrayHttpMessageConverter());
        DEFAULT_CONVERTERS.add(new ResourceHttpMessageConverter());
        DEFAULT_CONVERTERS.add(new SourceHttpMessageConverter<Source>());
        DEFAULT_CONVERTERS.add(new AllEncompassingFormHttpMessageConverter());
        if (jaxb2Present) {
            DEFAULT_CONVERTERS.add(new Jaxb2RootElementHttpMessageConverter());
        }
        if (jackson2Present) {
            DEFAULT_CONVERTERS.add(new MappingJackson2HttpMessageConverter());
        } else if (jacksonPresent) {
            DEFAULT_CONVERTERS.add(new MappingJacksonHttpMessageConverter());
        }
    }

    /**
     * 获取单例
     * 
     * @return
     */
    public static MessageConverterFactory getInstance() {
        return MessageConverterFactoryHolder.INSTANCE;
    }

    /**
     * 通过编码生成转换器,如果要扩展，可以添加相应的HttpMessageConverter
     * 
     * @param charset
     * @return
     */
    public List<HttpMessageConverter<?>> getMessageConverters(Charset charset) {
        List<HttpMessageConverter<?>> messageConverters = Lists.newArrayList();

        // check the charset message converter whether presented
        HttpMessageConverter<?> charsetConverter = null;
        if (charset != null) {
            charsetConverter = CHARSET_CONVERTERS.get(charset);
            if (charsetConverter == null) {
                charsetConverter = new StringHttpCharsetMessageConverter(charset);
                CHARSET_CONVERTERS.put(charset, charsetConverter);
            }
        } else {
            charsetConverter = DEFAULT_CHARSET_CONVERTER;
        }

        // Add charset message converter
        messageConverters.add(charsetConverter);

        // Add default message converters
        messageConverters.addAll(DEFAULT_CONVERTERS);
        return messageConverters;
    }

    /**
     * 通过编码生成转换器
     * 
     * @return
     */
    public List<HttpMessageConverter<?>> getMessageConverters() {
        return getMessageConverters(null);
    }

    /**
     * @author allen
     * @version $Id: MessageConverterFactory.java, v 0.1 2017年10月12日 上午10:41:17 allen Exp $
     */
    private static class MessageConverterFactoryHolder {
        private static final MessageConverterFactory INSTANCE = new MessageConverterFactory();
    }

}
