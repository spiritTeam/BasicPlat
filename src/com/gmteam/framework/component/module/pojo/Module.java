package com.gmteam.framework.component.module.pojo;

import com.gmteam.framework.core.model.tree.TreeNodeBean;
/**
 * 模块类，由于模块是树型数据，所以继承TreeNodeBean
 */
public class Module extends TreeNodeBean{
    private static final long serialVersionUID = -5024436009679818215L;
    private String displayName;
    private int levels;
    private String pName;
    private int isValidate;
    private int moduleType;
    private int style;
    private String icon;
    private String url;
    private String descn;
    public String getpName() {
        return pName;
    }
    public void setpName(String pName) {
        this.pName = pName;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public int getLevels() {
        return levels;
    }
    public void setLevels(int levels) {
        this.levels = levels;
    }
    public int getIsValidate() {
        return isValidate;
    }
    public void setIsValidate(int isValidate) {
        this.isValidate = isValidate;
    }
    public int getModuleType() {
        return moduleType;
    }
    public void setModuleType(int moduleType) {
        this.moduleType = moduleType;
    }
    public int getStyle() {
        return style;
    }
    public void setStyle(int style) {
        this.style = style;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getDescn() {
        return descn;
    }
    public void setDescn(String descn) {
        this.descn = descn;
    }
}