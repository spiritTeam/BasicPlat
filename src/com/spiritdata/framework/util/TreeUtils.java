package com.spiritdata.framework.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.spiritdata.framework.core.model.tree.TreeNode;
import com.spiritdata.framework.core.model.tree.TreeNodeBean;

public abstract class TreeUtils {
    /**
     * 把以"树结点数据Bean"为元素的list转换为树对象
     * @param l 以"树结点数据Bean"为元素的list
     * @return 转换后的对象，是一个Map，有两个元素：<br/>
     * forest——符合id/pid关系的树结点的List，一个森林，实际是以TreeNode为元素的List{List<TreeNode<String>>}；符合的标准是，树的根结点的pid="0";
     * errors——不符合id/pid结构的树结点数据Bean的List，是错误的数据，实际是以TreeNodeBean为元素的List{List<TreeNodeBean>}；
     */
    public static Map<String, Object> convertFromList(List<? extends TreeNodeBean> l) {
        Map<String, Object> ret = null;
        if (l!=null&&l.size()>0) {
            ret = new HashMap<String, Object>();
            List<TreeNode<TreeNodeBean>> forest = new ArrayList<TreeNode<TreeNodeBean>>();
            List<TreeNodeBean> errors=null;
            int mergeFlag;
            //第一次对List处理，把List中的元素插入forest;
            for (TreeNodeBean tnb: l) {
                TreeNode<TreeNodeBean> oneNode = new TreeNode<TreeNodeBean>(tnb);
                if (forest.size() == 0) forest.add(oneNode);
                else {// 判断forest中有没有oneNode的子结点或父结点
                    mergeFlag=0;
                    for (int i=0; i<forest.size(); i++) {
                        TreeNode<TreeNodeBean> node = forest.get(i);
                        if (node.getId().equals(oneNode.getParentId())) {//判断oneNode是不是某个结点的子结点
                            node.addChild(oneNode);
                            mergeFlag=1;
                        } else if (node.findNode(oneNode.getParentId())!=null) {//判断oneNode是不是某个结点的子结点
                            node.findNode(oneNode.getParentId()).addChild(oneNode);
                            mergeFlag=1;
                        } else if (node.getParentId()!=null&&node.getParentId().equals(oneNode.getId())) {//判断oneNode是不是某个结点的父结点
                            oneNode.addChild(node);
                            forest.set(i, oneNode);
                            mergeFlag=2;
                        }
                        if (mergeFlag!=0) break;
                    }
                    if (mergeFlag==0) forest.add(oneNode);
                }
            }
            //对forest逐个处理
            int _fSize = 0, fSize = forest.size();
            //int n=0;
            while (_fSize!=fSize&&fSize>1) {
                fSize = forest.size();
                int index=0;
                TreeNode<TreeNodeBean> node = forest.get(index);//标记结点
                while (node!=null) {
                    int _index=0;
                    TreeNode<TreeNodeBean> _node = forest.get(_index);//循环结点
                    boolean eatNode=false;//是否吃掉标记结点
                    while (_node!=null) {
                        mergeFlag=-1;
                        if (!_node.getId().equals(node.getId())) {
                            if (_node.getId().equals(node.getParentId())) {//循环结点是标记结点的父节点
                                _node.addChild(node);
                                mergeFlag=1;
                                eatNode=true;
                            } else if (_node.findNode(node.getParentId())!=null) {//循环结点是标记结点的祖宗结点
                                _node.findNode(node.getParentId()).addChild(node);
                                mergeFlag=1;
                                eatNode=true;
                            } else if (_node.getParentId()!=null&&_node.getParentId().equals(node.getId())) {//循环结点是标记结点的直接子结点
                                node.addChild(_node);
                                forest.remove(_index);
                                mergeFlag=2;
                            } else if (node.findNode(_node.getParentId())!=null) {//循环结点是标记结点的后续子结点
                                node.findNode(_node.getParentId()).addChild(_node);
                                forest.remove(_index);
                                mergeFlag=2;
                            }
                        }
                        if (mergeFlag==1||mergeFlag==-1) _index++;
                        if (_index>forest.size()-1) _node=null;
                        else _node=forest.get(_index);
                    }
                    if (eatNode) {
                        forest.remove(index);
                    } else index++;
                    if (index>forest.size()-1) node=null;
                    else node=forest.get(index);
                }
                _fSize = forest.size();
            }

            //处理完森林，下面从森林中，去除那些错误的结点，这里有一个默认的逻辑，pid=0的是正常的结点，这个在下一个版本修改吧
            for (int i=forest.size()-1; i>=0; i--) {
                TreeNode<TreeNodeBean> tn = forest.get(i);
                if (tn.getParentId()!=null&&!tn.getParentId().trim().equals("0")) {
                    if (errors==null) errors = new ArrayList<TreeNodeBean>();
                    errors.addAll(forest.remove(i).getAllBeansList());
                }
            }
            ret.put("forest", forest);
            ret.put("errors", errors);
        }
        return ret;
    }

