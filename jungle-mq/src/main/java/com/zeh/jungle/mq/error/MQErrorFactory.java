package com.zeh.jungle.mq.error;

import com.zeh.jungle.core.error.AbstractErrorFactory;
import com.zeh.jungle.core.error.JGError;

/**
 * Core模块错误工厂
 * 
 * @author allen
 * @version $Id: MQErrorFactory.java, v 0.1 2016年2月26日 下午4:14:42 allen Exp $
 */
public class MQErrorFactory extends AbstractErrorFactory {

    /** 
     * @see AbstractErrorFactory#provideErrorBundleName()
     */
    @Override
    protected String provideErrorBundleName() {
        return "jg-mq";
    }

    /**
     * 获取JpaDalErrorFactory单例
     * 
     * @return
     */
    public static MQErrorFactory getInstance() {
        return MQErrorFactoryHolder.FACTORY;
    }

    /**
     * CoreErrorFactoryHolder instance keeper
     * 
     * @author allen
     * @version $Id: MQErrorFactoryHolder.java, v 0.1 2016年2月26日 下午4:20:31 allen Exp $
     */
    private static final class MQErrorFactoryHolder {
        /** instance */
        private static final MQErrorFactory FACTORY = new MQErrorFactory();
    }

    /**
     * 消息服务器启动失败
     * 
     * @param group 消息分组
     * @param nameSrvAddr 消息命名服务器
     * @return
     */
    public JGError producerStartError(String group, String nameSrvAddr) {
        return createError("JG0500102000", group, nameSrvAddr);
    }

    /**
     * 消息投递失败
     * 
     * @param messageId 消息ID
     * @return
     */
    public JGError producerError(String messageId) {
        return createError("JG0500102001", messageId);
    }

    /**
     * 消息序列化失败
     * 
     * @param messageId 消息ID
     * @param serialization 序列化器
     * @return
     */
    public JGError serializeError(String messageId, String serialization) {
        return createError("JG0500102002", messageId, serialization);
    }

    /**
     * 消息反序列化失败
     * 
     * @param messageId 消息ID
     * @param serialization 序列化器
     * @return
     */
    public JGError unserializeError(String messageId, String serialization) {
        return createError("JG0500102003", messageId, serialization);
    }

    /**
     * 消息订阅失败
     * 
     * @param topci
     * @param eventId
     * @return
     */
    public JGError subscribeError(String topci, String eventId) {
        return createError("JG0500102004", topci, eventId);
    }

    /**
     * 消息订阅者启动失败
     * 
     * @param group 消息分组
     * @param nameSrvAddr 消息命名服务器
     * @return
     */
    public JGError subscribeStartError(String group, String nameSrvAddr) {
        return createError("JG0500102005", group, nameSrvAddr);
    }
}
