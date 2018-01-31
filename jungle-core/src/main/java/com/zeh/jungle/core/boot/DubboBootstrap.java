package com.zeh.jungle.core.boot;

import java.util.Arrays;
import java.util.List;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.ConfigUtils;
import com.alibaba.dubbo.container.Container;
import com.google.common.collect.Lists;

/**
 * Dubbo容器入口
 * 
 * @author allen
 * @version $Id: DubboBootstrap.java, v 0.1 2016年2月28日 上午5:44:03 allen Exp $
 */
public class DubboBootstrap implements JungleBootstrap {
    /** logger */
    private static final Logger                     logger        = LoggerFactory.getLogger(DubboBootstrap.class);

    /** dubbo容器key，指定dubbo需要启动的容器 */
    public static final String                      CONTAINER_KEY = "dubbo.container";

    /** dubbo扩展点 */
    private static final ExtensionLoader<Container> loader        = ExtensionLoader.getExtensionLoader(Container.class);

    /** dubbo容器 */
    private List<Container>                         containers;

    /** 
     * @see com.zeh.jungle.core.boot.JungleBootstrap#startup(String[])
     */
    @Override
    public synchronized boolean startup(String[] args) {
        try {
            containers = getDubboContainers(args);
            for (Container container : containers) {
                container.start();
                logger.info("Dubbo " + container.getClass().getSimpleName() + " started!");
            }
            logger.info("Dubbo service server started!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    /** 
     * @see com.zeh.jungle.core.boot.JungleBootstrap#shutdown()
     */
    @Override
    public synchronized boolean shutdown() {
        for (Container container : containers) {
            try {
                container.stop();
                logger.info("Dubbo " + container.getClass().getSimpleName() + " stopped!");
            } catch (Throwable t) {
                logger.error(t.getMessage(), t);
            }
        }
        return true;
    }

    /**
     * 获取dubbo容器
     * 
     * @param args
     * @return
     */
    protected List<Container> getDubboContainers(String[] args) {
        if (args == null || args.length == 0) {
            String config = ConfigUtils.getProperty(CONTAINER_KEY, loader.getDefaultExtensionName());
            args = Constants.COMMA_SPLIT_PATTERN.split(config);
        }
        final List<Container> containers = Lists.newArrayList();
        for (int i = 0; i < args.length; i++) {
            containers.add(loader.getExtension(args[i]));
        }
        logger.info("Use container type(" + Arrays.toString(args) + ") to run dubbo serivce.");
        return containers;
    }

    /**
     * @return
     */
    public List<Container> getContainers() {
        return containers;
    }

}
