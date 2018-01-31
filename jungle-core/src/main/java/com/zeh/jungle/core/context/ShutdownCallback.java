package com.zeh.jungle.core.context;

/**
 * 容器关闭感知接口，在容器销毁之前，会调用所有实现了该接口的实现类的shutdown方法
 * 应用通过实现该接口，可以在容器销毁前，执行自定义业务逻辑
 * 
 * @author allen
 * @version $Id: ShutdownCallback.java, v 0.1 2016年2月28日 上午12:30:39 allen Exp $
 */
public interface ShutdownCallback {

    /**
     * 在容器销毁前执行前处理操作
     * 
     * @param context 上下文
     */
    void shutdown(JungleContext context);
}
