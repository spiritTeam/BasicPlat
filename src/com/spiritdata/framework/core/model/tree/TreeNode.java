package com.spiritdata.framework.core.model.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.spiritdata.framework.core.model.BaseObject;
import com.spiritdata.framework.exceptionC.Plat0003CException;

/**
 * 树结点结构。所有树结构都应以此类为基类。此结构支持各结点数据结构不同的混合树，但要保证这些结点的实体都是TreeNodeBeand的继承类<br/>
 * 树对象是由树结点数据和树结点结构两部分组成的。
 * 注意树节点的Attribute是用户来扩展树的结构的，修改其中的内容，不会导致树结点对应的属性变化。
 * 但：Attribute中的数据是树节点数据及treeNodeBean数据的另一种存储方式，因此修改了这其中的属性，讲自动修改attribute中的内容
 * @author wh
 * @param <T> 以TreeNodeBean为基类的类。
 * @see com.spiritdata.framework.core.model.tree.TreeNodeBean
 * @since 0.1
 */
public class TreeNode<T extends TreeNodeBean> extends BaseObject implements Cloneable, Comparable<TreeNode<? extends TreeNodeBean>> {
    private static final long serialVersionUID=-4718111007014669779L;

    protected T tnEntity;//树模型实体

    /**
     * 设置实体，并根据实体设置树结点信息
     * @param tnEntity 实体
     */
    public void setTnEntity(T tnEntity) {
        this.tnEntity=tnEntity;
        if (this.tnEntity!=null) {
            this.id=tnEntity.getId();
            this.parentId=tnEntity.getParentId();
            this.nodeName=tnEntity.getNodeName();
            this.orderType=tnEntity.orderType;
            this.setOrder(tnEntity.getOrder());
            this.allowChildren=true;
            this.attributes=tnEntity.toHashMapAsBean();
            if (this.tnEntity.treeNode==null) this.tnEntity.setTreeNode(this);
            else {
                if (this.tnEntity.treeNode.getClass().getName().equals(this.getClass().getName())) this.tnEntity.setTreeNode(this);
            }
        }
    }

    /**
     * 得到实体对象
     * @return 实体对象
     */
    public T getTnEntity() {
        return tnEntity;
    }

    /**
     * 无参数构造函数
     */
    public TreeNode() {
        this.allowChildren=true;
    }

    /**
     * 由树结点数据构造类
     * @param tnEntity 树结点数据
     */
    public TreeNode(T tnEntity) {
        super();
        this.setTnEntity(tnEntity);
    }

    //树结点ID
    protected String id;
    /**
     * 得到本结点ID
     * @return 本结点ID
     */
    public String getId() {
        return this.id;
    }
    /**
     * 设置本结点ID,同时把下级结点parentid设置为新的ID
     * @param id
     */
    public void setId(String id) {
        this.id=id;
        if (tnEntity!=null&&tnEntity.getId()!=null&&!tnEntity.getId().equals(id)) tnEntity.setId(id);
        if (!id.equals(this.getAttribute("id")+"")) this.setAttribute("id", id);

        if (!this.isLeaf()) {
            for (TreeNode<? extends TreeNodeBean> c: this.getChildren()) {
                c.setParentId(id);
            }
        }
    }
    public void setId(Serializable id) {
        this.id=(String)id;
    }

    //父结点ID
    protected String parentId=null;
    /**
     * 得到本结点的父结点ID，若本结点是根，则返回null
     * @return 父结点ID，若本结点是根，则返回null
     */
    public String getParentId() {
        return this.parentId;
    }
    /**
     * 设置本结点的父结点ID
     * @param parentId 父结点ID
     */
    @SuppressWarnings("unused")
    public void setParentId(String parentId) {
        if (parentId==null) return;
        if (this.getParent()!=null&&this.getParent().getId()!=null) {
            if (!this.getParent().getId().equals(parentId)) {
                throw new Plat0003CException("预设值的父结点ID与已设置的父结点对象ID不相符合！");
            }
        }
        this.parentId=parentId;
        if (tnEntity!=null) {
            if (parentId==null&&tnEntity.getParentId()!=null) tnEntity.setParentId(parentId);
            if (tnEntity.getParentId()!=null&&!tnEntity.getParentId().equals(parentId)) tnEntity.setParentId(parentId);
        }
        if (parentId!=null&&!parentId.equals(this.getAttribute("parentId")+"")) this.setAttribute("parentId", parentId);
        if (parentId==null) this.removeAttribute("parentId");
    }

