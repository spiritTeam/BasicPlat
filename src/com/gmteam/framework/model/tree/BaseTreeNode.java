package com.gmteam.framework.model.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gmteam.framework.model.BaseObject;

@SuppressWarnings("serial")
public class BaseTreeNode<T extends TreeNodeModel> extends BaseObject implements TreeNode<T>, Comparable<T> {

    private T tnEntity;//树模型实体

    /**
     * 设置实体
     * @param tnEntity 实体
     */
    public void setTnEntity(T tnEntity) {
        this.tnEntity = tnEntity;
        this.id = tnEntity.id;
        this.parentId = tnEntity.parentId;
        this.title = tnEntity.title;
        this.order=tnEntity.order;
        this.orderType=tnEntity.orderType;
        this.allowChildren=true;
    }

    /**
     * 处理图标的属性
     * 看之前pf的代码，还能处理图标，觉得不错，先拷过来
     * 用用，看看显示结果
     * 添加属性：url，icons，
     * 我添加的代码是“===========之间的部分”
     */
    //=======================================================
    private String url;
    private String icons;
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getIcons() {
        return icons;
    }
    /**
     * 取得显示的图标，若为空或不可访问的文件或URL则返回默认图标，否则返回记录中所存的图标
     * 这个默认图标应该有不同的图片，通过后台参数进行统一管理。
     * @return 图标的路径
     */
    public String getIconsFowShow() {
        if (icons==null||"".equals(icons)) {
            return "resources/images/sys/skin0/icon/moduleIcon/6.gif";
        } else {
            //判断是否确实存在这个文件，路径或URL都可以，若判断通过，则直接返回，否则要返回默认图标
            //判断实体是否可访问
            //若不能够访问，则从系统参数中取得默认的图标
            //**系统参数中可能会维护多个默认图标，如：模块组、模块；用户组、用户；字典组、字典；任务等等。
            return icons;
        }
    }

