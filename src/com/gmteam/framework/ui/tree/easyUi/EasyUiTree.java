package com.gmteam.framework.ui.tree.easyUi;

import java.util.HashMap;
import java.util.Map;
import com.gmteam.framework.core.model.tree.TreeNode;
import com.gmteam.framework.core.model.tree.TreeNodeBean;
import com.gmteam.framework.ui.tree.UiTree;
/**
 * EasyUi中的树，继承自UiTree，所有用easyUi作为前台树显示的模块都可以直接用这个类。
 * 只要设置相应的T就可以了。<br/>
 * 如：EasyUiTree<Module> met = new EasyUiTree<Module>(root);<br/>
 * 个性化的功能还在调用的类中自己实现，如自动展开到第几级等。本类中提供的构造树的方法，所有结点都是收起的。
 * @author wh
 * @param <T>
 */
public class EasyUiTree<T extends TreeNodeBean> extends UiTree<T> {
    private static final long serialVersionUID = 1810248521425568689L;

    private String state;
    private String iconCls;
    private boolean checked;
    private String pName;
    public String getNodeName() {
        return nodeName;
    }
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
    public String getpName() {
        return pName;
    }
    public void setpName(String pName) {
        this.pName = pName;
    }
    private String parentNodeName;
    
    public String getParentNodeName() {
        return parentNodeName;
    }
    public void setParentNodeName(String parentNodeName) {
        this.parentNodeName = parentNodeName;
    }

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
        Map<String, Object> m=this.getTnEntity().toHashMapAsBean();
        if (m.get("displayName")!=null) treeM.put("text", m.get("displayName")+"");
        return treeM;
    }

    @Override
    protected Map<String, Object> convert4Attributes() {
        Map<String, Object> retM = this.getTnEntity().toHashMapAsBean();
        retM.putAll(this.getAttributes());
        return retM;
    }

    @Override
    protected Map<String, Object> convert4TreeGrid() {
        Map<String, Object> treeGridM = new HashMap<String, Object>();
        treeGridM.put("state", this.getState());
        treeGridM.put("iconCls", this.getIconCls());
        treeGridM.put("checked", this.isChecked());
        treeGridM.putAll(this.convert4Attributes());
        return treeGridM;
    }
}