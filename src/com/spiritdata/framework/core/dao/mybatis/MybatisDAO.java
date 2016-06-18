package com.spiritdata.framework.core.dao.mybatis;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.spiritdata.framework.exceptionC.Plat0101CException;
import com.spiritdata.framework.ext.mybatis.PageBounds;
import com.spiritdata.framework.ext.mybatis.PageList;
import com.spiritdata.framework.util.JsonUtils;
import com.spiritdata.framework.core.model.BaseObject;
import com.spiritdata.framework.core.model.Page;

public class MybatisDAO <T extends BaseObject> extends SqlSessionDaoSupport implements GeneralMybatisDAO<T> {

    private SqlSession sqlSession;
    //命名空间
    private String namespace;

    @Override
    public void setNamespace(String namespace) {
        this.namespace=namespace;
    }
    //平台默认的mybatis调用ID
    private String insertkey="insert";
    private String updatekey="update";
    private String deletekey="delete";
    private String infokey="getInfo";
    private String listkey="getList";
    private String countkey="getCount";
    private String executekey="execute";

    /**
     * 初始化
     * @throws Exception
     */
    public void initialize() {
        this.sqlSession=this.getSqlSession();
    }

    @Override
    public int insert(Object newData) {
        return insert(insertkey, newData);
    }

    @Override
    public int insert(String statementId, Object newData) {
        String key=namespace+"."+statementId;
        try {
            if (sqlSession==null) this.sqlSession=this.getSqlSession();
            this.sqlSession.insert(key, newData);
            return 1;
        } catch(Exception e) {
            throw new Plat0101CException("新增数据异常:采用SQL="+key+",数据["+newData.getClass().getName()+"(@"+newData.hashCode()+")]", e);
        }
    }

    @Override
    public int save(Object aData) {
        String exceptionMsg="";
        Exception ei=null;
        int a=0;
        try {
            a=insert(insertkey, aData);
        } catch(Exception e) {
            exceptionMsg=e.getMessage();
            ei=e;
        }
        try {
            if (a!=1) return update(updatekey, aData);
            else return -1;
        } catch(Exception e) {
            throw new Plat0101CException("保存数据问题：\n\t先新增数据，出现如下问题："+exceptionMsg+"\n\t后更新数据，出现如下问题："+e.getMessage(), ei==null?e:e.initCause(ei));
        }
    }

    @Override
    public int update(Object updateData) {
        return this.update(updatekey, updateData);
    }

    @Override
    public int update(String statementId, Object updateData) {
        String key=namespace+"."+statementId;
        try {
            if (sqlSession ==null) this.sqlSession=this.getSqlSession();
            return this.sqlSession.update(key, updateData);
        } catch(Exception e) {
            throw new Plat0101CException("更新数据异常:采用SQL="+key+",数据["+updateData.getClass().getName()+"(@"+updateData.hashCode()+")]", e);
        }
    }

    @Override
    public int delete(Object parameter) {
        return this.delete(deletekey, parameter);
    }

    @Override
    public int delete(String statementId, Object parameter) {
        String key=namespace+"."+statementId;
        try {
            if (sqlSession ==null) this.sqlSession=this.getSqlSession();
            return this.sqlSession.delete(key, parameter);
        } catch(Exception e) {
            throw new Plat0101CException("删除数据异常:采用SQL="+key+",数据["+parameter.getClass().getName()+"(@"+parameter.hashCode()+")]", e);
        }
    }

    @Override
    public int getCount() {
        return this.getCount(countkey, null);
    }

    @Override
    public int getCount(Object parameter) {
        return this.getCount(countkey, parameter);
    }

    @Override
    public int getCount(String statementId, Object parameter) {
        String key=namespace+"."+statementId;
        try {
          if (sqlSession ==null) this.sqlSession=this.getSqlSession();
          return this.sqlSession.selectOne(key, parameter);
        } catch(Exception e) {
            throw new Plat0101CException("得到数据记录条数异常:采用SQL="+key+",数据["+parameter.getClass().getName()+"(@"+parameter.hashCode()+")]", e);
        }
    }

    @Override
    public T getInfoObject(Object idObj) {
        return (T)this.getInfoObject(infokey, idObj);
    }

    @Override
    public T getInfoObject(String statementId, Object idObj) {
        String key=namespace+"."+statementId;
        try {
            if (sqlSession ==null) this.sqlSession=this.getSqlSession();
            return (T)this.sqlSession.selectOne(key, idObj);
        } catch(Exception e) {
            throw new Plat0101CException("得到单条(范型)信息异常:采用SQL="+key+",数据["+idObj.getClass().getName()+"(@"+idObj.hashCode()+")]=["+JsonUtils.objToJson(idObj)+"]", e);
        }
    }

    @Override
    public List<T> queryForList() {
        return this.queryForList(listkey, null);
    }

    @Override
    public List<T> queryForList(Object parameter) {
        return this.queryForList(listkey, parameter);
    }

    @Override
    public List<T> queryForList(String statementId) {
        return this.queryForList(statementId, null);
    }

    @Override
    public List<T> queryForList(String statementId, Object parameter) {
        String key=namespace+"."+statementId;
        try {
            if (sqlSession ==null) this.sqlSession=this.getSqlSession();
            return this.sqlSession.selectList(key, parameter);
        } catch(Exception e) {
            throw new Plat0101CException("得到(范型)信息列表异常:采用SQL="+key+",数据["+parameter.getClass().getName()+"(@"+parameter.hashCode()+")]", e);
        }
    }

