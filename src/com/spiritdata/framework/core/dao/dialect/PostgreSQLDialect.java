package com.spiritdata.framework.core.dao.dialect;

import com.spiritdata.framework.util.ChineseCharactersUtils;

/**
 * Postgre方言
 * @author wh
 */
public class PostgreSQLDialect extends Dialect{
    public String getLimitString(String sql, int offset, int limit) {
        return new StringBuffer( sql.length()+20 ).append(sql).append(offset > 0 ? " limit "+offset+" offset "+limit : " limit "+limit).toString();
    }

    @Override
    public int getStrLen(String str, String codeType) {
        if (codeType==null||codeType.length()==0) return ChineseCharactersUtils.getStrLen(str, 3);
        else
        if(codeType.toUpperCase().equals("UTF-8")||codeType.toUpperCase().equals("UTF8")) return ChineseCharactersUtils.getStrLen(str, 3);
        else
        if (codeType.toUpperCase().equals("GBK")||codeType.toUpperCase().equals("GB2312")) return ChineseCharactersUtils.getStrLen(str, 2);
        else return ChineseCharactersUtils.getStrLen(str, 3);
    }
}