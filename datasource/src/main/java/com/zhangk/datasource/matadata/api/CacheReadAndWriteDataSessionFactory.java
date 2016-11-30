package com.zhangk.datasource.matadata.api;

import com.zhangk.datasource.matadata.session.CacheDataSession;
import com.zhangk.redis.factory.RedisSessionFactory;

/**
 * Created by zhangkui on 16/5/5.
 */
public interface CacheReadAndWriteDataSessionFactory extends ReadAndWriteDataSessionFactory{

    /**
     * 获取缓存工厂
     * @return
     */
    RedisSessionFactory getRedisSessionFactory();
    /**
     * 获取读库缓存数据访问session
     * @return
     * DataSession
     *
     */
    CacheDataSession getCacheReadDataSession();

    /**
     * 获取写库缓存数据访问session
     * @return
     * DataSession
     *
     */
    CacheDataSession getCacheWriteDataSession();

}
