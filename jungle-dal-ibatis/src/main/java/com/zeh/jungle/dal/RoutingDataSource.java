package com.zeh.jungle.dal;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 数据源路由,基于spring jdbc AbstractRoutingDataSource<br/>
 * 根本不同的DataRoutingContextHolder context选择datasource
 * @author allen
 * @version $Id: RoutingDataSource.java, v 0.1 2016年4月29日 下午2:09:43 wb30644 Exp $
 */
public class RoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DataRoutingContextHolder.getContext();
    }

}
