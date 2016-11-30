package com.zhangk.datasource.matadata.api;


import com.zhangk.datasource.matadata.session.DataSession;

/**
 * Created by zhangkui on 16/4/27.
 */
public interface ReadAndWriteDataSessionFactory extends DataSessionFactory{

    /**
     * 获取读库数据访问session
     * @return
     * DataSession
     *
     */
    DataSession getReadDataSession();

    /**
     * 获取写库数据访问session
     * @return
     * DataSession
     *
     */
    DataSession getWriteDataSession();
}
