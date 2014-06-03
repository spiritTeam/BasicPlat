package com.gmteam.framework.ui.tree.easyUi;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.gmteam.framework.component.module.pojo.Module;
import com.gmteam.framework.component.module.service.ModuleService;
import com.gmteam.framework.core.model.tree.TreeNode;
import com.gmteam.framework.core.model.tree.TreeNodeBean;
import com.gmteam.framework.ui.tree.UiTree;

public class EasyUiTree<T extends TreeNodeBean> extends UiTree<T> {
    private static final long serialVersionUID = 1810248521425568689L;

    private String state;
    private String iconCls;
    private boolean checked;
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
    /**
     * 改了open
     * @param tn
     * @throws CloneNotSupportedException
     */
    public EasyUiTree(TreeNode<? extends TreeNodeBean> tn) throws CloneNotSupportedException {
        super(tn);
        if (tn.isLeaf()) this.state="open"; else {
            this.state="open";
            for (TreeNode<?> t: tn.getChildren()) this.addChild(new EasyUiTree<T>(t));
        }
    }

    @Override
    protected Map<String, Object> convert4Tree() {
        Map<String, Object> treeM = new HashMap<String, Object>();
        treeM.put("id", this.getId());
        this.getTnEntity().toHashMapAsBean();
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
        return this.getTnEntity().toHashMapAsBean();
    }
    @Resource
    ModuleService ms = new ModuleService();
    @Override
    protected Map<String, Object> convert4TreeGrid() {
        Map<String, Object> treeM = new HashMap<String, Object>();
        treeM.put("state", this.getState());
        treeM.put("iconCls", this.getIconCls());
        treeM.put("checked", this.isChecked());
        treeM.putAll(this.getTnEntity().toHashMapAsBean());
        Map<String, Object> m=this.getTnEntity().toHashMapAsBean();
        if (m.get("displayName")!=null) treeM.put("nodeName", m.get("displayName")+"");
        if(parentId.equals("0")){
            this.setParentNodeName("模块");
        }if(!parentId.isEmpty()&&!parentId.equals("0")&&!parentId.equals("-1")){
            Module mm = ms.getModuleById(this.parentId);
            System.out.println("");
            this.setParentNodeName(mm.getDisplayName());
        }
        treeM.put("parentNodeName", this.getParentNodeName());
        return treeM;
    }
}