    //父结点
    protected TreeNode<? extends TreeNodeBean> parent;
    /**
     * 得到本结点的父结点，若本结点是根，则返回null
     * @return 父结点，若本结点是根，则返回null
     */
    public TreeNode<? extends TreeNodeBean> getParent() {
        return this.parent;
    }
    /**
     * 设置本结点的父结点。<br/>
     * 这个方法在使用中不建议调用，否则会引起树结构的不一致，构建树要使用addChild或addChildren的方法
     * @param parent 父结点
     */
    private void setParent(TreeNode<? extends TreeNodeBean> parent) {
        this.parent=parent;
        if (parent!=null) {
            this.setParentId(parent.getId());
        } else {
            this.parentId=null;
            if (tnEntity!=null) tnEntity.setParentId(null);
        }
    }

    //树结点名称
    protected String nodeName;
    /**
     * 设置本结点名称
     * @param title 结点名称
     */
    public void setNodeName(String nodeName) {
        this.nodeName=nodeName;
        if (tnEntity!=null&&!tnEntity.getNodeName().equals(nodeName)) tnEntity.setNodeName(nodeName);
        if (!nodeName.equals(this.getAttribute("nodeName")+"")) this.setAttribute("nodeName", nodeName);
    }
    /**
     * 得到本结点名称
     * @return 结点名称
     */
    public String getNodeName() {
        return this.nodeName;
    }

    //树结点的层数，level=0，是根结点
    /**
     * 得到本结点的在树中的层数
     * @return 层数，根结点的层数是0
     */
    public int getLevel() {
        int _l=0;
        TreeNode<? extends TreeNodeBean> _tn=this;
        while(!_tn.isRoot()) {
            _l++;
            _tn=_tn.getParent();
        }
        return _l;
    }

    //是否允许有子结点
    protected boolean allowChildren;
    /**
     * 本结点是否允许有子结点
     * @return true允许有子结点，false不能有子结点
     */
    public boolean isAllowChildren() {
        return this.allowChildren;
    }
    /**
     * 设置本结点是否允许有子结点
     * @param allowChildren true允许有子结点，false不能有子结点
     */
    public void setAllowChildren(boolean allowChildren) {
        this.allowChildren=allowChildren;
    }

    //以下为结点操作
    /**
     * 判断是否是叶结点。如果子结点数是0，则是叶结点。
     * @return 如果是叶结点返回true，否则返回false
     */
    public boolean isLeaf() {
        return (this.children==null||this.children.size()==0);
    }

    /**
     * 判断是否是根结点。每棵树只有一个结结点，父结点为空的为根结点。
     * @return 如果是树的根，返回ture
     */
    public boolean isRoot() {
        return (this.getParent()==null);
    }

    /**
     * 返回包含此结点的树的根。
     * @return 此结点的树的根结点
     */
    public TreeNode<? extends TreeNodeBean> getRoot() {
        TreeNode<? extends TreeNodeBean> rootNode=this;
        while (!rootNode.isRoot()) rootNode=rootNode.getParent();
        return rootNode;
    }

    /**
     * 得到结点在树中路径名，此路径是从根到本级的全路径名
     * @return 结点路径名，以"/"分割
     */
    public String getTreePathName() {
        return getTreePathName(null, -1);
    }

