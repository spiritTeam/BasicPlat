package com.gmteam.framework.component.module.web;

import java.util.ArrayList;
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
import com.gmteam.framework.IConstants;
import com.gmteam.framework.component.cache.FrameworkCacheLifecycleUnit;
import com.gmteam.framework.component.module.pojo.Module;
import com.gmteam.framework.component.module.service.ModuleService;
import com.gmteam.framework.core.model.tree.TreeNode;
import com.gmteam.framework.ui.tree.easyUi.EasyUiTree;

@Controller
public class ModuleController {
    @Resource
    private ModuleService moduleService;
    @Resource
    private FrameworkCacheLifecycleUnit fCLU;
    /**
     * tree
     * @return
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
     * treegrid
     * @return
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
    @RequestMapping(value="getPNameMap.do",method = RequestMethod.POST)
    public@ResponseBody List<String> getPNameMap(){
        List<String> reqJson = new ArrayList<String>();
        StringBuffer sb = new StringBuffer();
        List<Module> mList = moduleService.getModuleList();
        Module mm=null;
        for(int i=0;i<mList.size();i++){
            mm=mList.get(i);
            if(i<mList.size()-1){
                sb.append("{id:"+mm.getId()+","+"text:"+mm.getDisplayName()+"},");
            }else{
                sb.append("{id:"+mm.getId()+","+"text:"+mm.getDisplayName()+"}");
            }
        }
        reqJson.add(sb.toString());
        return reqJson;
    }
    /**
     * 刷新缓存
     */
    @RequestMapping(value="refreshManager.do",method = RequestMethod.POST)
    public @ResponseBody Map<String,Object> refreshModule(){
        try{
            fCLU.refresh(IConstants.CATCH_MODULE);
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
     * insert
     * @param formTree
     * @return
     */
    @RequestMapping(value="insertModule.do",method = RequestMethod.POST)
    public @ResponseBody int insertModule(@ModelAttribute("formModule") Module formModule,HttpServletRequest req){
        Module m =formModule;
        String mName = req.getParameter("moduleName");
        m.setNodeName(mName);
        UUID uuid = UUID.randomUUID();
//        List<Module> lM = moduleService.getModuleList();
//        for(Module mm :lM){
//            if(m.getpName().equals(mm.getDisplayName())){
//                m.setParentId(mm.getId());
//            }
//        }
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
    /**
     * update
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
     * delete
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
