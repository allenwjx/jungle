package com.zeh.jungle.utils.page.dal;

import java.io.Serializable;
import java.util.Collection;

/**
 * 分页集合
 * 
 * @author allen
 * @version $Id: PageList.java, v 0.1 2016年2月26日 上午11:30:09 allen Exp $
 */
public class PageList<T> implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    /** 分页器 */
    private Paginator         paginator;

    /** 数据集 */
    private Collection<T>     data;

    /**
     * 默认构造函数
     */
    public PageList() {
        paginator = new Paginator();
    }

    /**
     * 构造函数
     * 
     * @param data 数据集
     */
    public PageList(Collection<T> data) {
        this.data = data;
    }

    /**
     * 构造函数
     * 
     * @param data 数据集
     * @param paginator 分页器
     */
    public PageList(Collection<T> data, Paginator paginator) {
        this.data = data;
        this.paginator = (paginator == null) ? new Paginator() : paginator;
    }

    /**
     * 获取分页器
     * 
     * @return 分页器
     */
    public Paginator getPaginator() {
        return paginator;
    }

    /**
     * 设置分页器
     * 
     * @param paginator 分页器
     */
    public void setPaginator(Paginator paginator) {
        if (paginator != null) {
            this.paginator = paginator;
        }
    }

    /**
     * 获取数据集
     * 
     * @return 数据集
     */
    public Collection<T> getData() {
        return data;
    }
}