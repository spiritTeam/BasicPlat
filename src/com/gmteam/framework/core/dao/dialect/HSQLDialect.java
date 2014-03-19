package com.gmteam.framework.core.dao.dialect;

/**
 * HSQL方言
 * @author wh
 */
public class HSQLDialect extends Dialect{
    @Override
    public String getLimitString(String sql, int offset, int limit) {
        boolean hasOffset = offset>0;
        return new StringBuffer(sql.length()+10).append(sql).insert( sql.toLowerCase().indexOf("select") + 6, hasOffset ? " limit "+offset+" "+limit : " top "+limit).toString();
    }
}
