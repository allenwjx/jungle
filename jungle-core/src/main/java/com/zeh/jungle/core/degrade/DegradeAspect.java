package com.zeh.jungle.core.degrade;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;

import com.google.common.collect.Maps;

/**
 * 
 * @author allen
 * @version $Id: DegradeAspect.java, v 0.1 2017年12月7日 下午4:55:05 allen Exp $
 */
public class DegradeAspect implements Ordered {
    /** logger */
    private static final Logger         logger   = LoggerFactory.getLogger(DegradeAspect.class);

    /** Degrade deciders */
    private Map<String, DegradeDecider> deciders = Maps.newHashMap();

    /**
     * Delegate the business logic wrapped by degrade service
     * 
     * @param pjp
     * @param degrade
     * @return
     * @throws Throwable
     */
    public Object runDegradeMethod(final DegradeJoinPoint pjp, Degrade degrade) throws Throwable {
        boolean degradeRet = false;
        DegradeDecider decider = null;

        try {
            decider = deciders.get(degrade.name());
            degradeRet = decider.decide(pjp.getExecutingObject(), pjp.getParameters(), pjp.getMethodName(), degrade);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        if (decider == null) {
            logger.warn("Cannot found the DegradeDecider for name " + degrade.name());
            return pjp.proceed();
        }

        if (degradeRet) {
            logger.info("The service has been degraded, name: " + degrade.name() + ", class: " + pjp.getExecutingObject().getClass().getName() + ",method: " + pjp.getMethodName());
            return decider.degrade(pjp.getExecutingObject(), pjp.getParameters());
        }

        // execute as normally
        return pjp.proceed();
    }

    @Override
    public int getOrder() {
        return 1;
    }

    public void setDeciders(Map<String, DegradeDecider> deciders) {
        this.deciders = deciders;
    }

}
