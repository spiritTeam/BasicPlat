package com.spiritdata.framework.component.demo.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spiritdata.framework.component.module.pojo.Module;
import com.spiritdata.framework.component.module.service.ModuleService;
import com.spiritdata.framework.core.model.tree.TreeNode;
import com.spiritdata.framework.ui.tree.easyUi.EasyUiTree;
import com.spiritdata.framework.util.StringUtils;
import com.spiritdata.framework.util.TreeUtils;

@Controller
public class TestTreeController {

    @Resource
    private ModuleService moduleService;

    @RequestMapping("testRestructureTree.do")
    @ResponseBody
    public Map<String,Object> showAllTree(HttpServletRequest request) throws CloneNotSupportedException {
        Map<String,Object> map = new HashMap<String, Object>();
        TreeNode<Module> root = moduleService.getModuleRoot();
        String s = request.getParameter("ids");
        List<TreeNode<Module>> rts = TreeUtils.restructureTree(root.getChildren(), StringUtils.strConvertList(s, ","));
        if (rts!=null&&rts.size()>0) {
            EasyUiTree<Module> met = new EasyUiTree<Module>(rts.get(0));
            for (TreeNode<Module> eut: met.getChildren()) {
                ((EasyUiTree<Module>)eut).setState("open");
            }
            map = met.toTreeGridMap();
        }
        return map;
    }
}