package com.zhangk.datasource.matadata.session;

import com.zhangk.datasource.matadata.param.CustomSQL;
import com.zhangk.datasource.matadata.param.Param;
import org.apache.ibatis.session.SqlSession;
import com.zhangk.datasource.exception.DataAccessException;

import java.util.List;

/**
 * Created by zhangkui on 16/4/27.
 */
public interface DataSession {

    /**
     * 获得默认数据库session
     * @param:
     * @return: SqlSession
     * @throws
     */

    SqlSession getSession();

    /**
     * 保存至数据库
     * @param po
     * @return
     * @throws DataAccessException
     * T
     *
     */
    <T> T save(Class<T> clazz, T po) throws DataAccessException;


    /**
     * 更新数据库对象
     * @param clazz
     * @param po
     * @return
     * @throws DataAccessException
     * int
     *
     */
    <T> int update(Class<T> clazz, T po) throws DataAccessException;

    /**
     * 根据whereSql 更新固定列信息
     * @param clazz
     * @param param
     * @param whereSql
     * @return
     * @throws DataAccessException
     * int
     *
     */
    <T> int updateCustomColumnByWhere(Class<T> clazz, Param param, CustomSQL whereSql) throws DataAccessException;

    /**
     * 逻辑删除数据库对象，设置disabled=1
     * @param clazz
     * @param param
     * @return
     * @throws DataAccessException
     * int
     *
     */

    <T> int logicDelete(Class<T> clazz, Param param) throws DataAccessException;

    /**
     * 根据条件
     * @param clazz
     * @param whereSql
     * @return
     * @throws DataAccessException
     * int
     *
     */
    <T> int logicWhereDelete(Class<T> clazz, CustomSQL whereSql) throws DataAccessException;

    /**
     * 物理删除数据库对象，执行delete SQL语句
     * @param clazz
     * @param id
     * @return
     * @throws DataAccessException
     * int
     *
     */
    <T> int physicalDelete(Class<T> clazz, Integer id) throws DataAccessException;

    /**
     * 执行带where条件的物理删除
     * @param clazz
     * @param whereSql
     * @return
     * @throws DataAccessException
     * int
     *
     */
    <T> int physicalWhereDelete(Class<T> clazz, CustomSQL whereSql) throws DataAccessException;

    /**
     * 根据对象ID查询数据库对象
     * @param id
     * @return
     * @throws DataAccessException
     * T
     *
     */
    <T> T querySingleResultById(Class<T> clazz, int id) throws DataAccessException;

    /**
     * 根据UUID查询数据库对象
     * @param uuid
     * @return
     * @throws DataAccessException
     * T
     *
     */
    <T> T querySingleResultByUUID(Class<T> clazz, String uuid) throws DataAccessException;

    /**
     * 根据查询条件，查询单个数据库对象
     * @param param
     * @return
     * @throws DataAccessException
     * T
     *
     */
    <T> T querySingleResultByParams(Class<T> clazz, Param param) throws DataAccessException;

    /**
     * 根据查询条件查询数据库对象列表
     * @param param
     * @return
     * @throws DataAccessException
     * List<T>
     *
     */
    <T> List<T> queryListResult(Class<T> clazz, Param param) throws DataAccessException;

    /**
     * 根据where条件查询数据
     * @param clazz
     * @param whereSQL
     * @return
     * @throws DataAccessException
     * List<T>
     *
     */
    <T> List<T> queryListResultByWhere(Class<T> clazz, CustomSQL whereSQL) throws DataAccessException;

    /**
     * 根据查询条件查询对象数量
     * @param param
     * @return
     * @throws DataAccessException
     * long
     *
     */
    <T> long queryListResultCount(Class<T> clazz, Param param) throws DataAccessException;

    /**
     * 根据where条件查询数据库条数
     * @param clazz
     * @param whereSQL
     * @return
     * long
     *
     */
    <T> long queryListResultCountByWhere(Class<T> clazz, CustomSQL whereSQL);


    /**
     * Mapper模式查询，返回单一对象，可以提供VO查询
     * @param able
     * @param param
     * @param formater
     * @return
     * S
     *
     */
    <T> T querySingleVO(Class<T> formater, Throwable able, Param param) throws DataAccessException;

    /**
     * Mapper模式查询，返回单一对象，可以提供VO查询,可根据mapper中自定义的元素的ID操作
     * @param elementId
     * @param formater
     * @param param
     * @return
     * @throws DataAccessException
     * S
     *
     */
    <T> T querySingleVOByCustomElementName(Class<T> formater, String elementId, Param param)
            throws DataAccessException;


    /**
     * Mapper模式查询，返回列表对象，可以提供VO查询
     * @param clazz 类型转换类类型
     * @param able
     * @param param
     * @param <T>
     * @return
     * @throws DataAccessException
     */
    <T> List<T> queryVOList(Class<T> clazz, Throwable able, Param param) throws DataAccessException;

    /**
     *  Mapper模式查询，返回列表对象，可以提供VO查询,可根据mapper中自定义的元素的ID操作
     * @param clazz 类型转换类类型
     * @param elementId
     * @param param
     * @return
     * @throws DataAccessException
     * List<S>
     *
     */
    <T> List<T> queryVOListByCustomElementName(Class<T> clazz, String elementId, Param param)
            throws DataAccessException;

    /**
     * 批量插入数据
     * @param clazz
     * @param able
     * @param list
     * @param <T>
     * @throws DataAccessException
     */
    <T> void insertBatch(Class<T> clazz, Throwable able, List list) throws DataAccessException ;
}
