package com.zeh.jungle.utils.page;

import java.io.Serializable;

/**
 * 分页请求模型
 * 
 * @author allen
 * @version $Id: PageRequest.java, v 0.1 2016年3月10日 下午3:37:52 allen Exp $
 */
public class PageRequest<T> implements Serializable {
    /**  */
    private static final long serialVersionUID = -1938726125791988327L;

    /** 查询条件 */
    private T                 condition;

    /** 当前页码 */
    private int               pageNum          = 1;

    /** 分页列表大小 */
    private int               pageSize         = 20;

    /**
     * 默认构造函数
     */
    public PageRequest() {
        super();
    }

    /**
     * @param condition
     * @param pageNum
     * @param pageSize
     */
    public PageRequest(T condition, int pageNum, int pageSize) {
        super();
        this.condition = condition;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    /**
     * @return
     */
    public T getCondition() {
        return condition;
    }

    /**
     * @param condition
     */
    public void setCondition(T condition) {
        this.condition = condition;
    }

    /**
     * @return
     */
    public int getPageNum() {
        return pageNum;
    }

    /**
     * @param pageNum
     */
    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    /**
     * @return
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * @param pageSize
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(pageNum).append(",").append(pageSize).append(",").append(condition);
        return sb.toString();
    }

}
