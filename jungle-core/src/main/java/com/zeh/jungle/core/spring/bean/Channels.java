package com.zeh.jungle.core.spring.bean;

import java.util.List;

/**
 * 
 * @author allen
 * @version $Id: Channels.java, v 0.1 2016年3月3日 下午6:34:39 allen Exp $
 */
public class Channels {

    /** 消费者订阅的Channel，Channel由主题、事件id组成 */
    private List<Channel> channels;

    /**
     * Getter for channels
     * 
     * @return
     */
    public List<Channel> getChannels() {
        return channels;
    }

    /**
     * Setter for channels
     * 
     * @param channels
     */
    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

}
