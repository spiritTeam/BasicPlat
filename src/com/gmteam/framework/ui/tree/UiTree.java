package com.gmteam.framework.ui.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gmteam.framework.core.model.tree.TreeNode;
import com.gmteam.framework.core.model.tree.TreeNodeBean;

/**
 * 为显示层提供的树对象的抽象方法，显示层的树都应是此类的子类。<br/>
 * 提供把树对象转换为树Map和树表Map的基础方法。<br/>
 * 本类的实现类要提供convert4Tree/convert4TreeGrid/convert4Attributes的方法。<br/>
 * 框架的思路是：把后台用到的树对象转换为显示层树对象，显示层树对象不影响后台树对象的结构，之后由显示层逻辑对显示层树对象进行处理。
 * @author wh
 * @param <T>
 */
@SuppressWarnings("serial")
public abstract class UiTree<T extends TreeNodeBean> extends TreeNode<T> {

    /**
     * 构造函数，需要其继承的类实现具体的构造方法
     * @param tn 需要转换的树
     * @throws CloneNotSupportedException 
     */
    public UiTree(TreeNode<? extends TreeNodeBean> tn) throws CloneNotSupportedException {
        T tnb = (T)tn.getTnEntity().clone();
        this.setTnEntity(tnb);
    }

    /**
     * 将显示树转换为Map对象
     * @return 显示树所对应的Map对象，若树为空，则返回null
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> toTreeMap() {
        Map<String, Object> uiTreeMap = new HashMap<String, Object>();
        uiTreeMap.putAll(this.convert4Tree());
        uiTreeMap.put("attributes", this.convert4Attributes());
        if (!this.isLeaf()) {
            List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();
            for (TreeNode<? extends TreeNodeBean> c: this.getChildren()) {
                UiTree<T> ut = (UiTree<T>)c;
                l.add(ut.toTreeMap());
            }
            uiTreeMap.put("children", l);
        }
        if (uiTreeMap.size()>0) return uiTreeMap;
        else return null;
    }

    /**
     * 将显示树转换为TreeGrid相对应的Map对象
     * @return 显示树所对应的Map对象，若树为空，则返回null
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> toTreeGridMap() {
        Map<String, Object> uiTreeGridMap = new HashMap<String, Object>();
        uiTreeGridMap.putAll(this.convert4TreeGrid());
        if (!this.isLeaf()) {
            List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();
            for (TreeNode<? extends TreeNodeBean> c: this.getChildren()) {
                UiTree<T> ut = (UiTree<T>)c;
                l.add(ut.toTreeGridMap());
            }
            uiTreeGridMap.put("children", l);
        }
        if (uiTreeGridMap.size()>0) return uiTreeGridMap;
        else return null;
    }

    /**
     * 转换为字段属性
     * @return 字段属性Map
     */
    protected abstract Map<String, Object> convert4Tree();

    /**
     * 转换为扩展属性。
     * @return 扩展属性Map
     */
    protected abstract Map<String, Object> convert4Attributes();

    /**
     * 转换为TreeGrid中的Grid行记录
     * @return Grid行记录
     */
    protected abstract Map<String, Object> convert4TreeGrid();
}