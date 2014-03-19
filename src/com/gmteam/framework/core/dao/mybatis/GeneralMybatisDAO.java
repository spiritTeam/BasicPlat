package com.gmteam.framework.core.dao.mybatis;

import java.util.List;
import com.gmteam.framework.core.dao.GeneralDao;
import com.gmteam.framework.model.Page;

/**
 * mybatis形式DAO的接口，继承自GeneralDao<T><br/>
 * 定义基于mybatis的数据访问层的全方法
 * @author zhuhua，wh
 * @param <T> T是Dao对应的entity对象
 */
public interface GeneralMybatisDAO<T> extends GeneralDao<T> {
    /**
     * 定义mybatis sqlmap的namespace
     * 每一个dao的实例都应该指定一个namespace
     * @param namespace
     */
    public void setNamespace(String namespace);

    /**
     * 插入一个新对象，返回插入记录的数，若返回0，则插入失败
     * @param statementId sqlmap中插入记录的语句的id
     * @param newData 新对象
     * @return 插入记录的条数,若返回0，则插入失败
     * @throws Exception
     */
    public int insert(String statementId, Object newData) throws Exception;

    /**
     * 更新记录
     * @param statementId sqlmap中更新记录的语句的id
     * @param updateData 更新对象，此对象包括唯一的字段组合（或ID），其他对应对象的值跟新为这个对象中的对应值
     * @return 更新记录的条数
     */
    public int update(String statementId, Object updateData) throws Exception;

    /**  
     * 删除记录
     * @param statementId sqlmap中删除记录的语句的id
     * @param parameter 欲删除对象的标识，在关系数据库中，记录的各列的值与这个对象中的相应值相等的记录将被删除
     * @return 删除记录的条数
     */
    public int delete(String statementId, Object parameter) throws Exception;
    
    /**
     * 得到默认的查询结果总数
     * @return 结果总数
     */
    public int getCount()throws Exception;

    /**
     * 得到查询总数
     * @param statementId sqlmap中对应的语句的id
     * @param parameter 查询条件对象
     * @return 结果总数
     */
    public int getCount(String statementId, Object parameter) throws Exception;

    /**
     * 查询出一条记录
     * @param statementId sql语句标识
     * @param idObj 查询条件对象，应该为ID
     * @return 返回符合查询条件的一条记录，结果为T对象
     */
    public T getInfoObject(String statementId, Object idObj) throws Exception;

    /**
     * 查询出一条记录，若statementId为空，
     * @param statementId sql语句标识
     * @param parameter 查询条件对象
     * @return 返回符合查询条件的一条记录，结果为自由对象
     */
    public <V> V queryForObjectAutoTranform(String statementId, Object parameter) throws Exception;
    
    /**
     * 以列表的方式返回默认的查询结果
     * @return 以列表形式返回的查询结果
     */
    public List<T> queryForList()throws Exception;

    /**
     * 以列表的方式返回查询结果
     * @param statementId sql语句标识
     * @return 以列表形式返回的查询结果
     */
    public List<T> queryForList(String statementId) throws Exception;

    /**
     * 以列表的方式返回查询结果
     * @param statementId sql语句标识
     * @param parameter 查询条件对象
     * @return 以列表形式返回的查询结果
     */
    public List<T> queryForList(String statementId, Object parameter) throws Exception;

    /**
     * 指定statementId的列表查询(不受泛型类约束)
     * @param <V> 自由形式
     * @param statementId sql语句标识
     * @param parameter 查询条件对象
     * @return 以列表形式返回的查询结果，结果为自由对象
     */
    public <V> List<V> queryForListAutoTranform(String statementId, Object parameter) throws Exception;

    /**
     * 指定statement的分页查询，本方法明确给出计算总数的Sql语句，速度可更快
     * @param countSqlId 计算总数的sql语句标识
     * @param pageQuerySqlId sql语句标识，用于查询数据
     * @param parameter 查询条件对象
     * @param pageIndex 返回的页码
     * @param pageSize 每页结果条数
     * @return 返回页对象，页对象中的记录为T
     */
    public Page<T> pageQuery(String countSqlId, String pageQuerySqlId, Object parameter, int pageIndex, int pageSize) throws Exception;

    /**
     * 指定statement的分页查询，本方法未给出计算总数的Sql语句，总数从pageQuerySqlId计算
     * @param pageQuerySqlId sql语句标识，用于查询数据
     * @param parameter 查询条件对象
     * @param pageIndex 返回的页码
     * @param pageSize 每页结果条数
     * @return 返回页对象，页对象中的记录为T
     */
    public Page<T> pageQuery(String pageQuerySqlId, Object parameter, int pageIndex, int pageSize) throws Exception;

    /**
     * 默认分页查询
     * @param parameter 查询条件对象
     * @param pageIndex 返回的页码
     * @param pageSize 每页结果条数
     * @return 返回页对象，页对象中的记录为T
     * @return 返回页对象，页对象中的记录为自由对象
     */
    public Page<T> pageQuery(Object parameter, int pageIndex, int pageSize) throws Exception;

    /**
     * 指定statement的分页查询，若countSqlId为空，则总数从pageQuerySqlId计算
     * @param pageQuerySqlId sql语句标识，用于查询数据
     * @param parameter 查询条件对象
     * @param pageIndex 返回的页码
     * @param pageSize 每页结果条数
     * @return 返回页对象，页对象中的记录为自由对象
     */
    public <V> Page<V> pageQueryAutoTranform(String countSqlId, String pageQuerySqlId, Object parameter, int pageIndex, int pageSize) throws Exception;

    /**
     * 开始事务
     * @throws Exception
     */
    public void startTransaction() throws Exception;

    /**
     * 提交事务
     * @throws Exception
     */
    public void commitTransaction() throws Exception;

    /**
     * 结束事务
     * @throws Exception
     */
    public void endTransaction() throws Exception;
}