package com.zeh.jungle.core.i18n;

import java.io.Serializable;

/**
 * i18n资源文件消息数据模型
 * 
 * @author allen
 * @version $Id: Message.java, v 0.1 2016年2月26日 下午2:30:52 allen Exp $
 */
public class Message implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    /**消息数据文本*/
    private String            message;

    /**消息代码*/
    private String            messageKey;

    /**消息模版占位符参数*/
    private Object[]          args;

    /**下一条消息*/
    private Message           nextMessage;

    protected Message(String message, String messageKey, Object... args) {
        super();
        this.message = message;
        this.messageKey = messageKey;
        this.args = args;
    }

    /**
     * 获取消息Key
     * 
     * @return the messageKey
     */
    public String getMessageKey() {
        return messageKey;
    }

    /**
     * 获取消息描述文本
     * 
     * @return the message
     */
    public String getMessage() {
        return message + (nextMessage != null ? ". " + nextMessage.getMessage() : "");
    }

    /**
     * 获取消息占位符参数
     * 
     * @return the args
     */
    public Object[] getArgs() {
        return args;
    }

    /**
     * 获取关联的下一条消息
     * 
     * @return the nextMessage
     */
    public Message getNextMessage() {
        return nextMessage;
    }

    /**
     * 设置关联的下一条消息
     * 
     * @param nextMessage the nextMessage to set
     */
    public Message setNextMessage(Message nextMessage) {
        this.nextMessage = nextMessage;
        return this;
    }

    @Override
    public String toString() {
        return this.getMessage();
    }
}
