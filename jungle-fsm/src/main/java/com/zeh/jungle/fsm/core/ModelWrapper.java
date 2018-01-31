package com.zeh.jungle.fsm.core;

/**
 * @author allen
 * @create $ ID: ModelWrapper, 18/1/15 14:17 allen Exp $
 * @since 1.0.0
 */
public interface ModelWrapper<T, MBiz, MState> {

    /**
     * 获取业务模型
     *
     * @return 业务模型
     */
    T getModel();

    /**
     * 获取业务模型编号
     *
     * @return 业务模型比编号
     */
    String getId();

    /**
     * 获取当前业务类型
     * 
     * @return 当前业务类型
     */
    MBiz getBizType();

    /**
     * 获取当前业务状态
     * 
     * @return 当前状态
     */
    MState getState();

}
