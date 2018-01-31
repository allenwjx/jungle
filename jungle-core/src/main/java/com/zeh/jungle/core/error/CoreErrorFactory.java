package com.zeh.jungle.core.error;

/**
 * Core模块错误工厂
 * 
 * @author allen
 * @version $Id: CoreErrorFactory.java, v 0.1 2016年2月26日 下午4:14:42 allen Exp $
 */
public class CoreErrorFactory extends AbstractErrorFactory {

    /** 
     * @see com.zeh.jungle.core.error.AbstractErrorFactory#provideErrorBundleName()
     */
    @Override
    protected String provideErrorBundleName() {
        return "jg-core";
    }

    /**
     * 获取JpaDalErrorFactory单例
     * 
     * @return
     */
    public static CoreErrorFactory getInstance() {
        return CoreErrorFactoryHolder.FACTORY;
    }

    /**
     * CoreErrorFactoryHolder instance keeper
     * 
     * @author allen
     * @version $Id: CoreErrorFactoryHolder.java, v 0.1 2016年2月26日 下午4:20:31 allen Exp $
     */
    private static final class CoreErrorFactoryHolder {
        /** instance */
        private static final CoreErrorFactory FACTORY = new CoreErrorFactory();
    }

    /**
     * 加载系统启动插件配置文件失败
     * 
     * @param file
     * @return
     */
    public JGError extensionFileError(String file) {
        return createError("JG0500101001", file);
    }

    /**
     * 加载系统启动插件失败
     * 
     * @param extensionName
     * @return
     */
    public JGError loadExtensionFail(String extensionName) {
        return createError("JG0500101002", extensionName);
    }

    /**
     * LY0500101003=RPC服务调用失败，服务连接超时；interface：{0}；method：{1}
     *
     * @param interfaceName
     * @param methodName
     * @return
     */
    public JGError rpcConnectionError(String interfaceName, String methodName) {
        return createError("JG0500101003", interfaceName, methodName);
    }

    /**
     * LY0500101004=RPC服务调用失败，IO读写超时；interface：{0}；method：{1}
     *
     * @param interfaceName
     * @param methodName
     * @return
     */
    public JGError rpcIOError(String interfaceName, String methodName) {
        return createError("JG0500101004", interfaceName, methodName);
    }

    /**
     * LY0500101005=RPC服务调用失败：{0}；interface：{1}；method：{2}
     *
     * @param error
     * @param interfaceName
     * @param methodName
     * @return
     */
    public JGError rpcError(String error, String interfaceName, String methodName) {
        return createError("JG0500101005", error, interfaceName, methodName);
    }

}
