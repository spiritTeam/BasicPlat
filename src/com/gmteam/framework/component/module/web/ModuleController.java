package com.gmteam.framework.component.module.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gmteam.framework.component.module.pojo.Module;
import com.gmteam.framework.component.module.service.ModuleService;
import com.gmteam.framework.model.tree.BaseTreeNode;
import com.gmteam.framework.model.tree.EasyUiTree;
import com.gmteam.framework.utils.JsonUtil;

@Controller
public class ModuleController {

    @Resource
    private ModuleService moduleService;

    @RequestMapping("test.do")
    public @ResponseBody
    BaseTreeNode test() {
        List<BaseTreeNode<Module>> root = moduleService.getRoot();
        BaseTreeNode<Module> node = root.get(0);
        //List<BaseTreeNode<Module>> roots = (List<BaseTreeNode<Module>>) toNollParentNode((List<TreeNode<Module>>)root);
//        EasyUiTree t = new EasyUiTree(node);
//        try {
//              String s = JsonUtil.beanToJson(t);
//              System.out.println(s);
//        } catch (JsonProcessingException e) {
//               TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        // int i=0;
        // for(BaseTreeNode<Module> node : root){
        // i++;
        // System.out.println(i);
        // }
        // List<Module> l = moduleService.getList();
        // Module m = l.get(0);
        // BaseTreeNode<Module> oneNode = new BaseTreeNode<Module>();
        // oneNode.setTnEntity(m);
        // for(Module m : l){
        // // System.out.print(m.getId()+"  ");
        // // System.out.print(m.getParentId()+"  ");
        // // System.out.println(m.getDisplayName());
        //
        // }
        // Test t = new Test();
        return node;

    }

//    public List<BaseTreeNode<Module>> toNollParentNode(List<BaseTreeNode<Module>> roots) {
//        for (TreeNode<Module> n : list) {
//            n.setParent(null);
//            if (n.getChildren().size() != 0) {
//                toNollParentNode(n.getChildren());
//            }
//        }
//        return (List<BaseTreeNode<Module>>)list;
//    }
}
