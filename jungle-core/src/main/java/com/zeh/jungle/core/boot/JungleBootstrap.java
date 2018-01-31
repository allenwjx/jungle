package com.zeh.jungle.core.boot;

/**
 * 容器启动入口
 * 
 * @author allen
 * @version $Id: JungleBootstrap.java, v 0.1 2016年2月28日 上午4:54:09 allen Exp $
 */
public interface JungleBootstrap {

    /**
     * 启动应用
     * 
     * @param args
     * @return
     */
    boolean startup(String[] args);

    /**
     * 终止应用
     * 
     * @return
     */
    boolean shutdown();
}
