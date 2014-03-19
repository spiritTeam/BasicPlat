package com.gmteam.framework.model.tree;

import com.gmteam.framework.model.BaseObject;

/**
 * 树节点的数据结构，所有的树对象都应继承这个类。
 * @author wh
 */
@SuppressWarnings("serial")
public abstract class TreeNodeModel extends BaseObject {
    //树节点ID
    String id;
    /**
     * 得到本节点ID
     * @return 本节点ID
     */
    public String getId() {
        return this.id;
    }
    /**
     * 设置本节点ID,同时把下级节点parentid设置为新的ID
     * @param id
     */
    public void setId(String id) {
        this.id=id;
    }
    //父节点ID
    String parentId;
    /**
     * 得到本节点的父节点ID，若本节点是根，则返回null
     * @return 父节点ID，若本节点是根，则返回null
     */
    public String getParentId() {
        return this.parentId;
    }
    /**
     * 设置本节点的父节点ID
     * @param parentId
     */
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    //节点名称
    String title;
    /**
     * 得到本节点名称
     * @return 本节点名称
     */
    public String getTitle() {
        return this.title;
    }
    /**
     * 设置本节点名称
     * @param title 节点名称
     */
    public void setTitle(String title) {
        this.title = title;
    }
    //排序
    int order;
    public int getOrder() {
        return this.order;
    }
    public void setOrder(int order) {
        this.order=order;
    }
    //排序模式:若orderType=0从大到小排列；若orderType=1从小到大排列，默认为0从大到小排序
    int orderType;
    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }
}