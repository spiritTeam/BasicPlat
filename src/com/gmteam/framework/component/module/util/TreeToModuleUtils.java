package com.gmteam.framework.component.module.util;

import java.util.List;

import com.gmteam.framework.component.module.pojo.Module;
import com.gmteam.framework.ui.tree.easyUi.EasyUiTree;
/** 
 * 把一个新的节点转化成一个module类
 * @author mht
 * @version  
 * 类说明 
 */
public abstract class TreeToModuleUtils {
    /**
     * for insert
     * @param eut
     * @return
     */
    public static Module treeToModule(EasyUiTree eut){
       Module m = new Module();
//       String pName = eut.getParentNodeName();
//       List<Module> sourceList = ModuleCacheUtils.getSourceList();
//       for(Module mSource :sourceList){
//           if(pName.equals(mSource.getModuleName())){
//               m.setParentId(mSource.getId());
//               m.setLevels(mSource.getLevels()+1);
//           }
//       }
//       m.setDisplayName(eut.getText());
//       m.setDescn(eut.getDescn());
//       m.setIcon(eut.getIconCss());
//       m.setSorts(0);
//       m.setHasChild(2);
//       m.setIsvalidate(1);
//       m.setIsvalidate(1);
//       m.setModuleName(eut.getText());
//       m.setTypes(2);
//       m.setUrl(eut.getUrl());
       return m;
    }
    /**
     * for update
     * 只准许修改url，name，descn，text，icon
     * @param eut
     * @return
     */
//    public static Module treeToModuleUpdate(EasyUiTree eut){
//        Module m = new Module();
//        String eutId = eut.getId();
//        System.out.println(eutId);
//        List<Module> sourceList = ModuleCacheUtils.getSourceList();
//        for(Module mSource :sourceList){
//            if(eutId.equals(mSource.getId())){
//               mSource.setDisplayName(eut.getText());
//               mSource.setModuleName(eut.getText());
//               mSource.setUrl(eut.getUrl());
//               mSource.setDescn(eut.getDescn());
//               mSource.setIcon(eut.getIconCss());
//               m = mSource;
//            }
//        }
//        return m;
//    }
}
