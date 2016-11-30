package com.zhangk.redis.jedis.factory;/**
 * Created by Administrator on 2015/12/30 0030.
 */


import com.zhangk.redis.api.MulitiRedisSession;
import com.zhangk.redis.api.RedisSession;
import com.zhangk.redis.exception.RedisException;
import com.zhangk.redis.factory.RedisSessionFactory;

/**
 * @ClassName: JedisClusterSessionFactory
 * @Description:TODO
 * @author: zhangk
 * @date: 2015/12/30 0030
 */
public class JedisClusterSessionFactory implements RedisSessionFactory {
    public RedisSession getSession() {
        return null;
    }

    @Override
    public MulitiRedisSession getMulitiSession() throws RedisException {
        return null;
    }
}
