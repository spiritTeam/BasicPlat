package com.spiritdata.framework.core.dao.dialect;

/**
 * Postgre方言
 * @author wh
 */
public class PostgreSQLDialect extends Dialect{
    public String getLimitString(String sql, int offset, int limit) {
        return new StringBuffer( sql.length()+20 ).append(sql).append(offset > 0 ? " limit "+offset+" offset "+limit : " limit "+limit).toString();
    }
}