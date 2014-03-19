package com.gmteam.framework.core.dao.dialect;

/**
 * MySql方言
 * @author wh
 */
public class MySqlDialect extends Dialect {
    @Override
    public String getLimitString(String sql, int offset, int limit) {
        if (offset > 0) return sql + " limit " + offset + "," + limit;
        return sql + " limit " + limit;
    }
}