    /**
     * 得到结点在树中路径名称
     * @param split 路径分割符
     * @param level 取路径的级别。小于等于-1:取全部;0:取到根但不包括跟;n:向上取n级，若n大于总级数，取全部
     * @return 结点路径名
     */
    public String getTreePathName(String split, int level) {
        int _level=level;
        String _s=(split==null?"/":split);
        String ret=this.nodeName;
        TreeNode<? extends TreeNodeBean> _parent=this.parent;
        boolean canContinue=true;
        while(_parent!=null&&canContinue) {
            if (level<=-1) {
                canContinue=true;
                ret=_parent.getNodeName()+_s+ret;
            } else if (level==0) {
                if (_parent.isRoot()) canContinue=false;
                else ret=_parent.getNodeName()+_s+ret;
            } else {
                if ((--_level)>0) ret=_parent.getNodeName()+_s+ret;
                else canContinue=false;
            }
            _parent=_parent.getParent();
        }
        return ret;
    }

    /**
     * 得到结点在树中Id路径
     * @param split 路径分割符
     * @param level 取路径的级别。小于等于-1:取全部;0:取到根但不包括跟;n:向上取n级，若n大于总级数，取全部
     * @return 结点路径名
     */
    public String getTreePathId(String split, int level) {
        int _level=level;
        String _s=(split==null?"/":split);
        String ret=this.id;
        TreeNode<? extends TreeNodeBean> _parent=this.parent;
        boolean canContinue=true;
        while(_parent!=null&&canContinue) {
            if (level<=-1) {
                canContinue=true;
                ret=_parent.getId()+_s+ret;
            } else if (level==0) {
                if (_parent.isRoot()) canContinue=false;
                else ret=_parent.getId()+_s+ret;
            } else {
                if ((--_level)>0) ret=_parent.getId()+_s+ret;
                else canContinue=false;
            }
            _parent=_parent.getParent();
        }
        return ret;
    }

    //以下为计数操作
    /**
     * 得到结点下的所有子结点的个数
     * @return 所有递归子结结点个数
     */
    public int getAllCount() {
        if (this.isLeaf()) return 0;
        int ret=this.children.size();
        if (ret>0) {
            for (TreeNode<? extends TreeNodeBean> tn: this.getChildren()) {
                ret += tn.getAllCount();
            }
        }
        return ret;
    }
    /**
     * 获得子结点个数
     * @return 子结点个数
     */
    public int getChildCount() {
        return (this.children==null?0:this.children.size());
    }

    //以下子结点操作
    //子结点列表
    protected List<TreeNode<? extends TreeNodeBean>> children;
    /**
     * 得到本结点的子结点列表
     */
    public List<TreeNode<? extends TreeNodeBean>> getChildren() {
        return this.children;
    }

    /**
     * 根据id，得到相应的子结点
     * @param id 子结点id
     * @return 若存在子结点id为给定值得结点，返回这个结点，否则返回null
     */
    public TreeNode<? extends TreeNodeBean> getChild(String id) {
        if (this.children==null) return null;
        TreeNode<? extends TreeNodeBean> retNode=null;
        for (int i=0; i<this.children.size(); i++) {
            TreeNode<? extends TreeNodeBean> tn=this.children.get(i);
            if (tn.getId().equals(id)) {
                retNode=tn;
                break;
            }
        }
        return retNode;
    }

    /**
     * 设置子结点列表。如果不允许有子结点，则抛出IllegalStateException异常。
     * @param children 子结点列表
     * @exception IllegalStateException 如果结点不允许有子结点
     */
    public void setChildren(List<TreeNode<? extends TreeNodeBean>> children) {
        if (!allowChildren) throw new Plat0003CException("本结点[id="+this.id+";nodeName="+this.nodeName+"]不允许有子结点！");
        if (children!=null) {
            for (TreeNode<? extends TreeNodeBean> tn: children) {
                tn.setParent(this);
            }
            this.children=children;
            if (this.children.size()>1) Collections.sort(this.children);
        }
    }

