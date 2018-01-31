package com.zeh.jungle.core.spring.bean;

import java.util.List;

/**
 * 
 * @author allen
 * @version $Id: Channel.java, v 0.1 2016年3月3日 下午5:13:37 allen Exp $
 */
public class Channel {
    /** 消息主题 */
    private String             topic;

    /** Channel事件 */
    private List<ChannelEvent> events;

    /**
     * Getter for topic
     * 
     * @return
     */
    public String getTopic() {
        return topic;
    }

    /**
     * Setter for topic
     * 
     * @param topic
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }

    /**
     * Getter for events
     * 
     * @return
     */
    public List<ChannelEvent> getEvents() {
        return events;
    }

    /**
     * Setter for events
     * 
     * @param events
     */
    public void setEvents(List<ChannelEvent> events) {
        this.events = events;
    }

}
