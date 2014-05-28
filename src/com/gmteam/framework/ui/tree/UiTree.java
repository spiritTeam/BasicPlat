package com.gmteam.framework.ui.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gmteam.framework.core.model.tree.TreeNode;
import com.gmteam.framework.core.model.tree.TreeNodeBean;

@SuppressWarnings("serial")
public abstract class UiTree<T extends TreeNodeBean> extends TreeNode<T> {

    /**
     * 构造函数，需要其继承的
     * @param tn
     * @throws CloneNotSupportedException 
     */
    public UiTree(TreeNode<? extends TreeNodeBean> tn) throws CloneNotSupportedException {
        T tnb = tn.getTnEntity().clone();
        this.setTnEntity(tnb);
    }

    /**
     * 将显示树转换为Map对象
     * @return 显示树所对应的Map对象，若树为空，则返回null
     */
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