    /**
     * 为本结点列表增加一个子结点。<br>
     * 如果不允许有子结点，则抛出IllegalStateException异常。
     * @param child 新增加的子结点
     * @exception IllegalStateException 如果结点不允许有子结点
     */
    public void addChild(TreeNode<? extends TreeNodeBean> child) {
        if (child==null) return;
        if (!allowChildren) throw new Plat0003CException("本结点[id="+this.id+";nodeName="+this.nodeName+"]不允许有子结点！");
        if (this.children==null) this.children=new ArrayList<TreeNode<? extends TreeNodeBean>>();
        this.removeChild(child.getId());
        child.setParent(this);
        this.children.add(child);
        if (this.children.size()>1) Collections.sort(this.children);
    }

    /**
     * 为子结点列表追加一个子结点列表。<br>
     * 如果不允许有子结点，则抛出IllegalStateException异常。
     * @param children 新增加的结点列表
     * @exception IllegalStateException 如果结点不允许有子结点
     */
    public void appendChildren(List<TreeNode<? extends TreeNodeBean>> children) {
        if (!allowChildren) throw new Plat0003CException("本结点[id="+this.id+";nodeName="+this.nodeName+"]不允许有子结点！");
        if (this.children==null) this.children=new ArrayList<TreeNode<? extends TreeNodeBean>>();
        for (TreeNode<? extends TreeNodeBean> tn: children) {
            this.removeChild(tn.getId());
            tn.setParent(this);
        }
        this.children.addAll(children);
        if (this.children.size()>1) Collections.sort(this.children);
    }

    /**
     * 根据id，删除子结点
     * @param id 欲删除的子结点id
     * @return 若删除成功，返回被删除的结点，否则返回null
     */
    public TreeNode<? extends TreeNodeBean> removeChild(String id) {
        int removeIndex=-1;
        for (int i=0; i<this.children.size(); i++) {
            TreeNode<? extends TreeNodeBean> tn=this.children.get(i);
            if (tn.getId().equals(id)) {
                removeIndex=i;
                break;
            }
        }
        if (removeIndex==-1) return null;
        else {
            TreeNode<? extends TreeNodeBean> removeNode=this.children.remove(removeIndex);
            return removeNode;
        }
    }

    /**
     * 在所有下级结点中，查找id为给定值的结点
     * @param id 查找的id
     * @return 若找到返回该结点，否则返回null
     */
    public TreeNode<? extends TreeNodeBean> findNode(String id) {
        if (!allowChildren) return null;
        if (this.isLeaf()) return null;
        TreeNode<? extends TreeNodeBean> retNode=null;
        for (int i=0; i<this.children.size(); i++) {
            TreeNode<? extends TreeNodeBean> tn=this.children.get(i);
            if (tn.getId().equals(id)) {
                retNode=tn;
                break;
            }
        }
        if (retNode==null) {
            for (int i=0; i<this.children.size(); i++) {
                TreeNode<? extends TreeNodeBean> tn=this.children.get(i);
                if (!tn.isLeaf()) retNode=tn.findNode(id);
                if (retNode!=null) break;
            }
        }
        return retNode;
    }

    //结点属性
    protected Map<String, Object> attributes;

    /**
     * 得到本结点属性。<br/>
     * 注意:这里的属性还包括结点本身的属性，如ID，NODENAME等
     * @return 本结点的属性，把这些属性存储在Map结构中
     */
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    /**
     * 得到指定key对应的结点属性值。如果属性不包含key的映射关系，则返回 null。
     * @param key 属性的Key
     * @return key对应的结点属性值，如果无对应的<key, value>键值对，返回null。
     */
    public Object getAttribute(String key) {
        return this.attributes.get(key);
    }

    /**
     * 设置结点属性，属性的key为String，值为Object。
     * @param key 属性的Key
     * @param value 属性的值
     */
    public void setAttribute(String key, Object value) {
        if (key.equalsIgnoreCase("ID")) {
            if (value instanceof String) {
                if (!this.getId().equals(value)) this.setId((String)value);
            }
            else throw new Plat0003CException("当key为["+key+"]时,值的类型应为String类型");
        }
        if (key.equalsIgnoreCase("NODENAME")) {
            if (value instanceof String) {
                if (!this.getNodeName().equals(value)) this.setNodeName((String)value);
            }
            else throw new Plat0003CException("当key为["+key+"]时,值的类型应为String类型");
        }
        if (key.equalsIgnoreCase("PARENTID")) {
            if (value instanceof String) {
                if (!this.getParentId().equals(value)) this.setParentId((String)value);
            }
            else throw new Plat0003CException("当key为["+key+"]时,值的类型应为String类型");
        }
        if (this.attributes==null) this.attributes=new HashMap<String, Object>();
        this.attributes.put(key, value);
    }

