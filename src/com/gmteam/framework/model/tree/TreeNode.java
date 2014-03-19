package com.gmteam.framework.model.tree;

import java.util.List;

/**
 * 树节点接口，所有的树对象都应实现这个接口。
 * @author wh
 */

public interface TreeNode<T extends TreeNodeModel> {
    /**
     * 得到本节点ID
     * @return 本节点ID
     */
    public String getId();

    /**
     * 设置本节点ID,同时把下级节点parentid设置为新的ID
     * @param id
     */
    public void setId(String id);

    /**
     * 得到本节点的父节点ID，若本节点是根，则返回null
     * @return 父节点ID，若本节点是根，则返回null
     */
    public String getParentId();

    /**
     * 设置本节点的父节点ID
     * @param parentId
     */
    public void setParentId(String parentId);

    /**
     * 得到本节点的父节点，若本节点是根，则返回null
     * @return 父节点，若本节点是根，则返回null
     */
    public TreeNode<T> getParent();

    /**
     * 设置本节点的父节点。<br/>
     * 这个方法在使用中不建议调用，否则会引起树结构的不一致，构建树要使用addChild或addChildren的方法
     * @param parent 父节点
     */
    @Deprecated
    public void setParent(TreeNode<T> parent);

    /**
     * 得到本节点的在树中的层数
     * @return 层数，根节点的层数是0
     */
    public int getLevel();

    /**
     * 设置本节点的在树中的层数。<br/>
     * 这个方法在使用中不建议调用，否则会引起树结构的不一致，在构建树中，系统会自动完成设置这个参数
     * @param level 层数
     */
    @Deprecated
    public void setLevel(int level);

    /**
     * 本节点是否允许有子节点
     * @return true允许有子节点，false不能有子节点
     */
    public boolean isAllowChildren();

    /**
     * 设置本节点是否允许有子节点
     * @param allowChildren true允许有子节点，false不能有子节点
     */
    public void setAllowChildren(boolean allowChildren);

    /**
     * 判断是否是叶节点。如果子节点数是0，则是叶节点。
     * @return 如果是子节点返回true，否则返回false
     */
    public boolean isLeaf();

    /**
     * 判断是否是根节点。每棵树只有一个根节点，父节点为空的为根节点。
     * @return 如果是树的根，返回ture
     */
    public boolean isRoot();

    /**
     * 返回包含此节点的树的根。
     * @return 此节点的根节点
     */
    public TreeNode<T> getRoot();

    /**
     * 得到节点下的所有子节点的个数
     * @return 所有子节点个数
     */
    public int getAllCount();

    /**
     * 得到本节点名称
     * @return 本节点名称
     */
    public String getTitle();

    /**
     * 设置本节点名称
     * @param title 节点名称
     */
    public void setTitle(String title);

    /**
     * 得到节点在树中路径名，此路径是从根到本级的全路径名
     * @return 节点路径名
     */
    public String getTreePathName();

    /**
     * 得到节点在树中路径名称
     * @param split 路径分割符
     * @param level 取路径的级别。-1:取全部;0:取到根但不包括根;n:向上取n级，若n大于总级数，取全部
     * @return 节点路径名
     */
    public String getTreePathName(String split, int level);

    /**
     * 得到本节点的子节点列表
     */
    public List<TreeNode<T>> getChildren();

    /**
     * 设置子节点列表。如果不允许有子节点，则抛出IllegalStateException异常。
     * @param children 子节点列表
     * @exception IllegalStateException 如果节点不允许有子节点
     */
    public void setChildren(List<TreeNode<T>> children);

    /**
     * 为子节点列表增加一个子节点。<br>
     * 如果不允许有子节点，则抛出IllegalStateException异常。
     * @param child 新增加的节点
     * @exception IllegalStateException 如果节点不允许有子节点
     */
    public void addChild(TreeNode<T> child);

    /**
     * 为子节点列表追加一个列表。<br>
     * 如果不允许有子节点，则抛出IllegalStateException异常。
     * @param children 新增加的节点列表
     * @exception IllegalStateException 如果节点不允许有子节点
     */
    public void addChildren(List<TreeNode<T>> children);

    /**
     * 从子节点列表中，查找id为给定值的节点，若给定id不存在，返回null。<br/>
     * @param id 欲删除的子节点的id
     */
    public TreeNode<T> getChild(String id);

    /**
     * 从子节点列表中，删除id为给定值的子节点。
     * <P>删除成功:返回被删除的节点；
     * <br/>删除失败:如果子节点列表中找不到该节点，返回null。
     * <br/>如果本节点不允许有子节点，则返回null。
     * @param id 欲删除的节点的id
     * @return 删除成功返回被删除的节点；删除失败，返回null
     */
    public TreeNode<T> removeChild(String id);

    /**
     * 以本节点为根，查找id为给定值的节点，查找范围为本节点下所有的节点，若找不到对应的节点则返回null。<br>
     * 如果不允许有子节点，则返回null。
     * @param id 节点id
     * @return id为给定值的节点，若给定id不存在或本节点不允许有子节点，返回null
     */
    public TreeNode<T> findNode(String id);

    /**
     * 得到本节点的排序号
     * @return 本节点排序号
     */
    public int getOrder();

    /**
     * 设置本节点的排序号
     * @param order 本节点排序号
     */
    public void setOrder(int order);

    /**
     * 设置本节点的排序模式<br/>
     * 若orderType=0从大到小排列；若orderType=1从小到大排列，默认为0从大到小排序
     * @param orderType 排序模式
     */
    public void setOrderType(int orderType);

    /**
     * 得到该节点在同级节点中的前序节点，若本节点为同级节点的第一个节点，则返回null；若本节点为根，则返回null；
     * @return 前序节点
     */
    public TreeNode<T> preNode();

    /**
     * 得到该节点在同级节点中的后续节点，若本节点为同级节点的最后节点，则返回null；若本节点为根，则返回null；
     * @return 后序节点
     */
    public TreeNode<T> nextNode();
}