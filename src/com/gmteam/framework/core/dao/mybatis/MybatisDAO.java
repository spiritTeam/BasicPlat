package com.gmteam.framework.core.dao.mybatis;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import com.gmteam.framework.ext.mybatis.PageBounds;
import com.gmteam.framework.ext.mybatis.PageList;
import com.gmteam.framework.model.BaseObject;
import com.gmteam.framework.model.Page;

public class MybatisDAO <T extends BaseObject> extends SqlSessionDaoSupport implements GeneralMybatisDAO<T> {

    private SqlSession sqlSession;
    //命名空间
    private String namespace;

    @Override
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
    //平台默认的mybatis调用ID
    private String insertkey="insert";
    private String updatekey="update";
    private String deletekey="delete";
    private String infokey="getInfo";
    private String listkey="getList";
    private String countkey="getCount";

    /**
     * 初始化
     * @throws Exception
     */
    public void initialize() throws Exception {
        this.sqlSession = this.getSqlSession();
    }

    @Override
    public int insert(Object newData) throws Exception {
        java.math.BigDecimal b = new java.math.BigDecimal("ABC");

        return insert(insertkey, newData);
    }

    @Override
    public int insert(String statementId, Object newData) throws Exception {
        String key=namespace+"."+statementId;
        if (sqlSession ==null) this.sqlSession = this.getSqlSession();
        this.sqlSession.insert(key, newData);
        return 1;
    }

    @Override
    public int save(Object aData) throws Exception {
        int a = 0;
        try {
            a = insert(insertkey, aData);
        } catch(Exception e) {
        }
        if (a!=1) return update(updatekey, aData);
        else return -1;
    }

    @Override
    public int update(Object updateData) throws Exception {
        return this.update(updatekey, updateData);
    }

    @Override
    public int update(String statementId, Object updateData) throws Exception {
        String key=namespace+"."+statementId;
        if (sqlSession ==null) this.sqlSession = this.getSqlSession();
        return this.sqlSession.update(key, updateData);
    }

    @Override
    public int delete(Object parameter) throws Exception {
        return this.delete(deletekey, parameter);
    }

    @Override
    public int delete(String statementId, Object parameter) throws Exception {
        String key=namespace+"."+statementId;
        if (sqlSession ==null) this.sqlSession = this.getSqlSession();
        return this.sqlSession.delete(key, parameter);
    }

    @Override
    public int getCount() throws Exception {
        return this.getCount(countkey, null);
    }

    @Override
    public int getCount(Object parameter) throws Exception {
        return this.getCount(countkey, parameter);
    }

    @Override
    public int getCount(String statementId, Object parameter) throws Exception {
        String key=namespace+"."+statementId;
        if (sqlSession ==null) this.sqlSession = this.getSqlSession();
        return Integer.parseInt(String.valueOf(this.sqlSession.selectOne(key, parameter) ));
    }

    @Override
    public T getInfoObject(Object idObj) throws Exception {
        return (T)this.getInfoObject(infokey, idObj);
    }

    @Override
    public T getInfoObject(String statementId, Object idObj) throws Exception {
        String key=namespace+"."+statementId;
        if (sqlSession ==null) this.sqlSession = this.getSqlSession();
        return (T)this.sqlSession.selectOne(key, idObj);
    }

    @Override
    public List<T> queryForList() throws Exception {
        return this.queryForList(listkey, null);
    }

    @Override
    public List<T> queryForList(Object parameter) throws Exception {
        return this.queryForList(listkey, parameter);
    }

    @Override
    public List<T> queryForList(String statementId) throws Exception {
        return this.queryForList(statementId, null);
    }

    @Override
    public List<T> queryForList(String statementId, Object parameter) {
        String key=namespace+"."+statementId;
        if (sqlSession ==null) this.sqlSession = this.getSqlSession();
        return this.sqlSession.selectList(key, parameter);
    }

    @Override
    public Page<T> pageQuery(Object parameter, int pageIndex, int pageSize) throws Exception {
        return this.pageQuery(null, this.listkey, parameter, pageIndex, pageSize);
    }

    @Override
    public Page<T> pageQuery(String pageQuerySqlId, Object parameter, int pageIndex, int pageSize) throws Exception {
        return this.pageQuery(null, pageQuerySqlId, parameter, pageIndex, pageSize);
    }

    @Override
    public Page<T> pageQuery(String countSqlId, String pageQuerySqlId, Object parameter, int pageIndex, int pageSize) throws Exception {
        if (pageQuerySqlId==null) pageQuerySqlId=namespace+"."+this.listkey;
        PageBounds pageBounds = new PageBounds(pageIndex, pageSize);
        pageBounds.setContainsTotalCount(false);
        //计算总数
        Integer totalCount=null;
        if (countSqlId!=null) totalCount=getCount(countSqlId, parameter);
        else pageBounds.setContainsTotalCount(true);

        List<T> l = this.getSqlSession().selectList(pageQuerySqlId, parameter, pageBounds);
        if (countSqlId==null)  {
            PageList<T> pageList = (PageList<T>)l;
            totalCount = pageList.getPaginator().getTotalCount(); //得到结果条数
        }
        Page<T> rp = new Page<T>(totalCount, pageSize, pageIndex, l);
        return rp;
    }

    @Override
    public <V> V queryForObjectAutoTranform(String statementId, Object parameterObject) throws Exception {
        String key=namespace+"."+statementId;
        if (sqlSession ==null) this.sqlSession = this.getSqlSession();
        return this.sqlSession.selectOne(key, parameterObject);
    }

    @Override
    public <V> List<V> queryForListAutoTranform(String statementId, Object parameter) throws Exception {
        String key=namespace+"."+statementId;
        if (sqlSession ==null) this.sqlSession = this.getSqlSession();
        return this.sqlSession.selectList(key, parameter);
    }

    @Override
    public <V> Page<V> pageQueryAutoTranform(String countSqlId, String pageQuerySqlId, Object parameter, int pageIndex, int pageSize) throws Exception {
        if (pageQuerySqlId==null) pageQuerySqlId=namespace+"."+this.listkey;
        PageBounds pageBounds = new PageBounds(pageIndex, pageSize);
        pageBounds.setContainsTotalCount(false);
        //计算总数
        Integer totalCount=null;
        if (countSqlId!=null) totalCount=getCount(countSqlId, parameter);
        else pageBounds.setContainsTotalCount(true);

        List<V> l = this.getSqlSession().selectList(pageQuerySqlId, parameter, pageBounds);
        if (countSqlId==null)  {
            PageList<V> pageList = (PageList<V>)l;
            totalCount = pageList.getPaginator().getTotalCount(); //得到结果条数
        }
        Page<V> rp = new Page<V>(totalCount, pageSize, pageIndex, l);
        return rp;
    }

    @Override
    //目前还不知道怎样处理
    public void startTransaction() throws Exception {
        if (this.sqlSession!=null) {
        }
    }

    @Override
    public void commitTransaction() throws Exception {
        if (this.sqlSession!=null) {
            this.sqlSession.commit();
        }
    }

    @Override
    public void endTransaction() throws Exception {
        if (this.sqlSession!=null) {
            this.sqlSession.rollback();
        }
    }
}