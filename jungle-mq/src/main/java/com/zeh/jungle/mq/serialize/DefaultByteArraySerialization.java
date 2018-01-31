package com.zeh.jungle.mq.serialize;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.serialize.ObjectInput;
import com.alibaba.dubbo.common.serialize.ObjectOutput;
import com.alibaba.dubbo.common.serialize.Serialization;

/**
 * 
 * @author allen
 * @version $Id: DefaultByteArraySerialization.java, v 0.1 2016年3月2日 下午5:09:55 allen Exp $
 */
public class DefaultByteArraySerialization implements Serialization {

    /** 
     * @see com.alibaba.dubbo.common.serialize.Serialization#getContentTypeId()
     */
    @Override
    public byte getContentTypeId() {
        return 127;
    }

    /** 
     * @see com.alibaba.dubbo.common.serialize.Serialization#getContentType()
     */
    @Override
    public String getContentType() {
        return "x-application/byteArray-serialization";
    }

    /** 
     * @see com.alibaba.dubbo.common.serialize.Serialization#serialize(com.alibaba.dubbo.common.URL, OutputStream)
     */
    @Override
    public ObjectOutput serialize(URL url, OutputStream output) throws IOException {
        return new DefaultByteArraySerializationObjectOutput(output);
    }

    /** 
     * @see com.alibaba.dubbo.common.serialize.Serialization#deserialize(com.alibaba.dubbo.common.URL, InputStream)
     */
    @Override
    public ObjectInput deserialize(URL url, InputStream input) throws IOException {
        return new DefaultByteArraySerializationObjectInput(input);
    }

}
