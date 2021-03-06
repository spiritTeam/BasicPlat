package com.spiritdata.framework.core.dao.dialect;

import com.spiritdata.framework.util.ChineseCharactersUtils;

/**
 * Oracle方言
 * @author wh
 */
public class OracleDialect extends Dialect{
    @Override
    public String getLimitString(String sql, int offset, int limit) {
        sql = sql.trim();
        boolean isForUpdate = false;
        if (sql.toLowerCase().endsWith(" for update")) {
            sql = sql.substring( 0, sql.length()-11 );
            isForUpdate = true;
        }

        StringBuffer pagingSelect = new StringBuffer( sql.length()+100 );
        if (offset>0) pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
        else pagingSelect.append("select * from ( ");
        pagingSelect.append(sql);
        if (offset>0) {
            String endString = offset+"+"+limit;
            pagingSelect.append(" ) row_ ) where rownum_ <= " + endString + " and rownum_ > " + offset);
        } else pagingSelect.append(" ) where rownum <= " + limit);
        if (isForUpdate) pagingSelect.append( " for update" );

        return pagingSelect.toString();
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