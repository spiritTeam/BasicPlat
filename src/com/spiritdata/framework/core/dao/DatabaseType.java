package com.spiritdata.framework.core.dao;

import java.lang.IllegalArgumentException;

/**
 * 数据库类型枚举值
 * @author wh
 */
public enum DatabaseType {
    Oracle("Oracle"), MySql("MySql"), SqlServer("SqlServer"), SqlServer2005("SqlServer2005"),
    DB2("DB2"), H2("H2"), HSQL("HSQL"), PostgreSQL("PostgreSQL");

    private String name;

    private DatabaseType(String name) {  
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static DatabaseType getDatabaseType(String name) {
        if (name==null) name="Oracle";
        if (name.equalsIgnoreCase("Oracle")) {
            return DatabaseType.Oracle;
        } else if (name.equalsIgnoreCase("MySql")) {
            return DatabaseType.MySql;
        } else if (name.equalsIgnoreCase("SqlServer")) {
            return DatabaseType.SqlServer;
        } else if (name.equalsIgnoreCase("SqlServer2005")) {
            return DatabaseType.SqlServer2005;
        } else if (name.equalsIgnoreCase("DB2")) {
            return DatabaseType.DB2;
        } else if (name.equalsIgnoreCase("H2")) {
            return DatabaseType.H2;
        } else if (name.equalsIgnoreCase("HSQL")) {
            return DatabaseType.HSQL;
        } else if (name.equalsIgnoreCase("PostgreSQL")) {
            return DatabaseType.PostgreSQL;
        } else throw new IllegalArgumentException("不能识别的数据库类型！");
    }
}