package com.zeh.jungle.core.degrade;

/**
 * 
 * @author allen
 * @version $Id: DegradeDecider.java, v 0.1 2017年12月7日 下午4:50:19 allen Exp $
 */
public interface DegradeDecider {

    /**
     * 是否满足降级熔断条件
     * 
     * @param obj 代理的对象
     * @param parameter 方法参数
     * @param methodName 方法名称
     * @param degrade 降级注解
     * @return 是否满足降级条件
     */
    boolean decide(Object obj, Object[] parameter, String methodName, Degrade degrade);

    /**
     * 降级熔断方法
     * 
     * @param obj 代理的对象
     * @param parameter 方法参数
     * @return 降级方法返回的结果
     * @throws Exception 降级方法抛出的异常
     */
    Object degrade(Object obj, Object[] parameter) throws Exception;
}
