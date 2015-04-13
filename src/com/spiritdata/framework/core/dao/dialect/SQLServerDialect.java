package com.spiritdata.framework.core.dao.dialect;

import com.spiritdata.framework.util.ChineseCharactersUtils;

/**
 * SqlServer旧版本数据库方言
 * @author wh
 */
public class SQLServerDialect extends Dialect{
    static int getAfterSelectInsertPoint(String sql) {
        int selectIndex = sql.toLowerCase().indexOf( "select" );
        final int selectDistinctIndex = sql.toLowerCase().indexOf( "select distinct" );
        return selectIndex + ( selectDistinctIndex == selectIndex ? 15 : 6 );
    }

    public String getLimitString(String sql, int offset, int limit) {
        return getLimitString(sql,offset,null,limit,null);
    }

    public String getLimitString(String querySelect, int offset,String offsetPlaceholder, int limit, String limitPlaceholder) {
        if (offset>0) throw new UnsupportedOperationException( "sql server has no offset" );

        return new StringBuffer( querySelect.length()+8)
                .append( querySelect )
                .insert( getAfterSelectInsertPoint( querySelect ), " top " + limit )
                .toString();
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