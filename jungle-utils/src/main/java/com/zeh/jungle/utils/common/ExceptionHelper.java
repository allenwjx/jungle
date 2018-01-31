package com.zeh.jungle.utils.common;

import java.lang.reflect.InvocationTargetException;

/**
 * 异常处理工具类
 * 
 * @author allen
 * @version $Id: ExceptionHelper.java, v 0.1 2016年2月26日 下午3:25:04 allen Exp $
 */
public class ExceptionHelper {
    /**
     * 构造函数
     */
    private ExceptionHelper() {
    }

    /**
     * 处理Java动态代理产生的异常，如果异常类型是动态代理反射异常，则获取真实异常类型
     * @param t Java异常
     * @return 实际Java异常
     */
    public static <T extends Throwable> T unwrap(T t) {
        if (t instanceof InvocationTargetException) {
            return ((T) ((InvocationTargetException) t).getTargetException());
        }
        return t;
    }
}
