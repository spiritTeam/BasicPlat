package com.spiritdata.framework.core.dao.dialect;

/**
 * H2方言
 * @author wh
 */
public class H2Dialect extends Dialect {
    @Override
    public String getLimitString(String sql, int offset, int limit) {
        return new StringBuffer(sql.length()+40).append(sql).append((offset > 0) ? " limit "+limit+" offset "+offset : " limit "+limit).toString();
    }
}