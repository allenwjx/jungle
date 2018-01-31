package com.zeh.jungle.core.extension;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;

import com.zeh.jungle.core.boot.DefaultBootstrap;
import com.zeh.jungle.core.boot.JungleBootstrap;
import com.zeh.jungle.core.error.CoreErrorFactory;
import com.zeh.jungle.core.exception.ExtensionException;
import com.zeh.jungle.utils.common.LoggerUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

/**
 * 插件加载器默认实现
 * 
 * @author allen
 * @version $Id: DefaultExtensionLoader.java, v 0.1 2016年2月29日 下午4:17:27 allen Exp $
 */
public class DefaultExtensionLoader implements ExtensionLoader {

    /** logger */
    protected Logger           logger       = LoggerFactory.getLogger(getClass());

    /** 错误工厂 */
    protected CoreErrorFactory errorFactory = CoreErrorFactory.getInstance();

    /** 
     * @see com.zeh.jungle.core.extension.ExtensionLoader#loadExtension()
     */
    @Override
    public List<JungleBootstrap> loadExtension() throws ExtensionException {
        List<JungleBootstrap> boots = Lists.newArrayList();

        Enumeration<URL> urls = getExtensions();
        if (urls == null) {
            return boots;
        }

        while (urls.hasMoreElements()) {
            InputStream input = null;
            try {
                input = urls.nextElement().openStream();
                List<String> extensions = IOUtils.readLines(input, "utf-8");
                // loading extension
                for (String extension : extensions) {
                    if (StringUtils.isBlank(extension)) {
                        continue;
                    }
                    JungleBootstrap bootstrap = doLoadExtension(extension);
                    boots.add(bootstrap);
                    LoggerUtils.info(logger, "Loading extension {}", bootstrap.getClass().getName());
                }
            } catch (IOException e) {
                throw new ExtensionException(errorFactory.loadExtensionFail(JUNGLE_EXTENSION_FILE), e);
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        // ignore
                    }
                }
            }
        }
        return boots;
    }

    /**
     * 加载插件
     * 
     * @param extension
     */
    protected JungleBootstrap doLoadExtension(String extension) {
        try {
            Class<?> extClazz = Class.forName(extension.trim(), true, DefaultBootstrap.class.getClassLoader());
            if (!JungleBootstrap.class.isAssignableFrom(extClazz)) {
                throw new IllegalStateException(
                    "Error when load extension class(interface: JungleBootstrap, class: " + extClazz.getName() + "), class is not subtype of interface");
            }
            JungleBootstrap bootstrap = (JungleBootstrap) extClazz.newInstance();
            return bootstrap;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new ExtensionException(errorFactory.loadExtensionFail(extension), e);
        }
    }

    /**
     * 获取插件地址
     * 
     * @return
     * @throws IOException
     */
    private Enumeration<URL> getExtensions() {
        Enumeration<URL> urls;
        ClassLoader classLoader = DefaultBootstrap.class.getClassLoader();

        try {
            if (classLoader != null) {
                urls = classLoader.getResources(JUNGLE_EXTENSION_FILE);
            } else {
                urls = ClassLoader.getSystemResources(JUNGLE_EXTENSION_FILE);
            }
        } catch (IOException e) {
            throw new ExtensionException(errorFactory.loadExtensionFail(JUNGLE_EXTENSION_FILE), e);
        }
        return urls;
    }

}
