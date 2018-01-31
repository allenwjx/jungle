package com.zeh.jungle.utils.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.zeh.jungle.utils.common.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 序列化工具
 * 
 * @author allen
 * @version $Id: SerializationUtils.java, v 0.1 2016年2月26日 下午2:19:59 allen Exp $
 */
public class SerializationUtils {
    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(SerializationUtils.class);

    /**
     * 构造函数
     */
    private SerializationUtils() {
    }

    /**
     * java对象序列化
     * 
     * @param state
     * @return
     */
    public static byte[] serialize(Serializable state) {
        ObjectOutputStream oos = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream(512);
            oos = new ObjectOutputStream(bos);
            oos.writeObject(state);
            oos.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    LoggerUtils.warn(logger, "对象序列化失败, class: " + state.getClass(), e);
                }
            }
        }
    }

    /**
     * java对象反序列化
     * 
     * @param byteArray
     * @return
     */
    public static <T extends Serializable> T deserialize(byte[] byteArray) {
        ObjectInputStream oip = null;
        try {
            oip = new ObjectInputStream(new ByteArrayInputStream(byteArray));
            @SuppressWarnings("unchecked")
            T result = (T) oip.readObject();
            return result;
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        } finally {
            if (oip != null) {
                try {
                    oip.close();
                } catch (IOException e) {
                    LoggerUtils.warn(logger, "对象反序列化失败", e);
                }
            }
        }
    }
}
