package com.zeh.jungle.fsm.core;

import java.util.Map;

import com.zeh.jungle.fsm.FSMConstants;
import com.zeh.jungle.fsm.exception.StateHandleException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

/**
 * @author allen
 * @create $ ID: DefaultStatesManager, 18/1/15 14:35 allen Exp $
 * @since 1.0.0
 */
public class DefaultStatesManager implements StatesManager {
    /** Logger */
    private static final Logger                     LOGGER               = LoggerFactory.getLogger(DefaultStatesManager.class);

    /** 状态委托哈希表，KEY：状态类型+业务类型 */
    private static final Map<String, StateDelegate> stateDelegates       = Maps.newHashMap();

    /** 状态委托哈希表，KEY：状态委托名称 */
    private static final Map<String, StateDelegate> stateDelegatesByName = Maps.newHashMap();

    /**
     * 注册状态委托
     *
     * @param name          状态委托名称
     * @param stateClazz    状态类型
     * @param bizTypeClazz  业务类型
     * @param stateDelegate 状态委托
     */
    @Override
    public void register(String name, Class stateClazz, Class bizTypeClazz, StateDelegate stateDelegate) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("State delegate name cannot be null");
        }
        if (stateClazz == null || bizTypeClazz == null) {
            throw new IllegalArgumentException("stateClass or bizTypeClass cannot be null");
        }
        if (stateDelegate == null) {
            throw new IllegalArgumentException("StateDelegate cannot be null");
        }
        String key = getDelegateKey(stateClazz, bizTypeClazz);
        stateDelegates.put(key, stateDelegate);
        stateDelegatesByName.put(name, stateDelegate);
        LOGGER.info("Register the state delegate: key: " + key + ", type: " + stateDelegate.getClass().getName());
    }

    /**
     * 业务状态驱动
     * 
     * @param model                 业务模型
     * @param toBiz                 目标业务类型
     * @param toState               目标业务状态
     * @param properties            扩展属性
     * @param <Model>               业务模型
     * @param <MBiz>                业务类型
     * @param <MState>              业务状态
     * @throws StateHandleException 状态驱动异常
     */
    @Override
    public <Model extends ModelWrapper<T, MBiz, MState>, T, MBiz, MState> void setState(Model model, MBiz toBiz, MState toState,
                                                                                        Map<String, Object> properties) throws StateHandleException {
        if (model == null) {
            throw new IllegalArgumentException("Model cannot be null");
        }
        MBiz biz = model.getBizType();
        if (biz == null) {
            throw new IllegalArgumentException("The biz type of model cannot be null");
        }
        MState state = model.getState();
        if (state == null) {
            throw new IllegalArgumentException("The state of model cannot be null");
        }
        String key = getDelegateKey(state.getClass(), biz.getClass());
        StateDelegate<Model, T, MBiz, MState> delegate = stateDelegates.get(key);
        if (delegate == null) {
            throw new IllegalStateException("Cannot find the state delegate by key " + key);
        }

        // set state
        delegate.setState(model, toBiz, toState, properties);
    }

    /**
     * 业务状态驱动(业务类型一致)
     * 
     * @param model                 业务模型
     * @param toState               目标业务状态
     * @param <Model>               业务模型
     * @param <MBiz>                业务类型
     * @param <MState>              业务状态
     * @throws StateHandleException 状态驱动异常
     */
    @Override
    public <Model extends ModelWrapper<T, MBiz, MState>, T, MBiz, MState> void setState(Model model, MState toState) throws StateHandleException {
        setState(model, model.getBizType(), toState, null);
    }

    /**
     * 根据状态委托名称获取状态委托
     *
     * @param name 状态委托名称
     * @return 状态委托
     */
    @Override
    public <Model extends ModelWrapper<T, MBiz, MState>, T, MBiz, MState> StateDelegate<Model, T, MBiz, MState> getDelegateByName(String name) {
        if (stateDelegatesByName == null) {
            return null;
        }
        return stateDelegatesByName.get(name);
    }

    /**
     * 根据状态委托名称获取状态委托
     *
     * @param stateClazz   状态Class
     * @param bizTypeClazz 业务类型Class
     * @return 状态委托
     */
    @Override
    public <Model extends ModelWrapper<T, MBiz, MState>, T, MBiz, MState> StateDelegate<Model, T, MBiz, MState> getDelegateByType(Class stateClazz, Class bizTypeClazz) {
        if (stateDelegates == null) {
            return null;
        }
        String key = getDelegateKey(stateClazz, bizTypeClazz);
        return stateDelegates.get(key);
    }

    /**
     * 获取<code>StateDelegate</code>哈希表Key
     *
     * @param stateClazz 状态类型
     * @param bizTypeClazz 业务类型
     * @return <code>StateDelegate</code>哈希表Key
     */
    private String getDelegateKey(Class stateClazz, Class bizTypeClazz) {
        return stateClazz.getName() + FSMConstants.SPLIT1 + bizTypeClazz.getName();
    }

}
