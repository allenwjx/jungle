package com.zeh.jungle.client.serializer;

import com.zeh.jungle.utils.serializer.FastJsonUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author allen
 * @version $Id: FastJsonSerializer.java, v 0.1 2016年5月19日 下午1:32:37 allen Exp $
 */
public class FastJsonSerializer implements Serializer {

    private FastJsonSerializer() {
    }

    public static final FastJsonSerializer getInstance() {
        return FastJsonSerializerHolder.INSTANCE;
    }

    /**
     * @see Serializer#serialize(Object, String)
     */
    @Override
    public <T> String serialize(T obj, String dateFormat) {
        if (StringUtils.isNotBlank(dateFormat)) {
            return FastJsonUtils.toJSONString(obj, dateFormat);
        } else {
            return FastJsonUtils.toJSONString(obj);
        }
    }

    /** 
     * @see Serializer#deserialize(String, Class, String)
     */
    @Override
    public <T> T deserialize(String text, Class<T> clazz, String dateFormat) {
        return FastJsonUtils.fromJSONString(text, clazz);
    }

    private static final class FastJsonSerializerHolder {
        private static final FastJsonSerializer INSTANCE = new FastJsonSerializer();
    }

}
