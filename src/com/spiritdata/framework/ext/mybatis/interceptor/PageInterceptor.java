package com.spiritdata.framework.ext.mybatis.interceptor;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import com.spiritdata.framework.core.dao.DatabaseType;
import com.spiritdata.framework.core.dao.dialect.*;
import com.spiritdata.framework.ext.mybatis.PageBounds;
import com.spiritdata.framework.ext.mybatis.PageList;
import com.spiritdata.framework.ext.mybatis.Paginator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.*;

import javax.sql.DataSource;

/**
 * 为MyBatis提供基于数据库类型的分页查询的插件
 * 
 * 将拦截Executor.query()方法实现分页方言的插入.
 * @author wh
 */

@Intercepts({
  @Signature(type=Executor.class,method="query",args={MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
 ,@Signature(type=Executor.class,method="query",args={MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
})
public class PageInterceptor implements Interceptor{
    private static final Log logger = LogFactory.getLog(PageInterceptor.class);

    static int MAPPED_STATEMENT_INDEX = 0;
    static int PARAMETER_INDEX = 1;
    static int ROWBOUNDS_INDEX = 2;
    static int RESULT_HANDLER_INDEX = 3;

    private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
    
    private DatabaseType databaseType;//数据库类型，不同的数据库有不同的分页方法
    private Dialect dialect;

    static ExecutorService Pool;
    boolean asyncTotalCount = false;
    
    public Object intercept(final Invocation invocation) throws Throwable {
        final Executor executor = (Executor) invocation.getTarget();
        final Object[] queryArgs = invocation.getArgs();
        final MappedStatement ms = (MappedStatement)queryArgs[MAPPED_STATEMENT_INDEX];
        final Object parameter = queryArgs[PARAMETER_INDEX];
        final RowBounds rowBounds = (RowBounds)queryArgs[ROWBOUNDS_INDEX];
        final PageBounds pageBounds = new PageBounds(rowBounds);

        final int offset = pageBounds.getOffset();
        final int limit = pageBounds.getLimit();
        final int page = pageBounds.getPage();

        final BoundSql boundSql = ms.getBoundSql(parameter);
        final StringBuffer bufferSql = new StringBuffer(trimSql(boundSql.getSql()));

        String sql = bufferSql.toString();

        Callable<Paginator> countTask = null;

        if (rowBounds!=null && (offset!=RowBounds.NO_ROW_OFFSET || limit!=RowBounds.NO_ROW_LIMIT)) {
            if(pageBounds.isContainsTotalCount()){
                countTask = new Callable<Paginator>() {
                    public Paginator call() throws Exception {
                        Integer count = null;
                        MetaObject mo = MetaObject.forObject(invocation.getTarget(), DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, null);
                        DataSource ds = (DataSource)mo.getValue("delegate.configuration.environment.dataSource");
                        Connection conn = null;//ds.getConnection();
                        try {
                            conn = ms.getConfiguration().getEnvironment().getDataSource().getConnection();
                        } catch(Exception e) { }
                        if (conn==null) conn=ds.getConnection();
                        Cache cache = ms.getCache();
                        if(cache != null && ms.isUseCache()){
                            CacheKey cacheKey = executor.createCacheKey(ms,parameter,new PageBounds(),copyFromBoundSql(ms,boundSql,bufferSql.toString()));
                            count = (Integer)cache.getObject(cacheKey);
                            if (count==null) {
                                count = PageInterceptor.getCount(bufferSql.toString(),conn,ms,parameter,boundSql,dialect);
                                cache.putObject(cacheKey, count);
                            }
                        } else count = PageInterceptor.getCount(bufferSql.toString(),conn,ms,parameter,boundSql,dialect);

                        return new Paginator(page, limit, count);
                    }
                };
            }

            sql = dialect.getLimitString(sql, offset, limit);
            queryArgs[ROWBOUNDS_INDEX] = new RowBounds(RowBounds.NO_ROW_OFFSET,RowBounds.NO_ROW_LIMIT);
        }
        queryArgs[MAPPED_STATEMENT_INDEX] = copyFromNewSql(ms,boundSql,sql);

        Boolean async = pageBounds.getAsyncTotalCount() == null ? asyncTotalCount : pageBounds.getAsyncTotalCount();
        Future<Collection<Object>> listFuture = call(new Callable<Collection<Object>>() {
            public Collection<Object> call() throws Exception {
                return (Collection<Object>)invocation.proceed();
            }
        }, async);

        if (countTask!=null) {
            Future<Paginator> countFutrue = call(countTask, async);
            return new PageList<Object>((Collection<Object>)listFuture.get(), countFutrue.get());
        }

        return listFuture.get();
    }

    private <T> Future<T> call(Callable<T> callable, boolean async) {
        if (async) return (Future<T>)Pool.submit(callable);
        else {
            FutureTask<T> future = new FutureTask<T>(callable);
            future.run();
            return future;
        }
    }

    private MappedStatement copyFromNewSql(MappedStatement ms, BoundSql boundSql, String sql){
        BoundSql newBoundSql = copyFromBoundSql(ms, boundSql, sql);
        return copyFromMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
    }

    private BoundSql copyFromBoundSql(MappedStatement ms, BoundSql boundSql, String sql) {
        BoundSql newBoundSql = new BoundSql(ms.getConfiguration(),sql, boundSql.getParameterMappings(), boundSql.getParameterObject());
        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            String prop = mapping.getProperty();
            if (boundSql.hasAdditionalParameter(prop)) {
                newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
            }
        }
        return newBoundSql;
    }

    private MappedStatement copyFromMappedStatement(MappedStatement ms,SqlSource newSqlSource) {
        Builder builder = new Builder(ms.getConfiguration(),ms.getId(),newSqlSource,ms.getSqlCommandType());
        
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if(ms.getKeyProperties() != null && ms.getKeyProperties().length !=0){
            StringBuffer keyProperties = new StringBuffer();
            for(String keyProperty : ms.getKeyProperties()){
                keyProperties.append(keyProperty).append(",");
            }
            keyProperties.delete(keyProperties.length()-1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }
        
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        
        return builder.build();
    }

    private String trimSql(String sql) {
        sql = sql.trim();
        if (sql.endsWith(";")) sql=sql.substring(0, sql.length() - 1);
        return sql;
    }
    
    public static class BoundSqlSqlSource implements SqlSource {
        BoundSql boundSql;
        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }

    public void setDialect(Dialect dialect) {
        logger.debug("dialectClass: {"+dialect.getClass().getName()+"} ");
        this.dialect = dialect;
    }

    public void setAsyncTotalCount(boolean asyncTotalCount) {
        logger.debug("asyncTotalCount: {"+asyncTotalCount+"} ");
        this.asyncTotalCount = asyncTotalCount;
    }

    public void setPoolMaxSize(int poolMaxSize) {
        if (poolMaxSize>0) {
            logger.debug("poolMaxSize: {"+poolMaxSize+"} ");
            Pool = Executors.newFixedThreadPool(poolMaxSize);
        } else Pool = Executors.newCachedThreadPool();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }
    @Override
    public void setProperties(Properties p) {
        if (p.getProperty("databaseType")!=null) {
            this.databaseType = DatabaseType.getDatabaseType(p.getProperty("databaseType"));
            this.setDialect(DialectFactory.Generator(this.databaseType));
        }

        boolean b=false;
        try {
            b = Boolean.parseBoolean(p.getProperty("asyncTotalCount"));
        } catch (Exception e) {
        }
        setAsyncTotalCount(b);
        int pms=0;
        try {
            pms = Integer.parseInt(p.getProperty("poolMaxSize"));
        } catch (Exception e) {
        }
        setPoolMaxSize(pms);
    }

    public static int getCount(String sql, Connection conn, MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql, Dialect dialect) throws SQLException {
        String count_sql = dialect.getCountString(sql);
        logger.debug("Total count SQL [{"+count_sql+"}] ");
        logger.debug("Total count Parameters: {"+parameterObject+"} ");

        PreparedStatement countStmt = null;
        ResultSet rs = null;
        try {
            countStmt = conn.prepareStatement(count_sql);

            DefaultParameterHandler handler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
            handler.setParameters(countStmt);

            rs = countStmt.executeQuery();
            int count = 0;
            if (rs.next()) count = rs.getInt(1);
            logger.debug("Total count: {"+Integer.valueOf(count)+"}");
            return count;
        } finally {
            try { if (rs != null) rs.close(); }
            finally {
                try { if (countStmt != null) countStmt.close(); }
                finally {
                    if ((conn != null) && (!conn.isClosed())) conn.close();
                }
            }
        }
    }
}