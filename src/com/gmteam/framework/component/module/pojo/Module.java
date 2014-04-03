package com.gmteam.framework.component.module.pojo;

import com.gmteam.framework.model.tree.TreeNodeModel;

public class Module extends TreeNodeModel{
    /**
     * 
     */
    private static final long serialVersionUID = -5024436009679818215L;


    private String title;

    private Integer hasChild;

    private Integer levels;

    private Integer sorts;

    private Integer isvalidate;

    private Integer types;

    private String moduleName;

    private String displayName;

    private String url;

    private String style;

    private String icon;

    private String descn;


    
    public String getTitle() {
        return displayName;
    }

    public void setTitle(String title) {
        this.title = title;
    }
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName == null ? null : displayName.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style == null ? null : style.trim();
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

    public String getDescn() {
        return descn;
    }

    public void setDescn(String descn) {
        this.descn = descn == null ? null : descn.trim();
    }
}

