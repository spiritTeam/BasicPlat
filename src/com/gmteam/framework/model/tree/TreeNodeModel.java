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
    String displayName;
    /**
     * 得到本节点名称
     * @return 本节点名称
     */
    public String getDisplayName() {
        return this.displayName;
    }
    /**
     * 设置本节点名称
     * @param title 节点名称
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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
    //节点url
    public String url;
    //设置节点url
    public void setUrl(String url){
        this.url=url;
    }
    //得到节点的url
    public String getUrl(){
        return this.url;
    }
    //节点的类型
    public Integer types;
    public Integer getTypes() {
        return types;
    }
    public void setTypes(Integer types) {
        this.types = types;
    }
    //节点的图片icon的衔接
    public String icon;
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    //节点说明descn
    public String descn;
    public String getDescn() {
        return descn;
    }
    public void setDescn(String descn) {
        this.descn = descn;
    }
}