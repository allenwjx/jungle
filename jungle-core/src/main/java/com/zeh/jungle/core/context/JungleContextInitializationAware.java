package com.zeh.jungle.core.context;

/**
 * 容器初始化感知接口，应用实现该接口，可以在容器初始化时执行自定义业务逻辑
 * @author allen
 * @version $Id: JungleContextInitializationAware.java, v 0.1 2016年2月28日 上午1:09:00 allen Exp $
 */
public interface JungleContextInitializationAware {

    /**
     * 容器初始化
     */
    void init();
}
