package com.zeh.jungle.fsm.core;

import com.zeh.jungle.fsm.exception.StateHandleException;

import java.util.Map;

/**
 * @author allen
 * @create $ ID: StatesManager, 18/1/15 14:56 allen Exp $
 * @since 1.0.0
 */
public interface StatesManager {
    /**
     * 注册状态委托
     *
     * @param name          状态委托名称
     * @param stateClazz    状态类型
     * @param bizTypeClazz  业务类型
     * @param stateDelegate 状态委托
     */
    <Model extends ModelWrapper<T, MBiz, MState>, T, MBiz, MState> void register(String name, Class stateClazz, Class bizTypeClazz,
                                                                                 StateDelegate<Model, T, MBiz, MState> stateDelegate);

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
    <Model extends ModelWrapper<T, MBiz, MState>, T, MBiz, MState> void setState(Model model, MBiz toBiz, MState toState,
                                                                                 Map<String, Object> properties) throws StateHandleException;

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
    <Model extends ModelWrapper<T, MBiz, MState>, T, MBiz, MState> void setState(Model model, MState toState) throws StateHandleException;

    /**
     * 根据状态委托名称获取状态委托
     *
     * @param name      状态委托名称
     * @param <Model>   业务模型
     * @param <MBiz>    业务类型
     * @param <MState>  业务状态
     * @return          状态委托
     */
    <Model extends ModelWrapper<T, MBiz, MState>, T, MBiz, MState> StateDelegate<Model, T, MBiz, MState> getDelegateByName(String name);

    /**
     * 根据状态委托名称获取状态委托
     *
     * @param stateClazz    状态Class
     * @param bizTypeClazz  业务类型Class
     * @param <Model>       业务模型
     * @param <MBiz>        业务类型
     * @param <MState>      业务状态
     * @return              状态委托
     */
    <Model extends ModelWrapper<T, MBiz, MState>, T, MBiz, MState> StateDelegate<Model, T, MBiz, MState> getDelegateByType(Class stateClazz, Class bizTypeClazz);
}
