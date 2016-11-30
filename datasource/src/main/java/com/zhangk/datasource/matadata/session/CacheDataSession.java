package com.zhangk.datasource.matadata.session;


import com.zhangk.redis.api.RedisSession;

/**
 * Created by zhangkui on 16/5/5.
 */
public interface CacheDataSession extends DataSession{
    RedisSession getRedisSession();
}
