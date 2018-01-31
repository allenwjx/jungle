package com.zeh.jungle.utils.page.dal;

import java.util.List;
import java.util.Map;

import com.zeh.jungle.utils.common.BeanMapper;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

/**
 * ibatis分页查询工具类
 * 
 * @author allen
 * @version $Id: PageQueryUtils.java, v 0.1 2016年2月27日 上午11:17:21 allen Exp $
 */
public class PageQueryUtils {

    /**
     * ibatis分页查询
     * 
     * @param sqlMapClientTemplate ibatis jdbc客户端模板
     * @param statementName 分页查询sql
     * @param parameterObject 查询条件，包含查询起始页，查询数据量
     * @return
     */
    public static <T> PageList<T> pageQuery(SqlMapClientTemplate sqlMapClientTemplate, String statementName, PageQuery parameterObject) {
        return pageQuery(sqlMapClientTemplate, statementName, statementName + ".count", parameterObject, parameterObject.getPage(), parameterObject.getPageSize());
    }

    /**
     * ibatis分页查询
     * 
     * @param sqlMapClientTemplate ibatis jdbc客户端模板
     * @param statementName 分页查询sql
     * @param countStatementName 数据量统计sql
     * @param parameterObject 查询条件，包含查询起始页，查询数据量
     * @return
     */
    public static <T> PageList<T> pageQuery(SqlMapClientTemplate sqlMapClientTemplate, String statementName, String countStatementName, PageQuery parameterObject) {
        return pageQuery(sqlMapClientTemplate, statementName, countStatementName, parameterObject, parameterObject.getPage(), parameterObject.getPageSize());
    }

    /**
     * ibatis分页查询
     * 
     * @param sqlMapClientTemplate ibatis jdbc客户端模板
     * @param statementName 分页查询sql
     * @param parameterObject 查询条件，不包含查询起始页，查询数据量
     * @param page 查询起始页
     * @param pageSize 查询数据量
     * @return
     */
    public static <T> PageList<T> pageQuery(SqlMapClientTemplate sqlMapClientTemplate, String statementName, Object parameterObject, int page, int pageSize) {
        return pageQuery(sqlMapClientTemplate, statementName, statementName + ".count", parameterObject, page, pageSize);
    }

    /**
     * ibatis分页查询
     * 
     * @param sqlMapClientTemplate ibatis jdbc客户端模板
     * @param statementName 分页查询sql
     * @param countStatementName 数据量统计sql
     * @param parameterObject 查询条件，不包含查询起始页，查询数据量
     * @param page 查询起始页
     * @param pageSize 查询数据量
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> PageList<T> pageQuery(SqlMapClientTemplate sqlMapClientTemplate, String statementName, String countStatementName, Object parameterObject, int page,
                                            int pageSize) {
        // execute page count query
        Number totalCount = (Number) sqlMapClientTemplate.queryForObject(countStatementName, parameterObject);

        if (totalCount.intValue() > 0) {
            Map<String, Object> sqlParams = BeanMapper.bean2Map(parameterObject);
            int beginIndex = getBeginIndex(page, pageSize);
            sqlParams.put("offset", beginIndex);
            sqlParams.put("limit", pageSize);

            // execute page query
            List<T> resultSet = sqlMapClientTemplate.queryForList(statementName, sqlParams);

            return PageUtils.createPageList(resultSet, page, pageSize, totalCount.intValue());
        }
        return PageUtils.createEmptyPageList(page, pageSize);
    }

    /**
     * 获取查询起始游标
     * 
     * @param page 查询起始页
     * @param pageSize 单页数据量
     * @return 查询起始游标
     */
    private static int getBeginIndex(int page, int pageSize) {
        int beginIndex = 0;
        if (page > 0) {
            if (pageSize > 0) {
                beginIndex = (page - 1) * pageSize;
            }
        }
        return beginIndex;
    }
}
