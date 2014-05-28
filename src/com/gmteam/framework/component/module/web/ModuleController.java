package com.gmteam.framework.component.module.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gmteam.framework.component.module.pojo.Module;
import com.gmteam.framework.component.module.service.ModuleService;
import com.gmteam.framework.core.model.tree.TreeNode;
import com.gmteam.framework.ui.tree.easyUi.EasyUiTree;

@Controller
public class ModuleController {

    @Resource
    private ModuleService moduleService;

    @RequestMapping("showAllTree.do")
    @ResponseBody
    public Map<String,Object> showAllTree() throws CloneNotSupportedException {
        Map<String,Object> map = new HashMap<String, Object>();
        TreeNode<Module> root = moduleService.getModuleRoot();
        EasyUiTree<Module> met = new EasyUiTree<Module>(root);
        for (TreeNode<Module> eut: met.getChildren()) {
            ((EasyUiTree<Module>)eut).setState("open");
        }
        map = met.toTreeMap();
        return map;
    }

    @RequestMapping("showAllTreeGrid.do")
    @ResponseBody
    public Map<String,Object> showAllTreeGrid() throws CloneNotSupportedException {
        Map<String,Object> map = new HashMap<String, Object>();
        TreeNode<Module> root = moduleService.getModuleRoot();
        EasyUiTree<Module> met = new EasyUiTree<Module>(root);
        for (TreeNode<Module> eut: met.getChildren()) {
            ((EasyUiTree<Module>)eut).setState("open");
        }
        map = met.toTreeGridMap();
        return map;
    }
}