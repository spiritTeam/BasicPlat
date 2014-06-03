package com.gmteam.framework.component.module.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
        setParentName(met);
        map = met.toTreeGridMap();
        return map;
    }
    private void setParentName(EasyUiTree<Module> e) {
        if (e.isRoot()) e.setAttribute("parentName", "模块");
        else e.setAttribute("parentName", e.getParent().getNodeName());
        if (!e.isLeaf()) {
            for (TreeNode<?> t: e.getChildren()) {
                EasyUiTree<Module> _e = (EasyUiTree<Module>)t;
                setParentName(_e);
            }
        }
    }
    /**
     * 插入
     * @param formTree
     * @return
     */
    @RequestMapping(value="insertModule.do",method = RequestMethod.POST)
    public @ResponseBody int insertModule(@ModelAttribute("formModule") Module formModule){
        Module m =formModule;
        UUID uuid = UUID.randomUUID();
        List<Module> lM = moduleService.getModuleList();
        for(Module mm :lM){
            if(m.getpName().equals(mm.getDisplayName())){
                m.setParentId(mm.getId());
            }
        }
        m.setId(uuid+"");
        int rsp = 0;
        try {
            rsp = moduleService.insertModule(m);
            return rsp;
        } catch (Exception e) {
            e.printStackTrace();
            return rsp;
        }
    }
    @RequestMapping(value="deleteModule.do",method = RequestMethod.POST)
    public @ResponseBody int deleteModule(HttpServletRequest req){
        String id = req.getParameter("nodeId");
        int rsp = 0;
        try {
            rsp = moduleService.deleteModule(id);
            return rsp;
        } catch (Exception e) {
            e.printStackTrace();
            return rsp;
        }
    }
}
