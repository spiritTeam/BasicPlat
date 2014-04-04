package com.gmteam.framework.component.module.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gmteam.framework.component.module.pojo.Module;
import com.gmteam.framework.component.module.service.ModuleService;
import com.gmteam.framework.model.tree.BaseTreeNode;
import com.gmteam.framework.model.tree.ui.EasyUiTree;

@Controller
public class ModuleController {

    @Resource
    private ModuleService moduleService;

    @RequestMapping("test.do")
    @ResponseBody
    public Map<String,Object>  test() {
        Map<String,Object> map = new HashMap<String, Object>();
        List<BaseTreeNode<Module>> root = moduleService.getRoot();
        BaseTreeNode<Module> node = root.get(0);
        EasyUiTree t = new EasyUiTree(node);
        EasyUiTree s = new EasyUiTree();
        s.setChildren(new ArrayList<EasyUiTree>());
        s.getChildren().add(t);
        map.put("isPass", true);
        map.put("msg","登陆成功");
        map.put("type", 1);
        System.out.println(t.getId().equals("1"));
        map.put("data", s);
        return map;
    }
}
