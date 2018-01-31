package com.zeh.jungle.mq.spring.bean;

/**
 * 
 * @author allen
 * @version $Id: ChannelEvent.java, v 0.1 2016年3月3日 下午5:16:38 allen Exp $
 */
public class ChannelEvent {

    /** 事件码 */
    private String eventCode;

    /** 事件类型，保留字段 */
    private String eventType;

    /**
     * Getter for event code;
     * 
     * @return
     */
    public String getEventCode() {
        return eventCode;
    }

    /**
     * Setter for event code
     * 
     * @param eventCode
     */
    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    /**
     * Getter for event type
     * 
     * @return
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * Setter for event type
     * 
     * @param eventType
     */
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

}
