package com.spiritdata.framework.component.module.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spiritdata.framework.UGA.UgaConstants;
import com.spiritdata.framework.component.cache.FrameworkCLU;
import com.spiritdata.framework.component.module.pojo.Module;
import com.spiritdata.framework.component.module.service.ModuleService;
import com.spiritdata.framework.core.model.tree.TreeNode;
import com.spiritdata.framework.ui.tree.easyUi.EasyUiTree;
import com.spiritdata.framework.util.SequenceUUID;

/**
 * moduelController:
 * 包含tree的显示方法，TreeGrid的显示方法
 * @author mht，wh
 */
public class ModuleController {
    @Resource
    private ModuleService moduleService;
    @Resource
    private FrameworkCLU fCLU;
    /**
     * tree的显示
     * @throws CloneNotSupportedException
     */
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
    /**
     * treegrid的显示
     * @throws CloneNotSupportedException
     */
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
    /**
     * 刷新缓存
     */
    @RequestMapping(value="refreshManager.do",method = RequestMethod.POST)
    public @ResponseBody Map<String,Object> refreshModule(){
        try{
            fCLU.refresh(UgaConstants.CATCH_UGA_MODULE);
            Map<String,Object> map = new HashMap<String, Object>();
            TreeNode<Module> root = moduleService.getModuleRoot();
            EasyUiTree<Module> met = new EasyUiTree<Module>(root);
            for (TreeNode<Module> eut: met.getChildren()) {
                ((EasyUiTree<Module>)eut).setState("open");
            }
            setParentName(met);
            map = met.toTreeGridMap();
            return map;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 为每个节点绑定pName
     * @param e
     */
    @SuppressWarnings("unchecked")
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
     * insert一个module
     * @param formTree
     * @return
     */
    @RequestMapping(value="insertModule.do",method = RequestMethod.POST)
    public @ResponseBody int insertModule(@ModelAttribute("formModule") Module formModule,HttpServletRequest req){
        Module m =formModule;
        String mName = req.getParameter("moduleName");
        m.setNodeName(mName);
        m.setId(SequenceUUID.getPureUUID());
        int rsp = 0;
        try {
            rsp = moduleService.insertModule(m);
            return rsp;
        } catch (Exception e) {
            e.printStackTrace();
            return rsp;
        }
    }
    /**
     * update一个module
     * @param formModule
     * @param req
     * @return
     */
    @RequestMapping(value="updateModule.do",method=RequestMethod.POST)
    public @ResponseBody int updateModule(@ModelAttribute("formModule")Module formModule,HttpServletRequest req){
        Module m = formModule;
        String mName = req.getParameter("moduleName");
        m.setNodeName(mName);
        try {
            moduleService.updateModule(m);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    /**
     * delete一个module
     * @param req
     * @return
     */
    @RequestMapping(value="deleteModule.do",method = RequestMethod.POST)
    public @ResponseBody int deleteModule(HttpServletRequest req){
        String id = req.getParameter("nodeId");
        try {
            moduleService.deleteModule(id);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 2;
        }
    }
}
