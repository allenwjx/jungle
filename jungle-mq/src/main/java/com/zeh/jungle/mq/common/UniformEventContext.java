package com.zeh.jungle.mq.common;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;

/**
 * 统一消息事件上下文
 * 
 * @author allen
 * @version $Id: UniformEventContext.java, v 0.1 2016年3月2日 下午4:37:25 allen Exp $
 */
public class UniformEventContext {
    /***/
    private ConsumeConcurrentlyContext ccContext;
    /***/
    private ConsumeOrderlyContext      coContext;

    /**
     * @param ccContext
     * @param coContext
     */
    public UniformEventContext(ConsumeConcurrentlyContext ccContext, ConsumeOrderlyContext coContext) {
        super();
        this.ccContext = ccContext;
        this.coContext = coContext;
    }

    /**
     * Getter for ConsumeConcurrentlyContext
     * 
     * @return
     */
    public ConsumeConcurrentlyContext getCcContext() {
        return ccContext;
    }

    /**
     * Setter for ConsumeConcurrentlyContext
     * 
     * @param ccContext
     */
    public void setCcContext(ConsumeConcurrentlyContext ccContext) {
        this.ccContext = ccContext;
    }

    /**
     * Getter for ConsumeOrderlyContext
     * 
     * @return
     */
    public ConsumeOrderlyContext getCoContext() {
        return coContext;
    }

    /**
     * Setter for ConsumeOrderlyContext
     * 
     * @param coContext
     */
    public void setCoContext(ConsumeOrderlyContext coContext) {
        this.coContext = coContext;
    }

}
