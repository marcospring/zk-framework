package com.zhangk.datasource.matadata.impl.cache;


import com.zhangk.datasource.matadata.api.CacheReadAndWriteDataSessionFactory;
import com.zhangk.datasource.matadata.impl.common.DefaultReadAndWriteDataSessionFactory;
import com.zhangk.datasource.matadata.session.CacheDataSession;
import com.zhangk.redis.factory.RedisSessionFactory;

/**
 *默认的带有缓存功能的读写分离CacheDataSession工厂,用于获取带有缓存功能的数据库访问会话
 * Created by zhangkui on 16/5/5.
 */
public class DefaultCacheReadAndWriteDataSessionFactory extends DefaultReadAndWriteDataSessionFactory implements CacheReadAndWriteDataSessionFactory {

    private RedisSessionFactory redisSessionFactory;

    private CacheDataSession cacheReadDataSession;
    private CacheDataSession cacheWriteDataSession;

    @Override
    public CacheDataSession getCacheReadDataSession() {
        if (cacheReadDataSession != null)
            return cacheReadDataSession;
        cacheReadDataSession = new DefaultCacheDataSession(getSupport().getSqlSessionFacotry(getReadDataSource()),redisSessionFactory);
        return cacheReadDataSession;
    }

    @Override
    public CacheDataSession getCacheWriteDataSession() {
        if (cacheWriteDataSession != null)
            return cacheWriteDataSession;
        cacheWriteDataSession = new DefaultCacheDataSession(getSupport().getSqlSessionFacotry(getWriteDataSource()),redisSessionFactory);
        return cacheWriteDataSession;
    }

    @Override
    public RedisSessionFactory getRedisSessionFactory() {
        return redisSessionFactory;
    }

    public void setRedisSessionFactory(RedisSessionFactory redisSessionFactory) {
        this.redisSessionFactory = redisSessionFactory;
    }
}
