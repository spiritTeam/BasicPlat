package com.spiritdata.framework.core.dao;

import com.spiritdata.framework.exceptionC.Plat0102CException;

/**
 * 数据库类型枚举值
 * @author wh
 */
public enum DatabaseType {
    Oracle("Oracle"), MySql("MySql"), SqlServer("SqlServer"), SqlServer2005("SqlServer2005"),
    DB2("DB2"), H2("H2"), HSQL("HSQL"), PostgreSQL("PostgreSQL");

    private String value;

    private DatabaseType(String value) {  
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static DatabaseType getDatabaseType(String value) {
        if (value==null||value.trim().length()==0) return DatabaseType.Oracle;

        if (value.toUpperCase().equals("ORACLE")) return DatabaseType.Oracle;
        if (value.toUpperCase().equals("MYSQL")) return DatabaseType.MySql;
        if (value.toUpperCase().equals("SQLSERVER")) return DatabaseType.SqlServer;
        if (value.toUpperCase().equals("SQLSERVER2005")) return DatabaseType.SqlServer2005;
        if (value.toUpperCase().equals("DB2")) return DatabaseType.DB2;
        if (value.toUpperCase().equals("H2")) return DatabaseType.H2;
        if (value.toUpperCase().equals("HSQL")) return DatabaseType.HSQL;
        if (value.toUpperCase().equals("POSTGRESQL")) return DatabaseType.PostgreSQL;

        throw new Plat0102CException("不能识别的数据库类型:"+value+"！");
    }
}