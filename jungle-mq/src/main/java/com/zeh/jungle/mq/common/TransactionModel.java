package com.zeh.jungle.mq.common;

import org.apache.rocketmq.client.producer.LocalTransactionExecuter;

/**
 * 
 * @author allen
 * @version $Id: TransactionModel.java, v 0.1 2016年3月2日 下午3:31:11 allen Exp $
 */
public class TransactionModel {
    /** 本地事务校验执行器 */
    private LocalTransactionExecuter tranExecuter;

    /** 本地事务校验执行器参数 */
    private Object                   args;

    /**
     * 获取LocalTransactionExecuter
     * 
     * @return
     */
    public LocalTransactionExecuter getTranExecuter() {
        return tranExecuter;
    }

    /**
     * 设置LocalTransactionExecuter
     * 
     * @param tranExecuter
     */
    public void setTranExecuter(LocalTransactionExecuter tranExecuter) {
        this.tranExecuter = tranExecuter;
    }

    /**
     * 获取args
     * 
     * @return
     */
    public Object getArgs() {
        return args;
    }

    /**
     * 设置args
     * 
     * @param args
     */
    public void setArgs(Object args) {
        this.args = args;
    }
}