    public void setIcons(String icons) {
        this.icons = icons;
    }
    //=======================================================
    //树节点ID
    String id;
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        tnEntity.setId(id);
        this.id=id;
        if (!this.isLeaf()) {
            for (TreeNode<T> c: this.getChildren()) {
                c.setParentId(id);
            }
        }
    }
    //父节点ID
    String parentId;
    public String getParentId() {
        return this.parentId;
    }
    public void setParentId(String parentId) {
        this.parentId=parentId;
        tnEntity.setParentId(parentId);
    }
    //父节点
    TreeNode<T> parent;
    public TreeNode<T> getParent() {
        return this.parent;
    }
    @Deprecated
    public void setParent(TreeNode<T> parent) {
        this.parent = parent;
        this.parentId = parent.getId();
    }
    //树节点的层数，level=0，是根节点
    protected int level = 0;
    public int getLevel() {
        return this.level;
    }
    @Deprecated
    public void setLevel(int level) {
        this.level=level;
    }
    //是否允许有子节点
    protected boolean allowChildren;
    public boolean isAllowChildren() {
        return this.allowChildren;
    }
    public void setAllowChildren(boolean allowChildren) {
        this.allowChildren = allowChildren;
    }

    public boolean isLeaf() {
        return (this.children.size()==0);
    }

    public boolean isRoot() {
        return (this.getParent()==null);
    }

    public TreeNode<T> getRoot() {
        TreeNode<T> rootNode = this;
        while (!rootNode.isRoot()) rootNode=rootNode.getParent();
        return rootNode;
    }

    public int getAllCount() {
        int ret = this.children.size();
        if (ret>0) {
            for (TreeNode<T> tn: this.getChildren()) {
                ret += tn.getAllCount();
            }
        }
        return ret;
    }

    //树节点名称
    String title;
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
        tnEntity.setTitle(title);
    }

    public String getTreePathName() {
        String ret = this.title;
        TreeNode<T> _parent = this.parent;
        while(_parent!=null) {
            ret = _parent.getTitle()+"/"+ret;
            _parent = _parent.getParent();
        }
        return ret;
    }

    /**
     * 得到节点在树中路径名称
     * @param split 路径分割符
     * @param level 取路径的级别。-1:取全部;0:取到根但不包括跟;n:向上取n级，若n大于总级数，取全部
     * @return 节点路径名
     */
    public String getTreePathName(String split, int level) {
        int _level = level;
        String _s = (split==null?"/":split);
        String ret = this.title;
        TreeNode<T> _parent = this.parent;
        boolean canContinue = true;
        while(_parent!=null&&canContinue) {
            if (level<=-1) canContinue=true;
            else if (level==0) {
                if (_parent.isRoot()) canContinue = false;
                else ret = _parent.getTitle()+_s+ret;
            } else {
                if ((--_level)>0) ret = _parent.getTitle()+_s+ret;
                else canContinue = false;
            }
            _parent = _parent.getParent();
        }
        return ret;
    }
    //子节点列表
    List<TreeNode<T>> children;
    public List<TreeNode<T>> getChildren() {
        return this.children;
    }
    public void setChildren(List<TreeNode<T>> children) {
        if (!this.allowChildren) throw new IllegalStateException("节点不允许有子节点！");
        this.children = children;
    }

    public void addChild(TreeNode<T> child) {
        if (!allowChildren) throw new IllegalStateException("本节点[id="+this.id+";text="+this.title+"]不允许有子节点！");
        if (this.children==null) this.children = new ArrayList<TreeNode<T>>();
        this.removeChild(child.getId());
        child.setParent(this);
        child.setParentId(this.getId());
        child.setLevel(this.level+1);
        this.children.add(child);
    }

    public void addChildren(List<TreeNode<T>> children) {
        if (!allowChildren) throw new IllegalStateException("节点不允许有子节点！");
        if (this.children==null) this.children = new ArrayList<TreeNode<T>>();
        for (TreeNode<T> tn: children) {
            this.removeChild(tn.getId());
            tn.setParent(this);
            tn.setParentId(this.getId());
            tn.setLevel(this.level+1);
        }
        this.children.addAll(children);
    }

    public TreeNode<T> getChild(String id) {
        if (!allowChildren) throw new IllegalStateException("本节点[id="+this.id+" text="+this.title+"]不允许有子节点！");
        TreeNode<T> retNode=null;
        for (int i=0; i<this.children.size(); i++) {
            TreeNode<T> tn = this.children.get(i);
            if (tn.getId().equals(id)) {
                retNode=tn;
                break;
            }
        }
        return retNode;
    }

    public TreeNode<T> removeChild(String id) {
        if (!allowChildren) throw new IllegalStateException("本节点[id="+this.id+" text="+this.title+"]不允许有子节点！");
        int removeIndex=-1;
        for (int i=0; i<this.children.size(); i++) {
            TreeNode<T> tn = this.children.get(i);
            if (tn.getId().equals(id)) {
                removeIndex=i;
                break;
            }
        }
        if (removeIndex==-1) return null;
        else {
            TreeNode<T> removeNode = this.children.remove(removeIndex);
            return removeNode;
        }
    }

    public TreeNode<T> findNode(String id) {
        if (!allowChildren) return null;
        if (this.isLeaf()) return null;
        TreeNode<T> retNode=null;
        for (int i=0; i<this.children.size(); i++) {
            TreeNode<T> tn = this.children.get(i);
            if (tn.getId().equals(id)) {
                retNode=tn;
                break;
            }
        }
        if (retNode==null) {
            for (int i=0; i<this.children.size(); i++) {
                TreeNode<T> tn = this.children.get(i);
                if (!tn.isLeaf()) retNode = tn.findNode(id);
                if (retNode!=null) break;
            }
        }
        return retNode;
    }

    /**
     * 节点属性
     */
    protected Map<String, Object> attributes;
    /**
     * 得到本节点的扩展属性。<br/>
     * 注意:这里的属性还包括节点本身的属性，如ID，TITLE等
     * @return 本节点的属性，把这些属性存储在Map结构中
     */
    public Map<String, Object> getAttributes() {
        this.attributes = this.toHashMap();
        return attributes;
    }
    /**
     * 设置本节点的扩展属性。
     * @param attributes 扩展属性
     */
    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    /**
     * 设置节点属性，属性的key为String，值为Object。
     * @param key 属性的Key
     * @param value 属性的值
     */
    public void setAttribute(String key, Object value) {
        if (key.toUpperCase().equals("ID")) {
            if (value instanceof String) this.id= (String)value;
            else throw new IllegalArgumentException("当key为["+key+"]时,值的类型应为String类型");
        }
        if (key.toUpperCase().equals("TITLE")) {
            if (value instanceof String) this.title= (String)value;
            else throw new IllegalArgumentException("当key为["+key+"]时,值的类型应为String类型");
        }
        if (key.toUpperCase().equals("PARENTID")) {
            if (value instanceof String) this.parentId= (String)value;
            else throw new IllegalArgumentException("当key为["+key+"]时,值的类型应为String类型");
        }
        this.attributes.put(key, value);
    }

    /**
     * 得到指定key对应的节点属性值。如果属性不包含key的映射关系，则返回 null。
     * @param key 属性的Key
     * @return key对应的节点属性值，如果无对应的<key, value>键值对，返回null。
     */
    public Object getAttribute(String key) {
        return this.attributes.get(key);
    }

    /**
     * 删除key对应的节点属性值。
     * @param key 属性的Key
     */
    public void removeAttribute(String key) {
        this.attributes.remove(key);
    }

    public void setId(Serializable id) {
        this.id = (String)id;
    }

    //以下与排序有关
    //排序号
    int order;
    public int getOrder() {
        return this.order;
    }
    public void setOrder(int order) {
        this.order=order;
    }
    //排序模式:若orderType=1从小到大排列，默认为0从大到小排序
    int orderType=0;
    public void setOrderType(int orderType) {
        this.orderType=orderType;
    }

    public TreeNode<T> preNode() {
        if (this.parent==null) return null;
        int i=0;
        for (; i<this.parent.getChildren().size()-1; i++) {
            TreeNode<T> n = this.parent.getChildren().get(i);
            if (n.getId().equals(this.id)) break;
        }
        return (i==0?null:this.parent.getChildren().get(i-1));
    }

    public TreeNode<T> nextNode() {
        if (this.parent==null) return null;
        int i=this.parent.getChildren().size()-1;
        for (; i>=0; i--) {
            TreeNode<T> n = this.parent.getChildren().get(i);
            if (n.getId().equals(this.id)) break;
        }
        return (i==(this.parent.getChildren().size()-1)?null:this.parent.getChildren().get(i+1));
    }

    /**
     * 排序方法，order越大越靠前
     */
    public int compareTo(T tn) {
        return orderType==0?(tn.getOrder()-this.getOrder()):(this.getOrder()-tn.getOrder());
    }
}