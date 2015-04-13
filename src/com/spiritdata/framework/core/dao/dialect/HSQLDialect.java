package com.spiritdata.framework.core.dao.dialect;

import com.spiritdata.framework.util.ChineseCharactersUtils;

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