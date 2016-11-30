package com.zhangk.datasource.matadata.api;


import com.zhangk.datasource.matadata.session.DataSession;

/**
 * Created by zhangkui on 16/4/27.
 */
public interface DataSessionFactory {
    DataSession getDaoByDataSourceName(String dataSourceName);
}
