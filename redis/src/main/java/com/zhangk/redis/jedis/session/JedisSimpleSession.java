package com.zhangk.redis.jedis.session;

import com.csyy.common.JSONUtils;
import com.zhangk.redis.api.RedisSession;
import com.zhangk.redis.exception.RedisException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.Objects;

/**
 * @ClassName: JedisSimpleSession
 * @Description:TODO
 * @author: zhangk
 * @date: 2015/12/30 0030
 */
public class JedisSimpleSession implements RedisSession {

    private Logger logger = LoggerFactory.getLogger(getClass());
    protected static final int DEFAULT_TIMEOUT = 86400;
    /**
     * 默认的 {@code JSON} 完整日期/时间字段的格式化模式。
     */
    protected Jedis jedis;


    @Override
    public <T> T getTypeObject(Class<T> clazz, String key) throws RedisException {
        T t = null;
        String objStr = getData(key);
        logger.debug("[根据key:{},获得:{}]", key, objStr);
        try {
            if(objStr == null || "".equals(objStr))
                return t;
            t = JSONUtils.fromJson(objStr, clazz);
        } catch (Exception e) {
            logger.error("[redis错误:{}]", e);
            throw new RedisException(e);
        }
        return t;
    }

    @Override
    public String getData(String key) throws RedisException {
        String objStr;
        try {
            Object o = getJedis().get(key);
            if (o == null)
                return null;
            objStr = Objects.toString(o);
            logger.debug("[根据key:{},获得:{}]", key, objStr);
        } catch (Exception e) {
            logger.error("[redis错误:{}]", e);
            throw new RedisException(e);
        }
        return objStr;
    }

    @Override
    public void setData(String key, Object value) throws RedisException {
        try {
            if (key == null) {
                logger.info("[存入key:{},key:{}为空！]", key, value);
                throw new RedisException("存入键为空!");
            }
            if (value == null) {
                logger.info("[存入key:{},value:{}为空！]", key, value);
                throw new RedisException("存入值为空!");
            }
            setData(key, value.toString(), DEFAULT_TIMEOUT);
            logger.debug("[存入key:{},value:{}]", key, Objects.toString(value));
        } catch (Exception e) {
            logger.error("[redis错误:{}]", e);
            throw new RedisException(e);
        }
    }

    @Override
    public void setData(String key, Object value, int timeout) throws RedisException {
        try {
            if (key == null) {
                logger.info("[存入key:{},key:{}为空！]", key, value);
                throw new RedisException("存入键为空!");
            }
            if (value == null) {
                logger.info("[存入key:{},value:{}为空！]", key, Objects.toString(value));
                throw new RedisException("存入值为空!");
            }
            if (timeout == 0)
                timeout = DEFAULT_TIMEOUT;
            getJedis().setex(key, timeout, value.toString());
            logger.info("[存入key:{},value:{}]", key, Objects.toString(value));
        } catch (Exception e) {
            logger.error("[redis错误:{}]", e);
            throw new RedisException(e);
        }
    }

    @Override
    public boolean contains(String key) throws RedisException {
        try {
            return getJedis().exists(key);
        } catch (Exception e) {
            logger.error("[redis错误:{}]", e);
            throw new RedisException(e);
        }
    }

    @Override
    public long getKeyLastTime(String key) throws RedisException {
        try {
            if (key == null) {
                logger.info("[key:{}为空！]", key);
                throw new RedisException("存入键为空!");
            }
            long timeout = getJedis().ttl(key);
            logger.info("key:{},剩余超时时间为：{}", key, timeout);
            return timeout;
        } catch (Exception e) {
            logger.error("[redis错误:{}]", e);
            throw new RedisException(e);
        }
    }

    public Jedis getJedis() {
        return jedis;
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }

    @Override
    public long delete(String key) throws RedisException {
        try {
            return this.getJedis().del(key);
        } catch (Exception e) {
            logger.error("[redis错误:{}]", e);
            throw new RedisException(e);
        }
    }

    @Override
    public void flushAll() throws RedisException {
        try {
            this.getJedis().flushAll();
        } catch (Exception e) {
            logger.error("[redis错误:{}]", e);
            throw new RedisException(e);
        }
    }

    @Override
    public long setnx(String key, Object value) throws RedisException {
        try {
            if (key == null) {
                logger.info("[key:{}为空！]", key);
                throw new RedisException("存入键为空!");
            }
            if (value == null) {
                logger.info("[存入key:{},value:{}为空！]", key, Objects.toString(value));
                throw new RedisException("存入值为空!");
            }
            long result = getJedis().setnx(key, value.toString());
            return result;
        } catch (Exception e) {
            logger.error("[redis错误:{}]", e);
            throw new RedisException(e);
        }
    }

    @Override
    public long push(String key, String... value) throws RedisException {
        try {
            if (key == null) {
                logger.info("[存入队列key:{},key:{}为空！]", key, value);
                throw new RedisException("存入键为空!");
            }
            if (value == null) {
                logger.info("[存入队列key:{},value:{}为空！]", key, value);
                throw new RedisException("存入值为空!");
            }
            long result = getJedis().lpush(key, value);
            logger.debug("[存入队列key:{},value:{}]", key, Objects.toString(value));
            return result;
        } catch (Exception e) {
            logger.error("[redis错误:{}]", e);
            throw new RedisException(e);
        }
    }

    @Override
    public String pop(String key) throws RedisException {
        try {
            String o = getJedis().rpop(key);
            if (o == null)
                return null;
            logger.debug("[根据key:{},获得:{}]", key, o);
            return o;
        } catch (Exception e) {
            logger.error("[redis错误:{}]", e);
            throw new RedisException(e);
        }
    }

    @Override
    public long expire(String key, int seconds) throws RedisException {
        if (key == null) {
            logger.info("[key:{}为空！]", key);
            throw new RedisException("存入键为空!");
        }
        if (seconds <= 0) {
            logger.info("[超时时间应为大于零的整数,输入值为{}！]", seconds);
            throw new RedisException("存入值为空!");
        }
        long result = getJedis().expire(key, seconds);
        return result;
    }


    @Override
    public void publish(String chanel, Object msg) {
        String msgJson = JSONUtils.toJson(msg);
        publish(chanel,msgJson);
    }

    @Override
    public void publish(String chanel, String msgJson) {
        getJedis().publish(chanel, msgJson);
    }

    @Override
    public void subscribe(JedisPubSub jedisPubSub, String... channels) {
        getJedis().subscribe(jedisPubSub, channels);
    }
}
