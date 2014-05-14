package com.gmteam.framework.component.module.pojo;

import com.gmteam.framework.core.model.tree.TreeNodeModel;
/**
 * 
 */
public class Module extends TreeNodeModel{
    private static final long serialVersionUID = -5024436009679818215L;

    private Integer hasChild;

    private Integer levels;

    private Integer sorts;

    private Integer isvalidate;

    private Integer types;

    private String moduleName;

    private String style;

    public Integer getHasChild() {
        return hasChild;
    }

    public void setHasChild(Integer hasChild) {
        this.hasChild = hasChild;
    }

    public Integer getLevels() {
        return levels;
    }

    public void setLevels(Integer levels) {
        this.levels = levels;
    }

    public Integer getSorts() {
        return sorts;
    }

    public void setSorts(Integer sorts) {
        this.sorts = sorts;
    }

    public Integer getIsvalidate() {
        return isvalidate;
    }

    public void setIsvalidate(Integer isvalidate) {
        this.isvalidate = isvalidate;
    }

    public Integer getTypes() {
        return types;
    }

    public void setTypes(Integer types) {
        this.types = types;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName == null ? null : moduleName.trim();
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style == null ? null : style.trim();
    }
}

