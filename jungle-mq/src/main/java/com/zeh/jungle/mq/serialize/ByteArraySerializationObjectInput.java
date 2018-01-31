package com.zeh.jungle.mq.serialize;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Type;

import com.alibaba.dubbo.common.serialize.ObjectInput;
import com.alibaba.dubbo.common.utils.Assert;

/**
 * 
 * @author allen
 * @version $Id: ByteArraySerializationObjectInput.java, v 0.1 2016年3月2日 下午5:12:23 allen Exp $
 */
public class ByteArraySerializationObjectInput implements ObjectInput {

    /***/
    private final ObjectInputStream inputStream;

    /**
     * @param is
     * @throws IOException
     */
    public ByteArraySerializationObjectInput(InputStream is) throws IOException {
        this(new ObjectInputStream(is));
    }

    /**
     * @param is
     */
    protected ByteArraySerializationObjectInput(ObjectInputStream is) {
        Assert.notNull(is, "input == null");
        inputStream = is;
    }

    /**
     * 
     * @return
     */
    protected ObjectInputStream getObjectInputStream() {
        return inputStream;
    }

    /** 
     * @see com.alibaba.dubbo.common.serialize.DataInput#readBool()
     */
    @Override
    public boolean readBool() throws IOException {
        throw new UnsupportedOperationException("Read Bool not supported");
    }

    /** 
     * @see com.alibaba.dubbo.common.serialize.DataInput#readByte()
     */
    @Override
    public byte readByte() throws IOException {
        throw new UnsupportedOperationException("Read byte not supported");
    }

    /** 
     * @see com.alibaba.dubbo.common.serialize.DataInput#readShort()
     */
    @Override
    public short readShort() throws IOException {
        throw new UnsupportedOperationException("Read short not supported");
    }

    /** 
     * @see com.alibaba.dubbo.common.serialize.DataInput#readInt()
     */
    @Override
    public int readInt() throws IOException {
        throw new UnsupportedOperationException("Read int not supported");
    }

    /** 
     * @see com.alibaba.dubbo.common.serialize.DataInput#readLong()
     */
    @Override
    public long readLong() throws IOException {
        throw new UnsupportedOperationException("Read long not supported");
    }

    /** 
     * @see com.alibaba.dubbo.common.serialize.DataInput#readFloat()
     */
    @Override
    public float readFloat() throws IOException {
        throw new UnsupportedOperationException("Read float not supported");
    }

    /** 
     * @see com.alibaba.dubbo.common.serialize.DataInput#readDouble()
     */
    @Override
    public double readDouble() throws IOException {
        throw new UnsupportedOperationException("Read double not supported");
    }

    /** 
     * @see com.alibaba.dubbo.common.serialize.DataInput#readUTF()
     */
    @Override
    public String readUTF() throws IOException {
        return inputStream.readUTF();
    }

    /** 
     * @see com.alibaba.dubbo.common.serialize.DataInput#readBytes()
     */
    @Override
    public byte[] readBytes() throws IOException {
        return readUTF().getBytes();
    }

    /** 
     * @see com.alibaba.dubbo.common.serialize.ObjectInput#readObject()
     */
    @Override
    public Object readObject() throws IOException, ClassNotFoundException {
        throw new UnsupportedOperationException("Read object not supported");
    }

    /** 
     * @see com.alibaba.dubbo.common.serialize.ObjectInput#readObject(Class)
     */
    @Override
    public <T> T readObject(Class<T> cls) throws IOException, ClassNotFoundException {
        throw new UnsupportedOperationException("Read object not supported");
    }

    /** 
     * @see com.alibaba.dubbo.common.serialize.ObjectInput#readObject(Class, Type)
     */
    @Override
    public <T> T readObject(Class<T> cls, Type type) throws IOException, ClassNotFoundException {
        throw new UnsupportedOperationException("Read object not supported");
    }

}