    /**
     * 设置本结点属性。
     * @param attributes 属性
     */
    public void setAttributes(Map<String, Object> attributes) {
        Iterator<String> it= attributes.keySet().iterator();
        while (it.hasNext()) {
            String key=it.next();
            if (key.equalsIgnoreCase("ID")) {
                Object value=attributes.get(key);
                if (!(value instanceof String)) throw new Plat0003CException("当key为["+key+"]时,值的类型应为String类型");
            } else if (key.equalsIgnoreCase("NODENAME")) {
                Object value=attributes.get(key);
                if (!(value instanceof String)) throw new Plat0003CException("当key为["+key+"]时,值的类型应为String类型");
            } else if (key.equalsIgnoreCase("PARENTID")) {
                Object value=attributes.get(key);
                if (!(value instanceof String)) throw new Plat0003CException("当key为["+key+"]时,值的类型应为String类型");
            }
        }
        this.attributes=attributes;
    }

    /**
     * 追加本结点属性。
     * @param attributes 追加属性
     */
    public void appendAttributes(Map<String, Object> attributes) {
        Iterator<String> it= attributes.keySet().iterator();
        while (it.hasNext()) {
            String key=it.next();
            if (key.equalsIgnoreCase("ID")) {
                Object value=attributes.get(key);
                if (!(value instanceof String)) throw new Plat0003CException("当key为["+key+"]时,值的类型应为String类型");
            } else if (key.equalsIgnoreCase("NODENAME")) {
                Object value=attributes.get(key);
                if (!(value instanceof String)) throw new Plat0003CException("当key为["+key+"]时,值的类型应为String类型");
            } else if (key.equalsIgnoreCase("PARENTID")) {
                Object value=attributes.get(key);
                if (!(value instanceof String)) throw new Plat0003CException("当key为["+key+"]时,值的类型应为String类型");
            }
        }
        if (this.attributes==null) this.attributes=new HashMap<String, Object>();
        this.attributes.putAll(attributes);
    }

    /**
     * 删除key对应的结点属性值。
     * @param key 属性的Key
     */
    public void removeAttribute(String key) {
        this.attributes.remove(key);
    }

    //以下与排序有关
    //排序号
    protected int order;

    /**
     * 得到排序号
     * @return 排序号
     */
    public int getOrder() {
        return this.order;
    }

    /**
     * 设置排序号
     * @return 排序号
     */
    public void setOrder(int order) {
        this.order=order;
        //重新排序
        if (this.parent!=null&&this.parent.getChildren()!=null&&this.parent.getChildren().size()>1) {
            Collections.sort(this.parent.getChildren());
        }
        if (tnEntity!=null&&tnEntity.getOrder()!=order) tnEntity.setOrder(order);
        int oldOrder=-1;
        try {
            oldOrder=Integer.parseInt(this.getAttribute("order")+"");
        } catch(Exception e) {
        }
        if (oldOrder!=order) this.setAttribute("order", order);
    }

    //子结点列表的排序模式:若orderType=1从小到大排列，orderType=0从大到小排序，默认为0
    protected int orderType=0;

    /**
     * 得到子结点排序模式:若orderType=1从小到大排列，orderType=0从大到小排序
     * @return 子结点排序模式
     */
    public int getOrderType() {
        return this.orderType;
    }

    /**
     * 设置子结点排序模式
     * @param orderType 子结点排序模式：1从小到大排列，0从大到小排序
     */
    public void setOrderType(int orderType) {
        this.orderType=orderType;
    }

