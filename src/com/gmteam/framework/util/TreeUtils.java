package com.gmteam.framework.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gmteam.framework.core.model.tree.TreeNode;
import com.gmteam.framework.core.model.tree.TreeNodeBean;

public abstract class TreeUtils {

    /**
     * 把以"树结点数据Bean"为元素的list转换为树对象
     * @param l 以"树结点数据Bean"为元素的list
     * @return 转换后的对象，是一个Map，有两个元素：<br/>
     * forest——符合id/pid关系的树结点的List，一个森林，实际是以TreeNode为元素的List{List<TreeNode<?>>}；
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
                else {
                    // 判断forest中有没有oneNode的子结点或结结点
                    mergeFlag=0;
                    for (int i=0; i<forest.size(); i++) {
                        TreeNode<TreeNodeBean> node = forest.get(i);
                        if (node.getId().equals(oneNode.getParentId())) {//判断oneNode是不是某个结点的子结点
                            node.addChild(oneNode);
                            mergeFlag=1;
                        } else if (node.findNode(oneNode.getParentId())!=null) {//判断oneNode是不是某个结点的子结点
                            node.findNode(oneNode.getParentId()).addChild(oneNode);
                            mergeFlag=1;
                        } else if (node.getParentId().equals(oneNode.getId())) {//判断oneNode是不是某个结点的父结点
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
            while (_fSize!=fSize&&fSize>1) {//n++;
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
                            } else if (_node.getParentId().equals(node.getId())) {//循环结点是标记结点的直接子结点
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
            //System.out.println("=================="+n);
            //处理完森林，下面从森林中，去除那些错误的结点，这里有一个默认的逻辑，pid=0的是正常的结点，这个在下一个版本修改吧
            for (int i=forest.size()-1; i>=0; i--) {
                TreeNode<TreeNodeBean> tn = forest.get(i);
                if (tn.getParentId()!=null&&!tn.getParentId().equals("0")) {
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
        if (treeIndexMap==null||treeIndexMap.size()==0) return;
        for (TreeNode<V> tn: forest) {
            treeIndexMap.put(tn.getId(), tn);
            if (!tn.isLeaf()) TreeUtils.setTreeIndexMap(tn.getChildren(), treeIndexMap);
        }
    }
}