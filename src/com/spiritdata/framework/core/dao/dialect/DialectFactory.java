package com.spiritdata.framework.core.dao.dialect;

import java.sql.Connection;
import java.sql.SQLException;

import com.spiritdata.framework.core.dao.DatabaseType;
import com.spiritdata.framework.exceptionC.Plat0102CException;

/**
 * <p>数据库方言的工厂类，可以根据不同的数据库类型得到对应的方言。
 * <p>要实现方言的可配置性，还需要修改代码，以适应xml或json的配置文件，或与spring相结合。
 * @author wh
 */
public abstract class DialectFactory {

    /**
     * 根据数据库类型生成方言
     * @param dt 数据库类型
     * @return 对应的方言
     */
    public static Dialect Generator(DatabaseType dt) {
        switch (dt) {
            case Oracle: return new OracleDialect();
            case MySql: return new MySqlDialect();
            case SqlServer: return new SQLServerDialect();
            case SqlServer2005: return new SQLServer2005Dialect();
            case DB2: return new DB2Dialect();
            case H2: return new H2Dialect();
            case HSQL: return new HSQLDialect();
            case PostgreSQL: return new PostgreSQLDialect();
            default: return new OracleDialect();
        }
    }

    /**
     * 根据数据库类型的名称生成方言
     * @param dbTypeName 数据库类型名称 
     * @return 对应的方言
     */
    public static Dialect Generator(String dbTypeName) {
        return DialectFactory.Generator(DatabaseType.getDatabaseType(dbTypeName));
    }

    /**
     * 根据数据库连接，自动匹配方言，若conn为空，则会抛出异常。
     * <p>目前只支持MySql，若非mysql都视为Oracle
     * @param conn 数据库连接对象
     * @return 对应的方言
     */
    public static Dialect Generator(Connection conn) {
        if (conn!=null) {
            try {
                return DialectFactory.Generator(conn.getMetaData().getDatabaseProductName());
            } catch (SQLException e) {
                throw new Plat0102CException(e);
            }
        } else  throw new Plat0102CException("conn为空");
    }
}