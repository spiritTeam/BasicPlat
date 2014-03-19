package com.gmteam.framework.core.dao;

import java.util.List;

/**
 * 平台DAO通用接口,无论是使用hibernate、ibatis、jdbc、springjdbcTemplate建立数据访问对象层都需要实现此接口
 * @author zhuhua
 * @param <T> T是Dao对应的entity对象
 */
public interface GeneralDao<T>  {
    /**
     * 插入一个新对象，返回插入记录的数，若返回0，则插入失败
     * @param newData 新增的数据
     * @return 若插入成功返回1，
     */
    public int insert(Object newData) throws Exception;

    /**
     * 存储对象，若对象不存在insert，若对象存在update，判断标准是是否能够插入成功，注意要有主键
     * @param aData 新增的数据
     * @return 存储记录数
     */
    public int save(Object aData) throws Exception;

    /**
     * 更新记录
     * @param updateData 更新对象，此对象包括唯一的字段组合（或ID），其他对应对象的值跟新为这个对象中的对应值
     * @return 更新记录条数
     */
    public int update(Object updateData) throws Exception;

    /**
     * 删除记录
     * @param parameter 欲删除对象的标识，在关系数据库中，记录的各列的值与这个对象中的相应值相等的记录将被删除
     * @return 删除记录条数
     */
    public int delete(Object parameter) throws Exception;

    /**
     * 得到查询总数
     * @param parameter 查询条件对象
     * @return 结果总数
     */
    public int getCount(Object parameter) throws Exception;

    /**
     * 根据条件查询出一条记录
     * @param idObj 查询条件对象，应该为ID
     * @return 返回符合查询条件的一条记录，结果为T对象
     */
    public T getInfoObject(Object idObj) throws Exception;

    /**
     * 以列表的方式返回查询结果
     * @param parameter 查询条件对象
     * @return 返回符合查询条件的列表，结果为T对象列表
     */
    public List<T> queryForList(Object parameter) throws Exception;
}