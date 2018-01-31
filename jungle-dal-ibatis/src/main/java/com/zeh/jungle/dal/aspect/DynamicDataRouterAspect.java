package com.zeh.jungle.dal.aspect;

import java.lang.reflect.Method;

import com.zeh.jungle.dal.DataRoutingContextHolder;
import com.zeh.jungle.dal.annotation.DBResource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;

/**
 * 动态路由切面
 * 
 * @author allen
 * @version $Id: DynamicDataRouterAspect.java, v 0.1 2016年4月29日 下午3:10:45 wb30644 Exp $
 */
public class DynamicDataRouterAspect implements Ordered {

    /**
     * 
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around(value = "execution(* *(..)) && @annotation(dbResource)", argNames = "pjp,dbResource")
    public Object doAround(final ProceedingJoinPoint pjp, DBResource dbResource) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        if (!StringUtils.isEmpty(dbResource.value())) {
            DataRoutingContextHolder.setContext(dbResource.value());
        }
        return pjp.proceed();
    }

    /** 
     * 优先级最高
     * @see Ordered#getOrder()
     */
    @Override
    public int getOrder() {
        return 0;
    }

}
