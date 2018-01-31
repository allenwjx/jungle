package com.zeh.jungle.core.degrade;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * 
 * @author allen
 * @version $Id: DefaultDegradeAspect.java, v 0.1 2017年12月7日 下午5:20:14 allen Exp $
 */
@Aspect
public class DefaultDegradeAspect extends DegradeAspect {

    /**
     * This advice is used to add the monitor around method executions that have been tagged
     * with the Degrade annotation.
     * 
     * @param pjp
     * @param degrade
     * @return
     * @throws Throwable
     */
    @Around(value = "execution(* *(..)) && @annotation(degrade)", argNames = "pjp,degrade")
    public Object doDegrade(final ProceedingJoinPoint pjp, Degrade degrade) throws Throwable {

        //We just delegate to the super class, wrapping the AspectJ-specific ProceedingJoinPoint as an DegradeJoinPoint
        return runDegradeMethod(new DegradeJoinPoint() {
            @Override
            public Object proceed() throws Throwable {
                return pjp.proceed();
            }

            @Override
            public Object getExecutingObject() {
                return pjp.getThis();
            }

            @Override
            public Object[] getParameters() {
                return pjp.getArgs();
            }

            @Override
            public String getMethodName() {
                return pjp.getSignature().getName();
            }

            @Override
            public Signature getMethodSignature() {
                return pjp.getSignature();
            }
        }, degrade);
    }

}
