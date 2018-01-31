package com.zeh.jungle.client.serializer;

import java.util.TimeZone;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.thoughtworks.xstream.converters.basic.DoubleConverter;
import com.thoughtworks.xstream.converters.basic.IntConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * The type X stream serializer.
 *
 * @author allen
 * @version $Id : XStreamSerializer.java, v 0.1 2016年5月19日 下午1:35:01 allen Exp $
 */
public class XStreamSerializer implements Serializer {
    /** XStream */
    private XStream xstream;

    /**
     * Instantiates a new X stream serializer.
     */
    private XStreamSerializer() {
        if (xstream == null) {
            xstream = new XStream(new DomDriver("utf8"));
            xstream.registerConverter(new DateConverter("yyyy-MM-dd HH:mm:ss", new String[] { "yyyy/MM/dd HH:mm:ss", "yyyy-MM-dd" }, TimeZone.getTimeZone("GMT+8")));
            xstream.registerConverter(new DoubleConverter());
            xstream.registerConverter(new IntConverter());
            xstream.ignoreUnknownElements();
        }
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static final XStreamSerializer getInstance() {
        return XStreamSerializerHolder.INSTANCE;
    }

    /**
     * Serialize string.
     *
     * @param <T>        the type parameter
     * @param obj        the obj
     * @param dateFormat the date format
     * @return the string
     * @see Serializer#serialize(Object, String) Serializer#serialize(java.lang.Object, java.lang.String)
     */
    @Override
    public <T> String serialize(T obj, String dateFormat) {
        xstream.processAnnotations(obj.getClass());
        return xstream.toXML(obj);
    }

    /**
     * Deserialize t.
     *
     * @param <T>        the type parameter
     * @param text       the text
     * @param clazz      the clazz
     * @param dateFormat the date format
     * @return the t
     * @see Serializer#deserialize(String, Class, String) Serializer#deserialize(java.lang.String, java.lang.Class, java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(String text, Class<T> clazz, String dateFormat) {
        xstream.processAnnotations(clazz);
        return (T) xstream.fromXML(text);
    }

    /**
     * The type X stream serializer holder.
     */
    private static final class XStreamSerializerHolder {
        /**
         * The Instance.
         */
        private static final XStreamSerializer INSTANCE = new XStreamSerializer();
    }

}
