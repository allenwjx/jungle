package com.zeh.jungle.core.extension;

import com.zeh.jungle.core.boot.JungleBootstrap;
import com.zeh.jungle.core.exception.ExtensionException;

import java.util.List;

/**
 * 插件加载器
 * 
 * @author allen
 * @version $Id: ExtensionLoader.java, v 0.1 2016年2月29日 下午4:15:48 allen Exp $
 */
public interface ExtensionLoader {
    /** 扩展插件配置文件 */
    static final String JUNGLE_EXTENSION_FILE = "META-INF/jungle/jungle.extension";

    /**
     * 加载扩展插件
     * 
     * @return
     */
    List<JungleBootstrap> loadExtension() throws ExtensionException;
}
