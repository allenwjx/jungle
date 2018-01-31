package com.zeh.jungle.fsm;

/**
 * @author allen
 * @create $ ID: FSMConstants, 18/1/12 13:44 allen Exp $
 * @since 1.0.0
 */
public class FSMConstants {
    /** 业务与状态间分隔符 */
    public static final String SPLIT0    = "|";
    /** 业务与业务，状态与状态间分隔符 */
    public static final String SPLIT1    = "^";
    /** 替代字符 */
    public static final String PLACEHOLD = "$";
    /** 状态机处理异常 */
    public static final String FSM_ERROR = "-2";

    /**
     * 私有化构造方法
     */
    private FSMConstants() {
    }
}
