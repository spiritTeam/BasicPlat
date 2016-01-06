package com.spiritdata.framework.ui.tree;

import java.util.HashMap;
import java.util.Map;

import com.spiritdata.framework.core.model.tree.TreeNode;
import com.spiritdata.framework.core.model.tree.TreeNodeBean;
import com.spiritdata.framework.exceptionC.Plat0003CException;

public class ZTree<T extends TreeNodeBean> extends UiTree<T> {
    private static final long serialVersionUID = -4240752866278555428L;

    private String state;
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

    /**
     * 构造函数
     * @param tn 需要转换的结点数据
     * @throws CloneNotSupportedException 
     */
    public ZTree(T tn) {
        this.setTnEntity(tn);
    }

    /**
     * 构造函数，需要其继承的类实现具体的构造方法
     * @param tn 需要转换的树
     * @throws CloneNotSupportedException 
     */
    public ZTree(TreeNode<? extends TreeNodeBean> tn) throws CloneNotSupportedException {
        this.setTnEntity((T)tn.getTnEntity());
        if (tn.isLeaf()) this.state="close"; else {
            this.state="closed";
            for (TreeNode<? extends TreeNodeBean> t: tn.getChildren()) this.addChild(new ZTree(t));
        }
    }

    /**
     * 构造函数，根据原树tn，构造深度遍历树的显示树，并满足如下条件：<br/>
     * 若原树tn的总结点数大于limitCount，则只返回此树结点下的一级结点树，否则返回整棵树。
     * @param tn 需要转换的树
     * @throws CloneNotSupportedException 
     */
    public ZTree(TreeNode<? extends TreeNodeBean> tn, int limitCount) throws CloneNotSupportedException {
        this.setTnEntity((T)tn.getTnEntity());
        if (limitCount<1) throw new Plat0003CException(new IllegalArgumentException("结点限制数必须大于0"));
        if (tn.getAllCount()>limitCount) {
            this.state=tn.isLeaf()?"close":"closed";
            for (TreeNode<? extends TreeNodeBean> t: tn.getChildren()) {
                ZTree<? extends TreeNodeBean> uit = new ZTree(t.getTnEntity());
                uit.state=t.isLeaf()?"close":"closed";
                this.addChild(uit);
            }
        } else {
            if (tn.isLeaf()) this.state="close"; else {
                this.state="closed";
                for (TreeNode<? extends TreeNodeBean> t: tn.getChildren()) this.addChild(new ZTree<T>(t));
            }
        }
    }

    @Override
    protected Map<String, Object> convert4Tree() {
        Map<String, Object> treeM = new HashMap<String, Object>();
        treeM.put("id", this.getId());
        treeM.put("name", this.getNodeName());
        treeM.put("state", this.getState());
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
        treeGridM.put("checked", this.isChecked());
        treeGridM.putAll(this.convert4Attributes());
        return treeGridM;
    }

}
