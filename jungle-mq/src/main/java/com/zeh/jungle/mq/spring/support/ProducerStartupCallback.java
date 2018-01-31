package com.zeh.jungle.mq.spring.support;

import com.google.common.collect.Lists;
import com.zeh.jungle.core.context.JungleContext;
import com.zeh.jungle.core.context.ShutdownCallback;
import com.zeh.jungle.core.context.StartupCallback;
import com.zeh.jungle.mq.exception.MQException;
import com.zeh.jungle.mq.producer.UniformEventPublisher;
import com.zeh.jungle.utils.common.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author allen
 * @create $ ID: ProducerStartupCallback, 18/1/31 17:41 allen Exp $
 * @since 1.0.0
 */
@Service
public class ProducerStartupCallback implements StartupCallback, ShutdownCallback {
    /** logger */
    private static final Logger                      LOGGER     = LoggerFactory.getLogger(ProducerStartupCallback.class);
    /** 生产者 */
    private static final List<UniformEventPublisher> publishers = Lists.newArrayList();

    /**
     * 执行容器启动后的后处理操作
     *
     * @param context Jungle上下文
     */
    @Override
    public void startup(JungleContext context) {
        LoggerUtils.info(LOGGER, "[Jungle] Executing producer startup for RocketMQ ...");
        Map<String, UniformEventPublisher> beans = context.getSpringContext().getBeansOfType(UniformEventPublisher.class, false, true);
        if (beans != null) {
            try {
                for (UniformEventPublisher bean : beans.values()) {
                    bean.start();
                    publishers.add(bean);
                }
            } catch (MQException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }

    }

    /**
     * 获取顺序
     *
     * @return
     */
    @Override
    public int getOrder() {
        return 2;
    }

    /**
     * 在容器销毁前执行前处理操作
     *
     * @param context 上下文
     */
    @Override
    public void shutdown(JungleContext context) {
        LoggerUtils.info(LOGGER, "[Jungle] Executing producer shutdown for RocketMQ ...");
        for (UniformEventPublisher publisher : publishers) {
            try {
                publisher.shutdown();
            } catch (MQException e) {
                LoggerUtils.error(LOGGER, e.getMessage(), e);
            }
        }
    }
}
