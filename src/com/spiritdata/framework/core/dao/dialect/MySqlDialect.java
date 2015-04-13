package com.spiritdata.framework.core.dao.dialect;

import com.spiritdata.framework.util.ChineseCharactersUtils;

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