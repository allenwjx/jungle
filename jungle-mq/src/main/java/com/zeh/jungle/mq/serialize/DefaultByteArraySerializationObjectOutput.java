package com.zeh.jungle.mq.serialize;

import java.io.IOException;
import java.io.OutputStream;

import com.alibaba.dubbo.common.serialize.ObjectOutput;
import com.alibaba.dubbo.common.utils.Assert;

/**
 * 
 * @author allen
 * @version $Id: ByteArraySerializationObjectOutput.java, v 0.1 2016年3月2日 下午5:11:37 allen Exp $
 */
public class DefaultByteArraySerializationObjectOutput implements ObjectOutput {
    /***/
    private final OutputStream outputStream;

    /**
     * @param os
     * @throws IOException
     */
    public DefaultByteArraySerializationObjectOutput(OutputStream os) throws IOException {
        Assert.notNull(os, "output == null");
        this.outputStream = os;
    }

    /**
     * @return
     */
    protected OutputStream getObjectOutputStream() {
        return outputStream;
    }

    /** 
     * @see com.alibaba.dubbo.common.serialize.DataOutput#writeBool(boolean)
     */
    @Override
    public void writeBool(boolean v) throws IOException {
        throw new UnsupportedOperationException("Write bool not supported");
    }

    /** 
     * @see com.alibaba.dubbo.common.serialize.DataOutput#writeByte(byte)
     */
    @Override
    public void writeByte(byte v) throws IOException {
        throw new UnsupportedOperationException("Write byte not supported");
    }

    /** 
     * @see com.alibaba.dubbo.common.serialize.DataOutput#writeShort(short)
     */
    @Override
    public void writeShort(short v) throws IOException {
        throw new UnsupportedOperationException("Write short not supported");
    }

    /** 
     * @see com.alibaba.dubbo.common.serialize.DataOutput#writeInt(int)
     */
    @Override
    public void writeInt(int v) throws IOException {
        throw new UnsupportedOperationException("Write int not supported");
    }

    /** 
     * @see com.alibaba.dubbo.common.serialize.DataOutput#writeLong(long)
     */
    @Override
    public void writeLong(long v) throws IOException {
        throw new UnsupportedOperationException("Write long not supported");
    }

    /** 
     * @see com.alibaba.dubbo.common.serialize.DataOutput#writeFloat(float)
     */
    @Override
    public void writeFloat(float v) throws IOException {
        throw new UnsupportedOperationException("Write float not supported");
    }

    /** 
     * @see com.alibaba.dubbo.common.serialize.DataOutput#writeDouble(double)
     */
    @Override
    public void writeDouble(double v) throws IOException {
        throw new UnsupportedOperationException("Write double not supported");
    }

    /** 
     * @see com.alibaba.dubbo.common.serialize.DataOutput#writeUTF(String)
     */
    @Override
    public void writeUTF(String v) throws IOException {
        throw new UnsupportedOperationException("writeUTF not supported");
    }

    /** 
     * @see com.alibaba.dubbo.common.serialize.DataOutput#writeBytes(byte[])
     */
    @Override
    public void writeBytes(byte[] v) throws IOException {
        outputStream.write(v);
    }

    /** 
     * @see com.alibaba.dubbo.common.serialize.DataOutput#writeBytes(byte[], int, int)
     */
    @Override
    public void writeBytes(byte[] v, int off, int len) throws IOException {
        outputStream.write(v, off, len);
    }

    /** 
     * @see com.alibaba.dubbo.common.serialize.DataOutput#flushBuffer()
     */
    @Override
    public void flushBuffer() throws IOException {
        outputStream.flush();
    }

    /** 
     * @see com.alibaba.dubbo.common.serialize.ObjectOutput#writeObject(Object)
     */
    @Override
    public void writeObject(Object obj) throws IOException {
        throw new UnsupportedOperationException("Write object not supported");
    }

}
