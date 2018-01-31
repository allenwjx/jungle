package com.zeh.jungle.core.context;

/**
 * 容器启动感知接口，在Jungle完成启动后，会调用所有实现了该接口的实现类的startup方法
 * 应用通过实现该接口，可以在容器启动后，执行自定义业务逻辑
 * 
 * @author allen
 * @version $Id: StartupCallback.java, v 0.1 2016年2月28日 上午12:26:41 allen Exp $
 */
public interface StartupCallback extends Order {
    /** 默认顺序 */
    static final int DEFAULT_ORDER = 99;

    /**
     * 执行容器启动后的后处理操作
     * 
     * @param context Jungle上下文
     */
    void startup(JungleContext context);

}
