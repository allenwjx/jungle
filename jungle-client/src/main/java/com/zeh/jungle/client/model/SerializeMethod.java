package com.zeh.jungle.client.model;

import com.zeh.jungle.client.serializer.*;

/**
 * 
 * @author allen
 * @version $Id: SerializeMethod.java, v 0.1 2016年5月19日 下午4:43:01 allen Exp $
 */
public enum SerializeMethod {
                             /** none */
                             NONE_SERIALIZER(NoneSerializer.getInstance()),
                             /** fastjson */
                             FASTJSON_SERIALIZER(FastJsonSerializer.getInstance()),
                             /** xstream */
                             XSTREAM_SERIALIZER(XStreamSerializer.getInstance()),
                             /** jaxb xml */
                             JAXB_SERIALIZER(JaxbSerializer.getInstance());

    private Serializer serializer;

    private SerializeMethod(Serializer serializer) {
        this.serializer = serializer;
    }

    /**
     * Getter method for property <tt>serializer</tt>.
     * 
     * @return property value of serializer
     */
    public Serializer getSerializer() {
        return serializer;
    }
}
