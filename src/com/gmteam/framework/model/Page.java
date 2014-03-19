package com.gmteam.framework.model;

import java.io.Serializable;    
import java.util.ArrayList;    
import java.util.Collection;    

/**
 * 分页对象基类。
 * 包含当前页数据及分页信息如总记录数。
 * @author zhuhua,wanghui
 */

@SuppressWarnings("serial")
public class Page<T> implements Serializable {
    public static int DEFAULT_PAGE_SIZE = 50;

    /**
     * 当前页的数据,类型一般为List
     */
    protected Collection<T> data;
    /**
     * 取当前页中的记录
     */
    public Collection<T> getResult() {
        return data;
    }

    /**
     * 每页的记录数
     */
    protected int pageSize = Page.DEFAULT_PAGE_SIZE;
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 当前页的编号
     */
    protected int pageIndex = 0;
    public int getPageIndex() {
        return pageIndex;
    }
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    /**
     * 记录总数
     */
    protected int dataCount;
    public int getDataCount() {
        return dataCount;
    }

    /**
     * 总页数
     */
    public int getPageCount() {
        return (this.getDataCount()/pageSize)+(this.getDataCount()%pageSize==0?0:1);
    }

    /**
     * 响应时间
     */
    protected long requestTime;
    public long getRequestTime() {
        return requestTime;
    }
    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }

    /**
     * 构造方法，只构造空页。
     */
    public Page() {
        this(0, Page.DEFAULT_PAGE_SIZE, 0, new ArrayList<T>());
    }

    /**
     * 默认构造方法.
     * @param dataCount 总记录条数
     * @param pageSize 没页记录数
     * @param pageIndex 本页的索引数
     * @param data 本页包含的数据
     */
    public Page(int dataCount,int pageSize,int pageIndex, Collection<T> data) {
        this.pageSize = pageSize;
        this.pageIndex = pageIndex;
        this.dataCount = dataCount;
        this.data = data;
    }

    /**
     * 从所有的记录allData中根据pageSize和pageIndex计算出当前页数据
     * @param pageSize 每页记录数
     * @param pageIndex 本页的索引数
     * @param allData 所有的记录
     * @return 返回
     */
    public static <P> Page<P> getPage(int pageSize,int pageIndex, Collection<P> allData) {
        if (pageIndex<1) pageIndex=1;
        int _dataCount = allData.size();
        if ((pageIndex*pageSize)>_dataCount) pageIndex=_dataCount/pageSize;
        int _start = ((pageIndex-1)*pageSize)+1;
        int _end = (pageIndex*pageSize)>_dataCount?_dataCount:(pageIndex*pageSize);

        Object[] elementData = allData.toArray();
        allData.clear();
        for (int i=_start-1; i<_end; i++) {
            allData.add(((P)elementData[i]));
        }
        return new Page<P>(_dataCount, pageSize, pageIndex, allData);
    }
}