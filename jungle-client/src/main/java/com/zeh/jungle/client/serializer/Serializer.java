package com.zeh.jungle.client.serializer;

/**
 * 
 * @author allen
 * @version $Id: Serializer.java, v 0.1 2016年5月19日 下午1:29:49 allen Exp $
 */
public interface Serializer {

    /**
     * 对象序列化
     * 
     * @param obj
     * @param dateFormat
     * @return
     */
    <T> String serialize(T obj, String dateFormat);

    /**
     * 对象反序列化
     * 
     * @param text
     * @param clazz
     * @param dateFormat
     * @return
     */
    <T> T deserialize(String text, Class<T> clazz, String dateFormat);
}
