package com.zeh.jungle.core.degrade;

import org.aspectj.lang.Signature;

/**
 * AOP-framework降级熔断指标连接点
 * 
 * An AOP implementation indicator interface which offers all information required to do a measure, proceed original
 * method and log result in customizable way. Specific Join Point implementations in AOP libraries/frameworks
 * should implement it wrapping their own internal structures.
 * 
 * @author allen
 * @version $Id: DegradeJoinPoint.java, v 0.1 2017年12月7日 下午4:59:38 allen Exp $
 */
public interface DegradeJoinPoint {

    /**
     * Calls indicator method and returns its result.
     *
     * @return result of proceeding
     * @throws Throwable thrown exception
     */
    Object proceed() throws Throwable;

    /**
     * Returns an object whose method was annotated (indicator).
     *
     * @return an object whose method was annotated
     */
    Object getExecutingObject();

    /**
     * Returns a parameters (arguments) array of processing method.
     *
     * @return array of parameters
     */
    Object[] getParameters();

    /**
     * Returns a processing method name.
     *
     * @return processing method name
     */
    String getMethodName();

    /**
     * Returns MethodSignature
     * 
     * @return MethodSignature
     */
    Signature getMethodSignature();
}