    @Override
    public Page<T> pageQuery(Object parameter, int pageIndex, int pageSize) {
        return this.pageQuery(null, null, parameter, pageIndex, pageSize);
    }

    @Override
    public Page<T> pageQuery(String pageQuerySqlId, Object parameter, int pageIndex, int pageSize) {
        return this.pageQuery(null, pageQuerySqlId, parameter, pageIndex, pageSize);
    }

    @Override
    public Page<T> pageQuery(String countSqlId, String pageQuerySqlId, Object parameter, int pageIndex, int pageSize) {
        if (pageQuerySqlId==null) pageQuerySqlId=namespace+"."+this.listkey;
        try {
            PageBounds pageBounds=new PageBounds(pageIndex, pageSize);
            pageBounds.setContainsTotalCount(false);
            //计算总数
            Integer totalCount=null;
            if (countSqlId!=null) totalCount=getCount(countSqlId, parameter);
            else pageBounds.setContainsTotalCount(true);

            List<T> l=this.getSqlSession().selectList(pageQuerySqlId, parameter, pageBounds);
            if (countSqlId==null)  {
                PageList<T> pageList=(PageList<T>)l;
                totalCount=pageList.getPaginator().getTotalCount(); //得到结果条数
            }
            Page<T> rp=new Page<T>(totalCount, pageSize, pageIndex, l);
            return rp;
        } catch(Exception e) {
            throw new Plat0101CException("得到(范型)信息列表分页结果异常:采用SQL="+pageQuerySqlId
                    +",数据["+parameter.getClass().getName()+"(@"+parameter.hashCode()+")]"
                    +",分页参数[第("+pageIndex+")页,每页("+pageSize+")条记录]", e);
        }
    }

    @Override
    public <V> V queryForObjectAutoTranform(String statementId, Object parameter) {
        String key=namespace+"."+statementId;
        try {
            if (sqlSession ==null) this.sqlSession=this.getSqlSession();
            return (V)this.sqlSession.selectOne(key, parameter);
        } catch(Exception e) {
            throw new Plat0101CException("得到单条(自由)信息异常:采用SQL="+key+",数据["+parameter.getClass().getName()+"(@"+parameter.hashCode()+")]", e);
        }
    }

    @Override
    public <V> List<V> queryForListAutoTranform(String statementId, Object parameter) {
        String key=namespace+"."+statementId;
        try {
            if (sqlSession ==null) this.sqlSession=this.getSqlSession();
            return this.sqlSession.selectList(key, parameter);
        } catch(Exception e) {
            throw new Plat0101CException("得到(自由)信息列表异常:采用SQL="+key+",数据["+parameter.getClass().getName()+"(@"+parameter.hashCode()+")]", e);
        }
    }

    @Override
    public <V> Page<V> pageQueryAutoTranform(String countSqlId, String pageQuerySqlId, Object parameter, int pageIndex, int pageSize) {
        if (pageQuerySqlId==null) pageQuerySqlId=namespace+"."+this.listkey;
        try {
            PageBounds pageBounds=new PageBounds(pageIndex, pageSize);
            pageBounds.setContainsTotalCount(false);
            //计算总数
            Integer totalCount=null;
            if (countSqlId!=null) totalCount=getCount(countSqlId, parameter);
            else pageBounds.setContainsTotalCount(true);

            List<V> l=this.getSqlSession().selectList(pageQuerySqlId, parameter, pageBounds);
            if (countSqlId==null)  {
                PageList<V> pageList=(PageList<V>)l;
                totalCount=pageList.getPaginator().getTotalCount(); //得到结果条数
            }
            Page<V> rp=new Page<V>(totalCount, pageSize, pageIndex, l);
            return rp;
        } catch(Exception e) {
            throw new Plat0101CException("得到(自由)信息列表分页结果异常:采用SQL="+pageQuerySqlId
                    +",数据["+parameter.getClass().getName()+"(@"+parameter.hashCode()+")]"
                    +",分页参数[第("+pageIndex+")页,每页("+pageSize+")条记录]", e);
        }
    }

    @Override
    //目前还不知道怎样处理
    public void startTransaction() {
        try {
            if (this.sqlSession!=null) {
            }
        } catch(Exception e) {
            throw new Plat0101CException("开始事务处理", e);
        }
    }

    @Override
    public void commitTransaction() {
        try {
            if (this.sqlSession!=null) {
                this.sqlSession.commit();
            }
        } catch(Exception e) {
            throw new Plat0101CException("提交事务", e);
        }
    }

    @Override
    public void endTransaction() {
        try {
            if (this.sqlSession!=null) {
                this.sqlSession.rollback();
            }
        } catch(Exception e) {
            throw new Plat0101CException("结束事务", e);
        }
    }

    @Override
    public void execute(Object parameter) {
        execute(executekey, parameter);
    }

    @Override
    public void execute(String executeSqlId, Object parameter) {
        String key=namespace+"."+executeSqlId;
        try {
            if (sqlSession ==null) this.sqlSession=this.getSqlSession();
            this.sqlSession.update(key, parameter);
        } catch(Exception e) {
            throw new Plat0101CException("执行Sql异常:采用SQL="+key+",数据["+parameter.getClass().getName()+"(@"+parameter.hashCode()+")]", e);
        }
    }
}