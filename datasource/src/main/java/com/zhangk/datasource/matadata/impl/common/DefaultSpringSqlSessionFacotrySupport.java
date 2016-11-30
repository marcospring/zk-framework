package com.zhangk.datasource.matadata.impl.common;


import com.zhangk.datasource.matadata.api.AbstractSpringSqlSessionFactorySupport;
import com.zhangk.datasource.utils.PropertiesUtils;

/**
 * Created by zhangkui on 16/4/28.
 */
public class DefaultSpringSqlSessionFacotrySupport extends AbstractSpringSqlSessionFactorySupport {
    @Override
    protected String getFactoryName(String factoryName) {
        return PropertiesUtils.getString(factoryName, "sqlSessionFactory");
    }
}
