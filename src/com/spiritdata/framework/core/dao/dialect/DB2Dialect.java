package com.spiritdata.framework.core.dao.dialect;

import com.spiritdata.framework.util.ChineseCharactersUtils;

/**
 * DB2方言
 * @author wh
 */
public class DB2Dialect extends Dialect {
    private static String getRowNumber(String sql) {
        StringBuffer rownumber = new StringBuffer(50).append("rownumber() over(");
        int orderByIndex = sql.toLowerCase().indexOf("order by");
        if ( orderByIndex>0 && !hasDistinct(sql) ) rownumber.append( sql.substring(orderByIndex) );
        rownumber.append(") as rownumber_,");
        return rownumber.toString();
    }

    private static boolean hasDistinct(String sql) {
        return sql.toLowerCase().indexOf("select distinct")>=0;
    }

    @Override
    public String getLimitString(String sql, int offset, int limit) {
        int startOfSelect = sql.toLowerCase().indexOf("select");
        StringBuffer pagingSelect = new StringBuffer( sql.length()+100 )
            .append( sql.substring(0, startOfSelect) ) //add the comment
            .append("select * from ( select ") //nest the main query in an outer select
            .append( getRowNumber(sql) ); //add the rownnumber bit into the outer query select list

        if ( hasDistinct(sql) ) {
            pagingSelect.append(" row_.* from ( ") //add another (inner) nested select
                .append( sql.substring(startOfSelect) ) //add the main query
                .append(" ) as row_"); //close off the inner nested select
        } else {
            pagingSelect.append( sql.substring( startOfSelect + 6 ) ); //add the main query
        }

        pagingSelect.append(" ) as temp_ where rownumber_ ");

        //add the restriction to the outer select
        if (offset>0) {
            String endString = offset+"+"+limit;
            pagingSelect.append("between "+offset+"+1 and "+endString);
        } else pagingSelect.append("<= "+limit);

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