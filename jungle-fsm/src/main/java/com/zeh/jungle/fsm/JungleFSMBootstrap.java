package com.zeh.jungle.fsm;

import com.zeh.jungle.core.context.JungleContext;
import com.zeh.jungle.core.context.StartupCallback;

/**
 * @author allen
 * @create $ ID: JungleFSMBootstrap, 18/1/15 16:26 allen Exp $
 * @since 1.0.0
 */
public class JungleFSMBootstrap extends SpringFSMBootstrap implements StartupCallback {
    /**
     * 执行容器启动后的后处理操作
     *
     * @param context Jungle上下文
     */
    @Override
    public void startup(JungleContext context) {
        LOGGER.info("Initializing FSM container ...");
        startup();
        LOGGER.info("The FSM container is launched");
    }

    /**
     * 获取顺序
     *
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
