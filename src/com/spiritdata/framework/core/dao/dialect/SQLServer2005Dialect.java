package com.spiritdata.framework.core.dao.dialect;

import com.spiritdata.framework.util.ChineseCharactersUtils;
import com.spiritdata.framework.util.StringUtils;

/**
 * SQLServer200方言
 * @author wh
 */
public class SQLServer2005Dialect extends Dialect{

    /**
     * Add a LIMIT clause to the given SQL SELECT
     *
     * The LIMIT SQL will look like:
     *
     * WITH query AS
     *      (SELECT TOP 100 percent ROW_NUMBER() OVER (ORDER BY CURRENT_TIMESTAMP) as __row_number__, * from table_name)
     * SELECT *
     * FROM query
     * WHERE __row_number__ BETWEEN :offset and :lastRows
     * ORDER BY __row_number__
     * 
     * @param querySqlString The SQL statement to base the limit query off of.
     * @param offset         Offset of the first row to be returned by the query (zero-based)
     * @param limit          Maximum number of rows to be returned by the query
     * @return A new SQL statement with the LIMIT clause applied.
     */
    @Override
    public String getLimitString(String querySqlString, int offset, int limit) {
        StringBuffer pagingBuilder = new StringBuffer();
        String orderby = getOrderByPart(querySqlString);
        String distinctStr = "";

        String loweredString = querySqlString.toLowerCase();
        String sqlPartString = querySqlString;
        if (loweredString.trim().startsWith("select")) {
            int index = 6;
            if (loweredString.startsWith("select distinct")) {
                distinctStr = "DISTINCT ";
                index = 15;
            }
            sqlPartString = sqlPartString.substring(index);
        }
        pagingBuilder.append(sqlPartString);

        if (StringUtils.isNullOrEmptyOrSpace(orderby)) orderby="ORDER BY CURRENT_TIMESTAMP";

        StringBuffer result = new StringBuffer();
        result.append("WITH query AS (SELECT ")
            .append(distinctStr)
            .append("TOP 100 PERCENT ")
            .append(" ROW_NUMBER() OVER (")
            .append(orderby)
            .append(") as __row_number__, ")
            .append(pagingBuilder)
            .append(") SELECT * FROM query WHERE __row_number__ BETWEEN ")
            .append(offset+1).append(" AND ").append(offset+limit)
            .append(" ORDER BY __row_number__");

        return result.toString();
    }

    static String getOrderByPart(String sql) {
        String loweredString = sql.toLowerCase();
        int orderByIndex = loweredString.indexOf("order by");
        if (orderByIndex != -1) {
            // if we find a new "order by" then we need to ignore
            // the previous one since it was probably used for a subquery
            return sql.substring(orderByIndex);
        } else return "";
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