package com.zeh.jungle.core.error;

import com.zeh.jungle.core.i18n.Message;

/**
 * 错误信息基础模型
 * 
 * 错误码规范: JG0700100000
 * 第1-2位是错误码标识，固定位：JG（JG Error）；
 * 第3位是规范版本为，目前为0；
 * 第4位是错误级别，1-INFO, 3-WARN, 5-ERROR, 7-FATAL
 * 第5位是错误类别，0-系统错误, 1-业务错误, 2-第三方错误
 * 第6-9位是错误场景，全局分配，具体值见WIKI(TODO: WIKI地址)错误场景表, 系统的错误场景码为: 0100
 * 第10-12位是业务场景下的具体编码
 * 
 * @author allen
 * @version $Id: JGError.java, v 0.1 2016年2月26日 下午2:42:20 allen Exp $
 */
public class JGError extends Message {
    /**  */
    private static final long  serialVersionUID   = 1L;

    /** 默认错误消息, 应用程序切勿使用该错误信息，只有SOF容器使用该错误信息 */
    public static final String DEFAULT_ERROR_MSG  = "系统错误";

    /** 默认错误码, 应用程序切勿使用该错误码，只有SOF容器使用该错误码*/
    public static final String DEFAULT_ERROR_CODE = "JG0700100000";

    /** 错误码 */
    private String             code;

    /**
     * @param errMsg 错误文案模板
     * @param code 错误码
     * @param args 错误文案模板占位符参数
     */
    protected JGError(String errMsg, String code, Object... args) {
        super(errMsg, code, args);
        this.code = code;
    }

    /**
    * 获取错误码
    * 
    * @return 错误码
    */
    public String getCode() {
        return code;
    }

    /**
     * @return
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "JGError [code=" + code + ", err=" + getMessage() + "]";
    }
}
