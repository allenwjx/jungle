package com.zeh.jungle.utils.page.dal;

import com.zeh.jungle.utils.page.PageInfo;
import com.zeh.jungle.utils.page.PageResult;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 创建{@link PageList}实例工具
 * 
 * @author allen
 * @version $Id: PageUtils.java, v 0.1 2016年2月26日 下午12:35:53 allen Exp $
 */
public class PageUtils {

    /**
     * 构造函数
     */
    private PageUtils() {
    }

    /**
     * 创建空查询结果集合{@link PageList}
     * 
     * @param query 查询条件
     * @return
     */
    public static <T> PageList<T> createEmptyPageList(PageQuery query) {
        return new PageList<T>(new ArrayList<T>(0), new Paginator(0, query.getPageSize(), query.getPage()));
    }

    /**
     * 创建空查询结果集合{@link PageList}
     * 
     * @param page 查询页
     * @param pageSize 查询数据量
     * @return
     */
    public static <T> PageList<T> createEmptyPageList(int page, int pageSize) {
        return new PageList<T>(new ArrayList<T>(0), new Paginator(0, pageSize, page));
    }

    /**
     * 查询分页查询结果集
     * 
     * @param data 获取的数据
     * @param query 查询条件
     * @param totalCnt 符合查询条件的数据总量
     * @return 分页查询结果集
     */
    public static <T> PageList<T> createPageList(Collection<T> data, PageQuery query, int totalCnt) {
        Paginator paginator = new Paginator(totalCnt, query.getPageSize(), query.getPage());
        return new PageList<T>(data, paginator);
    }

    /**
     * 查询分页查询结果集
     * 
     * @param data 获取的数据
     * @param page 查询起始游标
     * @param pageSize 查询数据量
     * @param totalCnt 符合查询条件的数据总量
     * @return
     */
    public static <T> PageList<T> createPageList(Collection<T> data, int page, int pageSize, int totalCnt) {
        Paginator paginator = new Paginator(totalCnt, pageSize, page);
        return new PageList<T>(data, paginator);
    }

    // ~ facade base model to page

    /**
     * Facade模块PageResult数据模型转化为PageList
     * 
     * @param pageResult
     * @return
     */
    public static <T> PageList<T> createPageList(PageResult<T> pageResult) {
        PageInfo pageInfo = pageResult.getPageInfo();
        Paginator paginator = new Paginator(pageInfo.getItems(), pageInfo.getItemsPerPage(), pageInfo.getPage());
        return new PageList<T>(pageResult.getValues(), paginator);
    }

    /**
     * PageList转换为Facade模块PageResult数据模型
     * 
     * @param pageList
     * @return
     */
    public static <T> PageResult<T> createPageResult(PageList<T> pageList) {
        PageInfo pageInfo = new PageInfo();
        pageInfo.setItems(pageList.getPaginator().getTotalCount());
        pageInfo.setItemsPerPage(pageList.getPaginator().getPageSize());
        pageInfo.setPage(pageList.getPaginator().getCurrentPage());

        PageResult<T> pageResult = new PageResult<T>(pageList.getData());
        pageResult.setPageInfo(pageInfo);
        return pageResult;
    }

    /**
     * 创建PageResult
     * 
     * @param data
     * @param paginator
     * @return
     */
    public static <T> PageResult<T> createPageResult(Collection<T> data, Paginator paginator) {
        return new PageResult<T>(data, paginator.getCurrentPage(), paginator.getPageSize(), paginator.getTotalCount());
    }
}
