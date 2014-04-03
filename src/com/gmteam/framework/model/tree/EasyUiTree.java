package com.gmteam.framework.model.tree;

import java.util.HashMap;

public class EasyUiTree extends BaseTreeNode<TreeNodeModel>{
    private String text;//easyUiTree 名称
    private String iconCss;//easyUiTree 图标
    private String state;//easyUiTree 打开状态，close(子),open(打开),closed(收起)
    private String checked;//easyUiTree 是否被选择了

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getIconCss() {
        return iconCss;
    }
    public void setIconCss(String iconCss) {
        this.iconCss = iconCss;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getChecked() {
        return checked;
    }
    public void setChecked(String checked) {
        this.checked = checked;
    }
    /**
     * 根据tree转换为EasyUiTree
     */
    public EasyUiTree(TreeNode<?> tree) {
        this.setAllowChildren(tree.isAllowChildren());
        //this.setAttributes((HashMap<String, Object>)((HashMap<String, Object>)tree.getAttributes()).clone());
        this.setId(tree.getId());
        //this.setLevel(tree.getLevel());
        this.setOrder(tree.getOrder());
        this.setOrderType(tree.getOrder());
        this.setParentId(tree.getParentId());
        this.setTitle(tree.getTitle());
        this.setText(tree.getTitle());
        if (!tree.isLeaf()) {
            for (TreeNode<?> tn: tree.getChildren()) {
                this.addChild(new EasyUiTree(tn));
            } 
        }
    }
    
    public void setStateAndParentNull() {
        setStateAndParentNull(this);
    }
    private void setStateAndParentNull(EasyUiTree t) {
        t.setParent(null);
        if (!t.isLeaf()) t.setState("closed");
        else {
            if (t.getAttribute("isLeaf")==null) t.setState("close");
            else {
                if (t.getAttribute("isLeaf").equals("false")) t.setState("closed");
                else t.setState("close");
            }
        }
        if (!t.isLeaf()) {
            for (TreeNode tn: t.getChildren()) {
                setStateAndParentNull((EasyUiTree)tn);
            }
        }
    }
}
