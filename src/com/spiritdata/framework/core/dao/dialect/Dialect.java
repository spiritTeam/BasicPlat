package com.spiritdata.framework.core.dao.dialect;

/**
 * 数据库方言
 * @author wh
 */
public abstract class Dialect {
    /**
     * 得到计算结果总数的Sql语句
     * @param sql 原始的Sql语句
     * @return 计算结果的Sql语句
     */
    public String getCountString(String sql) {
        return "select count(1) from (" + sql + ") tmp_count";
    }

    /**
     * 得到硬分页的Sql语句
     * @param sql 原始Sql语句
     * @param offset 前偏移量
     * @param limit 限制数
     * @return 硬分页的Sql语句
     */
    public abstract String getLimitString(String sql, int offset, int limit);
}