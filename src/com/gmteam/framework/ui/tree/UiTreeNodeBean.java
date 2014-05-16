package com.gmteam.framework.ui.tree;

import java.util.Map;

import com.gmteam.framework.core.model.tree.TreeNodeBean;

/**
 * 显示树结点数据的抽象类。需要提供由TreeNodeModel向此类转换的方法。
 * <p>显示树由两部分构成：1字段属性；2扩展属性，下面以一段json数据来做说明：<br/>
 * 
 * @author wh
 * @see com.gmteam.framework.core.model.tree.TreeNodeBean
 * @since 0.1
 */
@SuppressWarnings("serial")
public abstract class UiTreeNodeBean extends TreeNodeBean{
    /**
     * 由TreeNodeModel转换为UiTreeNodeModel
     * @param tnm TreeNodeModel
     * @return UiTreeNodeModel
     */
    public abstract <U extends UiTreeNodeBean, TM extends TreeNodeBean> U convertFrom(TM tnm); 

    /**
     * 转换为字段属性
     * @return 字段属性Map
     */
    public abstract Map<String, Object> convert4Field();

    /**
     * 转换为扩展属性。
     * @return 扩展属性Map
     */
    public abstract Map<String, Object> convert4Attributes();

    /**
     * 转换为TreeGrid中的Grid行记录
     * @return Grid行记录
     */
    public abstract Map<String, Object> convert4TreeGrid();
}