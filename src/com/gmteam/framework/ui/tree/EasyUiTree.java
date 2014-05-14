package com.gmteam.framework.ui.tree;

import java.util.ArrayList;
import java.util.List;

import com.gmteam.framework.core.model.tree.TreeNode;

public class EasyUiTree {

    private String id;
    private String parentId;
    private String iconCss;
    private String state;
    private String selected;//节点是否被选中
    private String text;
    private String title;
    private List<EasyUiTree> children;;
    private String url;

    public EasyUiTree(){
        
    }
    public EasyUiTree(TreeNode<?> tree){
        this.setId(tree.getId());
        this.setParentId(tree.getParentId());
//        this.setIconCss(tree.getTnEntity().getIcon());
  //      this.setText(tree.getTitle());
    //    this.setTitle(tree.getTitle());
      //  this.setUrl(tree.getTnEntity().getUrl());
        if (tree.getChildren()!=null) {
            this.children = new ArrayList<EasyUiTree>();
            for (TreeNode<?> tn: tree.getChildren()) {
                this.addChild(new EasyUiTree(tn));
            }
        }
    }
    //增加子节点
    public void addChild(EasyUiTree easyUiTree) {
        this.children.add(easyUiTree);
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
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
    public String getSelected() {
        return selected;
    }
    public void setSelected(String selected) {
        this.selected = selected;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public List<EasyUiTree> getChildren() {
        return children;
    }
    public void setChildren(List<EasyUiTree> children) {
        this.children = children;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}