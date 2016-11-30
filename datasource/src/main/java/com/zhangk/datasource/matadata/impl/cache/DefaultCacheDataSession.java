package com.zhangk.datasource.matadata.impl.cache;


import com.csyy.common.JSONUtils;
import com.csyy.common.UUIDProvider;
import com.zhangk.redis.exception.RedisException;
import com.zhangk.datasource.constant.Constants;
import com.zhangk.datasource.exception.DataAccessException;
import com.zhangk.datasource.matadata.impl.common.DefaultDataSession;
import com.zhangk.datasource.matadata.param.CustomSQL;
import com.zhangk.datasource.matadata.param.ESQL;
import com.zhangk.datasource.matadata.param.Param;
import com.zhangk.datasource.matadata.param.SQLCreator;
import com.zhangk.datasource.matadata.session.CacheDataSession;
import com.zhangk.datasource.utils.CacheKeyUtils;
import com.zhangk.datasource.utils.SQLUtil;
import com.zhangk.redis.api.RedisSession;
import com.zhangk.redis.factory.RedisSessionFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangkui on 16/5/5.
 */
public class DefaultCacheDataSession extends DefaultDataSession implements CacheDataSession {

    public static final Logger logger = LoggerFactory.getLogger(DefaultDataSession.class);
    private SqlSessionFactory sqlSessionFactory;
    private RedisSessionFactory redisSessionFactory;
    private SqlSession session;

    public DefaultCacheDataSession() {
    }


    public DefaultCacheDataSession(SqlSessionFactory sqlSessionFactory, RedisSessionFactory redisSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.redisSessionFactory = redisSessionFactory;
    }

    @Override
    public RedisSession getRedisSession() {
        return redisSessionFactory.getSession();
    }

    @Override
    public SqlSession getSession() {
        if (session == null)
            session = new SqlSessionTemplate(sqlSessionFactory);
        return session;
    }

