package com.zeh.jungle.client.serializer;

/**
 * 不作序列化
 * 
 * @author allen
 * @version $Id: NoneSerializer.java, v 0.1 2016年5月24日 下午2:40:20 czj12867 Exp $
 */
public class NoneSerializer implements Serializer {

    private NoneSerializer() {
    }

    public static final NoneSerializer getInstance() {
        return NoneSerializerHolder.INSTANCE;
    }

    /**
     * @see Serializer#serialize(Object, String)
     */
    @Override
    public <T> String serialize(T obj, String dateFormat) {
        return obj.toString();
    }

    /** 
     * @see Serializer#deserialize(String, Class, String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(String text, Class<T> clazz, String dateFormat) {
        return (T) text;
    }

    private static final class NoneSerializerHolder {
        private static final NoneSerializer INSTANCE = new NoneSerializer();
    }
}