    /**
     * 设置树对象的以ID为索引的Map
     * @param forest 树对象的列表
     * @param treeIndexMap 所对应的索引Map
     */
    public static <V extends TreeNodeBean> void setTreeIndexMap(List<TreeNode<V>> forest, Map<String, TreeNode<V>> treeIndexMap) {
        if (forest==null||forest.size()==0) return;
        for (TreeNode<V> tn: forest) {
            treeIndexMap.put(tn.getId(), tn);
            if (!tn.isLeaf()) TreeUtils.setTreeIndexMap(tn.getChildren(), treeIndexMap);
        }
    }

    /**
     * 重组树，根据idc中的结点id，重组forest中的树，新的树包括仅包括idc中所有id结点的所有祖宗结点和子结点
     * @param forest 树对象的列表，此对象是需要重组树的全集
     * @param idc 结点id的Collection
     * @return 重组后的树，此树是原树的一个重组后的拷贝
     * @throws CloneNotSupportedException
     */
    public static <V extends TreeNodeBean> List<TreeNode<V>> restructureTree(List<TreeNode<V>> forest, Collection<String> idc) throws CloneNotSupportedException {
        if (forest==null||forest.size()==0) return null;
        if (idc==null||idc.size()==0) return null;
        List<TreeNode<V>> restructureForest = new ArrayList<TreeNode<V>>();
        idc = TreeUtils.distinctList(idc);
        for (String id: idc) {
            for (TreeNode<V> tn: forest) {
                TreeNode<V> _ftn = tn.getId().equals(id.trim())?tn:tn.findNode(id.trim());//在原树中查找是否有id结点
                if (_ftn!=null) {//若有
                    TreeNode<V> ctn = _ftn.clone();//以此id为根的树的克隆
                    boolean hasDeal = false;
                    for (TreeNode<V> rn: restructureForest) {
                        TreeNode<V> _rftn = rn.findNode(ctn.getId());
                        hasDeal = (_rftn!=null);
                    }
                    while (!hasDeal) {
                        if (!_ftn.isRoot()) _ftn=_ftn.getParent();
                        else {
                            restructureForest.add(ctn);
                            hasDeal = true;
                        }
                        if (!hasDeal) {
                            for (TreeNode<V> rn: restructureForest) {//查找合适的位置插入
                                TreeNode<V> _rftn = rn.getId().equals(_ftn.getId())?rn:rn.findNode(_ftn.getId());
                                if (_rftn!=null) {
                                    _rftn.addChild(ctn);
                                    hasDeal = true;
                                }
                            }
                        }
                        if (!hasDeal) {
                            //造上级结点
                            TreeNode<V> _ptn = new TreeNode<V>(_ftn.getTnEntity());
                            _ptn.addChild(ctn);
                            ctn=_ptn;
                        }
                    }
                }
            }
        }
        return restructureForest;
    }

    /**
     * 给list消重
     * @param sl 要消重的list
     * @return 消重后的list
     */
    private static Collection<String> distinctList(Collection<String> sl) {
        Collection<String> rl = new ArrayList<String>();
        for (String o: sl) {
            boolean isAdd = true;
            for (String _o: rl) {
                if (_o.equals(o)) {
                    isAdd = false;
                    break;
                }
            }
            if (isAdd) rl.add(o);
        }
        return rl;
    }
}