    /**
     * 排序方法
     */
    public int compareTo(TreeNode<? extends TreeNodeBean> o) {
        int _orderType=0;
        if (o.getParent()!=null) _orderType=o.getParent().getOrderType();
        return _orderType==0?(o.getOrder()-this.getOrder()):(this.getOrder()-o.getOrder());
    }

    /**
     * 得到深度树遍历中本结点的前序结点，在排序树中更有意义。<br/>
     * 前序结点：深度树遍历中，本结点的上一个结点——若本结点是其父结点的子结点的列表中非first结点，则返回的结点是子结点列表中序号减1的结点；若本结点是其父结点的子结点的列表中的first结点，则返回的结点是父结点；若本结点是根，则返回空，即根结点没有前序结点。<br/>
     * 若TreeNode!=root,则TreeNode.preNode()!=null，即若结点不是根，则必有前序结点；若TreeNode==root,则TreeNode.preNode()==null，即若结点是根，则必没有前序结点。
     * @return 前序结点
     */
    public TreeNode<? extends TreeNodeBean> preNode() {
        if (this.isRoot()) return null;
        int i=0;
        for (; i<this.getParent().getChildren().size()-1; i++) {
            TreeNode<? extends TreeNodeBean> n=this.getParent().getChildren().get(i);
            if (n.getId().equals(this.id)) break;
        }
        return (i==0?this.getParent():this.getParent().getChildren().get(i-1));
    }

    /**
     * 得到深度树遍历中本结点的后序结点，在排序树中更有意义。<br/>
     * 后序结点：深度树遍历中，本结点的下一个结点。<br/>
     * @return 后序结点
     */
    public TreeNode<? extends TreeNodeBean> nextNode() {
        if (this.isLeaf()) {
            if (this.isRoot()) return null;
            else {
                int i=0;
                TreeNode<? extends TreeNodeBean> p=this.getParent();
                if (p.getChildren().size()>1) {
                    for (;i<p.getChildren().size()-1; i++) {
                        TreeNode<? extends TreeNodeBean> n=p.getChildren().get(i);
                        if (n.getId().equals(this.id)) break;
                    }
                    if (i<p.getChildren().size()-1) {
                        return p.getChildren().get(i+1);
                    }
                }
                //向上查找
                String pid=p.getId();
                p=p.getParent();
                while (p!=null) {
                    for (i=0;i<p.getChildren().size()-1; i++) {
                        TreeNode<? extends TreeNodeBean> n=p.getChildren().get(i);
                        if (n.getId().equals(pid)) break;
                    }
                    if (i<p.getChildren().size()-1) {
                        return p.getChildren().get(i+1);
                    } else {
                        pid=p.getId();
                        p=p.getParent();
                    }
                }
                return null;
            }
        } else {
            return this.getChildren().get(0);
        }
    }

    /**
     * 深度克隆树，但不包括parent属性。克隆后的结点为根
     */
    @SuppressWarnings("unchecked")
    public TreeNode<? extends TreeNodeBean> clone() throws CloneNotSupportedException {
        TreeNode<T> cloneTn=new TreeNode<T>(tnEntity);
        cloneTn.setAttributes((HashMap<String, Object>)((HashMap<String, Object>)this.getAttributes()).clone());
        cloneTn.setParent(null);

        if (!this.isLeaf()) {
            for (TreeNode<? extends TreeNodeBean> tn: this.getChildren()) {
                TreeNode<? extends TreeNodeBean> cTn=tn.clone();
                cloneTn.addChild(cTn);
            }
        }

        return cloneTn;
    }

    /**
     * 按深度遍历，得到本结点以及所有下级结点的树对象列表
     * @return 树对象列表
     */
    public List<? extends TreeNodeBean> getAllBeansList() {
        List<TreeNodeBean> l=new ArrayList<TreeNodeBean>();
        l.add(this.getTnEntity());
        if (!this.isLeaf()) {
            for (TreeNode<? extends TreeNodeBean> tn: this.getChildren()) l.addAll(tn.getAllBeansList());
        }
        return l;
    }
}