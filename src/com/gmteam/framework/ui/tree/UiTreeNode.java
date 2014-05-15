package com.gmteam.framework.ui.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gmteam.framework.core.model.tree.TreeNode;
import com.gmteam.framework.core.model.tree.TreeNodeBean;
import com.gmteam.framework.utils.JsonUtil;


/**
 * 为显示树所定义的结点结构。
 * @author wh
 * @param <T> 以UiTreeNodeModel为基类的类。
 */
@SuppressWarnings("serial")
public class UiTreeNode<T extends UiTreeNodeBean> extends TreeNode<T> {
    /**
     * 把TreeNode转换为UiTreeNode
     * @param tn
     * @return
    protected abstract UiTreeNode<? extends UiTreeNodeModel> convertFrom(TreeNode<? extends TreeNodeModel> tn);
     */

    /**
     * 
     * @param tnEntity
     */
    public UiTreeNode(T tnEntity) {
        super(tnEntity);
    }
    
    /**
     * 将显示树转换为Json串
     * @return Json串
     * @throws JsonProcessingException
     */
    protected String toJsonTree() throws JsonProcessingException {
        return JsonUtil.beanToJson(this.toTreeMap());
    }

    /**
     * 将显示树转换为Map对象
     * @return 显示树所对应的Map对象，若树为空，则返回null
     */
    public Map<String, Object> toTreeMap() {
        Map<String, Object> uiTreeMap = new HashMap<String, Object>();
        T e = this.getTnEntity();
        uiTreeMap.putAll(e.convert4Field());
        uiTreeMap.put("attributes", e.convert4Attributes());
        if (!this.isLeaf()) {
            List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();
            for (TreeNode<? extends TreeNodeBean> c: this.getChildren()) {
                TreeNodeBean tnm = c.getTnEntity();
                T newE = (T)e.convertFrom(tnm);
                UiTreeNode<T> utn = new UiTreeNode<T>(newE);
                l.add(utn.toTreeMap());
            }
            uiTreeMap.put("children", l);
        }
        if (uiTreeMap.size()>0) return uiTreeMap;
        else return null;
    }

    /**
     * 将显示树转换为TreeGrid的Json串
     * @return TreeGrid的Json串
     * @throws JsonProcessingException
     */
    protected String toJsonTreeGrid() throws JsonProcessingException {
        return JsonUtil.beanToJson(this.toTreeGridMap());
    }

    /**
     * 将显示树转换为TreeGrid相对应的Map对象
     * @return 显示树所对应的Map对象，若树为空，则返回null
     */
    public Map<String, Object> toTreeGridMap() {
        Map<String, Object> uiTreeGridMap = new HashMap<String, Object>();
        T e = this.getTnEntity();
        uiTreeGridMap.putAll(e.convert4TreeGrid());
        uiTreeGridMap.put("attributes", e.convert4Attributes());
        if (!this.isLeaf()) {
            List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();
            for (TreeNode<? extends TreeNodeBean> c: this.getChildren()) {
                TreeNodeBean tnm = c.getTnEntity();
                T newE = (T)e.convertFrom(tnm);
                UiTreeNode<T> utn = new UiTreeNode<T>(newE);
                l.add(utn.toTreeGridMap());
            }
            uiTreeGridMap.put("children", l);
        }
        if (uiTreeGridMap.size()>0) return uiTreeGridMap;
        else return null;
    }
}