package com.gmteam.framework.ui.tree.easyUi;

import java.util.HashMap;
import java.util.Map;

import com.gmteam.framework.core.model.tree.TreeNode;
import com.gmteam.framework.core.model.tree.TreeNodeBean;
import com.gmteam.framework.ui.tree.UiTree;

public class EasyUiTree<T extends TreeNodeBean> extends UiTree<T> {
    private static final long serialVersionUID = 1810248521425568689L;

    private String state;
    private String iconCls;
    private boolean checked;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public EasyUiTree(TreeNode<? extends TreeNodeBean> tn) throws CloneNotSupportedException {
        super(tn);
        if (tn.isLeaf()) this.state="close"; else {
            this.state="closed";
            for (TreeNode<?> t: tn.getChildren()) this.addChild(new EasyUiTree<T>(t));
        }
    }

    @Override
    protected Map<String, Object> convert4Tree() {
        Map<String, Object> treeM = new HashMap<String, Object>();
        treeM.put("id", this.getId());
        treeM.put("text", this.getNodeName());
        treeM.put("state", this.getState());
        treeM.put("iconCls", this.getIconCls());
        treeM.put("checked", this.isChecked());
        return treeM;
    }

    @Override
    protected Map<String, Object> convert4Attributes() {
        return this.getTnEntity().toHashMapAsBean();
    }

    @Override
    protected Map<String, Object> convert4TreeGrid() {
        Map<String, Object> treeM = new HashMap<String, Object>();
        treeM.put("state", this.getState());
        treeM.put("iconCls", this.getIconCls());
        treeM.put("checked", this.isChecked());
        treeM.putAll(this.getTnEntity().toHashMapAsBean());
        return treeM;
    }
}