package com.zeh.jungle.dal.paginator;

import java.io.Serializable;

/**
 * 基础分页查询模型
 * 
 * @author allen
 * @version $Id: PageQuery.java, v 0.1 2016年2月26日 上午11:35:37 allen Exp $
 */
public class PageQuery implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    /** 查询页, 默认第一页 */
    private int               page             = 1;

    /** 单页数据量 */
    private int               pageSize         = 20;

    /**
     * 构造函数
     */
    public PageQuery() {
        this.page = 1;
        this.pageSize = 20;
    }

    /**
     * 构造函数
     * 
     * @param page 查询起始页
     * @param pageSize 单页数据量
     */
    public PageQuery(int page, int pageSize) {
        super();
        this.page = page;
        this.pageSize = pageSize;
    }

    /**
     * 获取查询页
     * 
     * @return 查询页码
     */
    public int getPage() {
        return page;
    }

    /**
     * 设置查询页
     * 
     * @param page
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * 获取每页数据量
     * 
     * @return 每页数据量
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 设置每页数据量
     * 
     * @param pageSize
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 获取查询起始游标
     * 
     * @return 查询起始游标
     */
    public int getBeginIndex() {
        int beginIndex = 0;
        if (page > 0) {
            if (pageSize > 0) {
                beginIndex = (page - 1) * pageSize;
            }
        }
        return beginIndex;
    }

    @Override
    public String toString() {
        return "PageQuery [page=" + page + ", pageSize=" + pageSize + "]";
    }
}
