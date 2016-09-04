package com.spiritdata.framework.core.model.tree;

import com.spiritdata.framework.core.model.BaseObject;
import com.spiritdata.framework.util.StringUtils;

/**
 * 树结点数据Bean，所有的树对象的数据都应继承这个类。<br/>
 * 树对象是由树结点数据Bean和树结点结构两部分组成的。树结点数据，一般是从数据库或文件中读取的数据。树结点结构是根据这些数据组织好的树对象。<br/>
 * 本结构除基本的本结点ID，父结点ID和本结点名称外，还有树结点的排序，并指明排序的方向，排序字段是用来按顺序在同级结点间排列次序用的。<br/>
 * 本结构除基本的本结点ID，父结点ID和本结点名称外，还有树结点的排序，并指明排序的方向，排序字段是用来按顺序在同级结点间排列次序用的。<br/>
 *
 * @author wh
 * @see com.spiritdata.framework.core.model.tree.TreeNode
 * @since 0.1
 */
public class TreeNodeBean extends BaseObject implements Cloneable {
    private static final long serialVersionUID=-2142327508636587743L;

    //树结点ID
    private String id;
    /**
     * 得到结点ID
     * @return 结点ID
     */
    public String getId() {
        return this.id;
    }
    /**
     * 设置结点ID,同时把下级结点parentid设置为新的ID
     * @param id
     */
    public void setId(String id) {
        if (StringUtils.isNullOrEmptyOrSpace(id)) this.id=null;
        else this.id=id;
        if (this.treeNode!=null) {
            this.treeNode.setId(id);
        }
    }

    //父结点ID
    private String parentId;
    /**
     * 得到结点点的父结点ID，若结点点是根，则返回null
     * @return 父结点ID，若结点点是根，则返回null
     */
    public String getParentId() {
        return this.parentId;
    }
    /**
     * 设置结点的父结点ID
     * @param parentId
     */
    public void setParentId(String parentId) {
        if (StringUtils.isNullOrEmptyOrSpace(parentId)) this.parentId=null;
        else this.parentId=parentId;
        if (this.treeNode!=null) {
            this.treeNode.setParentId(parentId);
        }
    }

    //结点名称
    private String nodeName;
    /**
     * 得到本结点名称
     * @return 本结点名称
     */
    public String getNodeName() {
        return this.nodeName;
    }
    /**
     * 设置本结点名称
     * @param nodeName 结点名称
     */
    public void setNodeName(String nodeName) {
        this.nodeName=(nodeName==null?null:nodeName.trim());
        if (this.treeNode!=null) {
            this.treeNode.setNodeName(nodeName);
        }
    }

    //排序
    private int order;
    /**
     * 设置结点的排序序号
     * @return 树结点的排序号
     */
    public int getOrder() {
        return this.order;
    }
    /**
     * 设置结点的排序序号
     * @param order 树结点的排序号
     */
    public void setOrder(int order) {
        this.order=order;
        if (this.treeNode!=null) {
            this.treeNode.setOrder(order);
        }
    }

    //排序模式:若orderType=0从大到小排列；若orderType=1从小到大排列，默认为0从大到小排序
    protected int orderType=0;
    /**
     * 设置结点的排序模式
     * @param orderType 排序模式：若orderType=0从大到小排列；若orderType=1从小到大排列
     */
    public void setOrderType(int orderType) {
        this.orderType=orderType;
    }

    //对应的treeNode
    private TreeNode<? extends TreeNodeBean> treeNode;
    /**
     * 设置对应的treeNode
     */
    protected void setTreeNode(TreeNode<? extends TreeNodeBean> treeNode) {
        this.treeNode=treeNode;
    }

    /**
     * 浅层Clone方法，树clone时需要。
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * 所有此类的子类，若想让自定义属性及时反映到树结点中的Attribute中，请在任何自定义属性修改后，调用此方法
     * @param key 属性名
     * @param value 属性值
     */
    protected void setAttributeOnChangeFieldsValue(String key, Object value) {
        if (key==null) return;
        if (this.treeNode!=null) {
            Object _oldValue=this.treeNode.getAttribute(key);
            if (!_oldValue.equals(value)) this.treeNode.setAttribute(key, value);
        }
    }
}