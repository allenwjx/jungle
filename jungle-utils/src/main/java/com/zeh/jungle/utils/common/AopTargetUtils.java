package com.zeh.jungle.utils.common;

import java.lang.reflect.Field;

import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.support.AopUtils;

/**
 * @author allen
 * @version $Id: AopTargetUtils.java, v 0.1 2017年1月17日 下午8:24:02 allen Exp $
 */
public class AopTargetUtils {
    /**
     * 获取目标对象
     *
     * @param proxy 代理
     * @return 代理对象
     */
    public static Object getTarget(Object proxy) {

        if (!AopUtils.isAopProxy(proxy)) {
            return proxy;
        }

        if (AopUtils.isJdkDynamicProxy(proxy)) {
            return getJdkDynamicProxyTargetObject(proxy);
        } else { //cglib  
            return getCglibProxyTargetObject(proxy);
        }

    }

    /**
     * 获取cglib代理对象
     *
     * @param proxy 代理
     * @return 代理对象
     */
    private static Object getCglibProxyTargetObject(Object proxy) {
        return getProxyTargetObject(proxy, "CGLIB$CALLBACK_0", false);
    }

    /**
     * 获取JDK动态代理对象
     *
     * @param proxy 代理
     * @return 代理对象
     */
    private static Object getJdkDynamicProxyTargetObject(Object proxy) {
        return getProxyTargetObject(proxy, "h", true);
    }

    /**
     * 获取代理对象
     *
     * @param proxy 代理
     * @param fieldName declared field name
     * @return 代理对象
     */
    private static Object getProxyTargetObject(Object proxy, String fieldName, boolean superClass) {
        try {
            Field h;
            if (superClass) {
                h = proxy.getClass().getSuperclass().getDeclaredField(fieldName);
            } else {
                h = proxy.getClass().getDeclaredField(fieldName);
            }
            h.setAccessible(true);
            Object o = h.get(proxy);

            Field advised = o.getClass().getDeclaredField("advised");
            advised.setAccessible(true);

            return ((AdvisedSupport) advised.get(o)).getTargetSource().getTarget();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
