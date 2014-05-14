package com.gmteam.framework.core.cache.pojo;

import com.gmteam.framework.core.model.BaseObject;
/**
 * 模块管理类
 */
public class PlatModule extends BaseObject{
    private static final long serialVersionUID = 1L;
    private int id;//模块id
    private int pId;//父id，默认0(第一级用户组)
    private int hasChild;//默认值2无子,1有结点,2无子结点
    private int levels;//默认值1无子  从1开始
    private int sort;//默认值0  数值大者靠前，从0-999999
    private int isValidate;//默认值1有效  1有效；2无效这里作为是否显示的意义
    private int types;//默认值2用户定义模块  1：系统模块，2：用户定义模块
    private String modeuleName;//模块名
    private String displayName;//显示名 界面显示的名称
    private String url;//链接--相对于上下文环境的路径
    private String style;//样式(不知道是什么)
    private String icon;//图片--相对于上下文环境的路径
    private String descn;//说明
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getpId() {
        return pId;
    }
    public void setpId(int pId) {
        this.pId = pId;
    }
    public int getHasChild() {
        return hasChild;
    }
    public void setHasChild(int hasChild) {
        this.hasChild = hasChild;
    }
    public int getLevels() {
        return levels;
    }
    public void setLevels(int levels) {
        this.levels = levels;
    }
    public int getSort() {
        return sort;
    }
    public void setSort(int sort) {
        this.sort = sort;
    }
    public int getIsValidate() {
        return isValidate;
    }
    public void setIsValidate(int isValidate) {
        this.isValidate = isValidate;
    }
    public int getTypes() {
        return types;
    }
    public void setTypes(int types) {
        this.types = types;
    }
    public String getModeuleName() {
        return modeuleName;
    }
    public void setModeuleName(String modeuleName) {
        this.modeuleName = modeuleName;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getStyle() {
        return style;
    }
    public void setStyle(String style) {
        this.style = style;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public String getDescn() {
        return descn;
    }
    public void setDescn(String descn) {
        this.descn = descn;
    }
    
}