    @Override
    public <T> T save(Class<T> clazz, T po) throws DataAccessException {
        T result;
        try {
            result = super.save(clazz, po);
            updateTableKey(clazz);
            updateDatabaseKey();
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
        return result;
    }


    @Override
    public <T> int update(Class<T> clazz, T po) throws DataAccessException {
        int result;
        try {
            result = super.update(clazz, po);
            updateTableKey(clazz);
            updateDatabaseKey();
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
        return result;
    }


    @Override
    public <T> int updateCustomColumnByWhere(Class<T> clazz, Param param, CustomSQL whereSql) throws DataAccessException {
        int result;
        try {
            result = super.updateCustomColumnByWhere(clazz, param, whereSql);
            updateTableKey(clazz);
            updateDatabaseKey();
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
        return result;
    }

    @Override
    public <T> int logicDelete(Class<T> clazz, Param param) throws DataAccessException {
        int result;
        try {
            result = super.logicDelete(clazz, param);
            updateTableKey(clazz);
            updateDatabaseKey();
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
        return result;
    }

    @Override
    public <T> int logicWhereDelete(Class<T> clazz, CustomSQL whereSql) throws DataAccessException {
        int result;
        try {
            result = super.logicWhereDelete(clazz, whereSql);
            updateTableKey(clazz);
            updateDatabaseKey();
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
        return result;
    }

    @Override
    public <T> int physicalDelete(Class<T> clazz, Integer id) throws DataAccessException {
        int result;
        try {
            result = super.physicalDelete(clazz, id);
            updateTableKey(clazz);
            updateDatabaseKey();
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
        return result;
    }

    @Override
    public <T> int physicalWhereDelete(Class<T> clazz, CustomSQL whereSql) throws DataAccessException {
        int result;
        try {
            result = super.physicalWhereDelete(clazz, whereSql);
            updateTableKey(clazz);
            updateDatabaseKey();
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
        return result;
    }

    @Override
    public <T> T querySingleResultById(Class<T> clazz, int id) throws DataAccessException {
        T obj;
        try {
            RedisSession redis = redisSessionFactory.getSession();
            //未获取缓存链接,执行数据库操作并返回
            if (redis == null) {
                obj = super.querySingleResultById(clazz, id);
            } else {
                //如果是行ID缓存行,则将行缓存的表对应的类名做前缀拼接
                String tableCacheKey = redis.getData(CacheKeyUtils.createTableKey(clazz));
                String rowKey = CacheKeyUtils
                        .createRowResultCacheKey(tableCacheKey, String.valueOf(id),clazz.getSimpleName());
                obj = redis.getTypeObject(clazz, rowKey);
                //如果getTypeObject方法中转化错误,返回NULL,则删除redis中的缓存
                if (obj == null) {
                    obj = super.querySingleResultById(clazz, id);
                    if (obj == null)
                        redis.delete(rowKey);
                    else
                        redis.setData(rowKey, JSONUtils.toJson(obj, JSONUtils.DEFAULT_DATE_TIME_PATTERN));
                }
                logger.info("[根据rowKey:{},获取值:{}]", rowKey, JSONUtils.toJson(obj, JSONUtils.DEFAULT_DATE_TIME_PATTERN));
            }
        } catch (RedisException e) {
            logger.error("querySingleResultById:RedisException:{}", e);
            obj = super.querySingleResultById(clazz, id);
        } catch (Exception e) {
            logger.error("cacheDao访问异常:{}", e);
            throw new DataAccessException(e);
        }
        return obj;
    }

    @Override
    public <T> T querySingleResultByUUID(Class<T> clazz, String uuid) throws DataAccessException {
        T obj;
        try {
            RedisSession redis = redisSessionFactory.getSession();
            //未获取缓存链接,执行数据库操作并返回
            if (redis == null) {
                obj = super.querySingleResultByUUID(clazz, uuid);
            } else {
                //如果是UUID缓存行,则直接用数据行的UUID作为缓存KEY
                String tableCacheKey = redis.getData(CacheKeyUtils.createTableKey(clazz));
                String rowKey = CacheKeyUtils.createRowResultCacheKey(tableCacheKey, uuid,clazz.getSimpleName());
                obj = redis.getTypeObject(clazz, rowKey);
                if (obj == null) {
                    obj = super.querySingleResultByUUID(clazz, uuid);
                    if (obj == null)
                        redis.delete(rowKey);
                    else
                        redis.setData(rowKey, JSONUtils.toJson(obj, JSONUtils.DEFAULT_DATE_TIME_PATTERN));
                }
                logger.info("[根据rowKey:{},获取值:{}]", rowKey, JSONUtils.toJson(obj, JSONUtils.DEFAULT_DATE_TIME_PATTERN));
            }
           } catch (RedisException e) {
            logger.error("querySingleResultByUUID:RedisException:{}", e);
            obj = super.querySingleResultByUUID(clazz, uuid);
        } catch (Exception e) {
            logger.error("cacheDao访问异常:{}", e);
            throw new DataAccessException(e);
        }
        return obj;
    }

    @Override
    public <T> T querySingleResultByParams(Class<T> clazz, Param param) throws DataAccessException {
        T obj;
        try {
            RedisSession redis = redisSessionFactory.getSession();
            //未获取缓存链接,执行数据库操作并返回
            if (redis == null) {
                obj = super.querySingleResultByParams(clazz, param);
            } else {
                //表缓存的KEY由两部分数据组成  表缓存key=md5(前缀+表缓存所在表的缓存key+_+sql)
                String resultKey = createTableResultKey(clazz, ESQL.QUERYSINGLERESULTBYPARAMS, param.get(), redis);
                obj = redis.getTypeObject(clazz, resultKey);
                if (obj == null) {
                    obj = super.querySingleResultByParams(clazz, param);
                    if (obj == null)
                        redis.delete(resultKey);
                    else
                        redis.setData(resultKey, JSONUtils.toJson(obj, JSONUtils.DEFAULT_DATE_TIME_PATTERN));
                }
                logger.info("[根据tableKey:{},获取值:{}]", resultKey, JSONUtils.toJson(obj));
            }

        } catch (RedisException e) {
            logger.error("querySingleResultByParams:RedisException:{}", e);
            obj = super.querySingleResultByParams(clazz, param);
        } catch (Exception e) {
            logger.error("cacheDao访问异常:{}", e);
            throw new DataAccessException(e);
        }
        return obj;
    }

    @Override
    public <T> List<T> queryListResult(Class<T> clazz, Param param) throws DataAccessException {
        List<T> list;
        try {
            RedisSession redis = redisSessionFactory.getSession();
            //未获取缓存链接,执行数据库操作并返回
            if (redis == null) {
                list = super.queryListResult(clazz, param);
            } else {
                String resultKey = createTableResultKey(clazz, ESQL.QUERYLISTRESULT, param.get(), redis);
                String jsonResult = redis.getData(resultKey);
                if (StringUtils.isEmpty(jsonResult)) {
                    list = super.queryListResult(clazz, param);
                    if (list == null) {
                        redis.delete(resultKey);
                    } else {
                        jsonResult = JSONUtils.toJson(list, JSONUtils.DEFAULT_DATE_TIME_PATTERN);
                        redis.setData(resultKey, jsonResult);
                    }
                } else {
                    list = JSONUtils.fromListJson(jsonResult, clazz, false);
                }
                logger.info("[根据tableKey:{},获取值:{}]", resultKey, jsonResult);
            }
        } catch (RedisException e) {
            logger.error("queryListResult:RedisException:{}", e);
            list = super.queryListResult(clazz, param);
        } catch (Exception e) {
            logger.error("cacheDao访问异常:{}", e);
            throw new DataAccessException(e);
        }
        return list;
    }

    @Override
    public <T> List<T> queryListResultByWhere(Class<T> clazz, CustomSQL whereSQL) throws DataAccessException {
        List<T> list;
        try {
            RedisSession redis = redisSessionFactory.getSession();
            //未获取缓存链接,执行数据库操作并返回
            if (redis == null) {
                list = super.queryListResultByWhere(clazz, whereSQL);
            } else {
                String whereSqlStr = whereSQL.toString();
                Map<String, Object> map = new HashMap<>();
                map.put("whereSqlStr", whereSqlStr);
                String resultKey = createTableResultKey(clazz, ESQL.QUERYLISTRESULTBYWHERE, map, redis);
                String jsonResult = redis.getData(resultKey);
                if (StringUtils.isEmpty(jsonResult)) {
                    list = super.queryListResultByWhere(clazz, whereSQL);
                    if (list == null) {
                        redis.delete(resultKey);
                    } else {
                        jsonResult = JSONUtils.toJson(list, JSONUtils.DEFAULT_DATE_TIME_PATTERN);
                        redis.setData(resultKey, jsonResult);
                    }
                } else {
                    list = JSONUtils.fromListJson(jsonResult, clazz, false);
                }
                logger.info("[根据tableKey:{},获取值:{}]", resultKey, jsonResult);
            }
        } catch (RedisException e) {
            logger.error("queryListResultByWhere:RedisException:{}", e);
            list = super.queryListResultByWhere(clazz, whereSQL);
        } catch (Exception e) {
            logger.error("cacheDao访问异常:{}", e);
            throw new DataAccessException(e);
        }
        return list;
    }

//    @Override
//    public <T> long queryListResultCount(Class<T> clazz, Param param) throws DataAccessException {
//        try {
//            RedisSession redis = redisSessionFactory.getSession();
//            //未获取缓存链接,执行数据库操作并返回
//            if (redis == null) {
//
//            } else {
//
//            }
//        } catch (Exception e) {
//            logger.error("cacheDao访问异常:{}", e);
//            throw new DataAccessException(e);
//        }
//        return 0;
//    }

//    @Override
//    public <T> long queryListResultCountByWhere(Class<T> clazz, CustomSQL whereSQL) {
//        try {
//            RedisSession redis = redisSessionFactory.getSession();
//            //未获取缓存链接,执行数据库操作并返回
//            if (redis == null) {
//
//            } else {
//
//            }
//        } catch (Exception e) {
//            logger.error("cacheDao访问异常:{}", e);
//            throw new DataAccessException(e);
//        }
//        return 0;
//    }

    @Override
    public <T> void insertBatch(Class<T> clazz,Throwable able, List list) throws DataAccessException {
        int result;
        try {
            super.insertBatch(clazz,able,list);
            updateTableKey(clazz);
            updateDatabaseKey();
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
        return;
    }

    @Override
    public <T> T querySingleVO(Class<T> formater,Throwable able, Param param) throws DataAccessException {
        T obj;
        try {
            RedisSession redis = redisSessionFactory.getSession();
            //未获取缓存链接,执行数据库操作并返回
            if (redis == null) {
                obj = super.querySingleVO(formater,able, param);
            } else {
                String resultKey = createDatabaseResultKey(able, null, param.get(), redis);
                String jsonResult = redis.getData(resultKey);
                if (StringUtils.isEmpty(jsonResult)) {
                    obj = super.querySingleVO(formater,able, param);
                    if (obj == null) {
                        redis.delete(resultKey);
                    } else {
                        jsonResult = JSONUtils.toJson(obj);
                        redis.setData(resultKey, jsonResult);
                    }
                } else {
                    obj = JSONUtils.fromJson(jsonResult,formater);
                }
                logger.info("[根据tableKey:{},获取值:{}]", resultKey, jsonResult);
            }
        } catch (RedisException e) {
            logger.error("querySingleVO:RedisException:{}", e);
            obj = super.querySingleVO(formater,able, param);
        } catch (Exception e) {
            logger.error("cacheDao访问异常:{}", e);
            throw new DataAccessException(e);
        }
        return obj;
    }

    @Override
    public <T> T querySingleVOByCustomElementName(Class<T> formater,String elementId, Param param) throws DataAccessException {
        T obj;
        try {
            RedisSession redis = redisSessionFactory.getSession();
            //未获取缓存链接,执行数据库操作并返回
            if (redis == null) {
                obj = super.querySingleVOByCustomElementName(formater,elementId, param);
            } else {
                String resultKey = createDatabaseResultKey(null, elementId, param.get(), redis);
                String jsonResult = redis.getData(resultKey);
                if (StringUtils.isEmpty(jsonResult)) {
                    obj = super.querySingleVOByCustomElementName(formater,elementId, param);
                    if (obj == null) {
                        redis.delete(resultKey);
                    } else {
                        jsonResult = JSONUtils.toJson(obj);
                        redis.setData(resultKey, jsonResult);
                    }
                } else {
                    obj = JSONUtils.fromJson(jsonResult,formater);
                }
                logger.info("[根据databaseKey:{},获取值:{}]", resultKey, jsonResult);
            }
        } catch (RedisException e) {
            logger.error("querySingleVOByCustomElementName:RedisException:{}", e);
            obj = super.querySingleVOByCustomElementName(formater,elementId, param);
        } catch (Exception e) {
            logger.error("cacheDao访问异常:{}", e);
            throw new DataAccessException(e);
        }
        return obj;
    }

    @Override
    public <T> List<T> queryVOList(Class<T> clazz, Throwable able, Param param) throws DataAccessException {
        List<T> list;
        try {
            RedisSession redis = redisSessionFactory.getSession();
            //未获取缓存链接,执行数据库操作并返回
            if (redis == null) {
                list = super.queryVOList(clazz, able, param);
            } else {
                String resultKey = createDatabaseResultKey(able, null, param.get(), redis);
                String jsonResult = redis.getData(resultKey);
                if (StringUtils.isEmpty(jsonResult)) {
                    list = super.queryVOList(clazz, able, param);
                    if (list == null) {
                        redis.delete(resultKey);
                    } else {
                        jsonResult = JSONUtils.toJson(list, JSONUtils.DEFAULT_DATE_TIME_PATTERN);
                        redis.setData(resultKey, jsonResult);
                    }
                } else {
                    list = JSONUtils.fromListJson(jsonResult, clazz, false);
                }
                logger.info("[根据databaseKey:{},获取值:{}]", resultKey, jsonResult);
            }
        } catch (RedisException e) {
            logger.error("queryVOList:RedisException:{}", e);
            list = super.queryVOList(clazz, able, param);
        } catch (Exception e) {
            logger.error("cacheDao访问异常:{}", e);
            throw new DataAccessException(e);
        }
        return list;
    }

    @Override
    public <T> List<T> queryVOListByCustomElementName(Class<T> clazz, String elementId, Param param) throws DataAccessException {
        List<T> list;
        try {
            RedisSession redis = redisSessionFactory.getSession();
            //未获取缓存链接,执行数据库操作并返回
            if (redis == null) {
                list = super.queryVOListByCustomElementName(clazz, elementId, param);
            } else {
                String resultKey = createDatabaseResultKey(null, elementId, param.get(), redis);
                String jsonResult = redis.getData(resultKey);
                if (StringUtils.isEmpty(jsonResult)) {
                    list = super.queryVOListByCustomElementName(clazz, elementId, param);
                    if (list != null) {
                        jsonResult = JSONUtils.toJson(list, JSONUtils.DEFAULT_DATE_TIME_PATTERN);
                        redis.setData(resultKey, jsonResult);
                    }
                } else {
                    list = JSONUtils.fromListJson(jsonResult, clazz, false);
                }
                logger.info("[根据databaseKey:{},获取值:{}]", resultKey, jsonResult);
            }
        } catch (RedisException e) {
            logger.error("queryVOListByCustomElementName:RedisException:{}", e);
            list = super.queryVOListByCustomElementName(clazz, elementId, param);
        } catch (Exception e) {
            logger.error("cacheDao访问异常:{}", e);
            throw new DataAccessException(e);
        }
        return list;
    }

    /**
     * 更新table Key
     *
     * @param clazz
     */
    private <T> void updateTableKey(Class<T> clazz) {
        String tableKey = null;
        try {
            //表进行添加操作,需要让表KEY失效
            RedisSession redis = redisSessionFactory.getSession();
            if (redis != null) {
                //更新表Key
                String cacheTableKey = CacheKeyUtils.createTableKey(clazz);
                tableKey = UUIDProvider.uuid();
                redis.setData(cacheTableKey, tableKey);
                logger.info("更新表:{},key:{}", cacheTableKey, tableKey);
            }
        } catch (RedisException e) {
            //处理了RedisException,不向上抛
            logger.debug("更新表Key异常:{}", e);
        }
    }

    /**
     * 更新库级缓存
     *
     * @return
     */
    private void updateDatabaseKey() {
        String databaseKey = null;
        try {

            RedisSession redis = redisSessionFactory.getSession();
            if (redis != null) {
                //更新表Key
                databaseKey = UUIDProvider.uuid();
                redis.setData(Constants.SQLConstants.DATABASE_KEY, databaseKey);
                logger.info("更新库:{},key:{}", Constants.SQLConstants.DATABASE_KEY, databaseKey);
            }
        } catch (RedisException e) {
            //处理了RedisException,不向上抛
            logger.debug("更新表Key异常:{}", e);
        }
    }

    /**
     * 获取表缓存结果的KEY
     *
     * @param clazz 表po
     * @param map   查询参数
     * @param redis
     * @param <T>
     * @return
     */
    private <T> String createTableResultKey(Class<T> clazz, ESQL esql, Map<String, Object> map, RedisSession redis) {
        try {
            String sql = SQLUtil.getSql(getSession(), SQLCreator.set(clazz, esql).get(), map);
            String tableCacheKey = redis.getData(CacheKeyUtils.createTableKey(clazz));
            return CacheKeyUtils.createTableResultCacheKey(tableCacheKey, sql);
        } catch (RedisException e) {
            //处理了RedisException,不向上抛e
            logger.debug("更新表Key异常:{}", e);
        } catch (Exception e) {
            logger.error("创建表级结果key异常:{}", e);
            throw new DataAccessException(e);
        }
        return null;
    }

    /**
     * 创建库级缓存结果KEY
     *
     * @param able
     * @param sqlId
     * @param map
     * @param redis
     * @return
     */
    private String createDatabaseResultKey(Throwable able, String sqlId, Map<String, Object> map, RedisSession redis) {
        try {
            if (able == null && StringUtils.isEmpty(sqlId))
                return null;
            if (able != null) {
                String sql = SQLUtil.getSql(getSession(), SQLCreator.set(Constants.SQLConstants.CUSTOMER_SQL, able).get(), map);
                String databaseCacheKey = redis.getData(Constants.SQLConstants.DATABASE_KEY);
                return CacheKeyUtils.createDatabaseResultCacheKey(databaseCacheKey, sql);
            } else if (!StringUtils.isEmpty(sqlId)) {
                String sql = SQLUtil.getSql(getSession(), sqlId, map);
                String databaseCacheKey = redis.getData(Constants.SQLConstants.DATABASE_KEY);
                return CacheKeyUtils.createDatabaseResultCacheKey(databaseCacheKey, sql);
            }
        } catch (RedisException e) {
            //处理了RedisException,不向上抛e
            logger.debug("更新表Key异常:{}", e);
        }
        return null;
    }

}
