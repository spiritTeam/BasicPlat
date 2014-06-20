package com.gmteam.framework.UGA;

import com.gmteam.framework.core.model.tree.TreeNodeBean;

/**
 * UGA框架。
 * 模块对象基础类，此类是树结点数据的子类，模块需继承此类。否则本框架登录会出现问题。
 * 模块必须要符合树的要求。
 * 需注意：采用树后，模块的显示名称要和树的nodeName相匹配。本类中的moduleName是模块的英文名称
 * @author wanghui
 */
@SuppressWarnings("serial")
public abstract class UgaModule extends TreeNodeBean{
    private String moduleName;
    private int isValidate;
    private int style;
    private String icon;
    private String url;

    /**
     * 得到模块名称
     * @return 模块名称
     */
    public String getModuleName() {
        return moduleName;
    }
    /**
     * 设置模块名称
     * @param displayName 模块名称
     */
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    /**
     * 得到是否生效，生效的模块才参与处理，并加以显示
     * @return 是否生效
     */
    public int getIsValidate() {
        return isValidate;
    }
    /**
     * 设置是否生效
     * @param isValidate 是否生小
     */
    public void setIsValidate(int isValidate) {
        this.isValidate = isValidate;
    }

    /**
     * 得到模块样式：1是Mis风格，2是Gis风格，3是自定义-必须要有相应的Url配合；注意：样式设置，只对第一级模块起作用
     * @return 模块样式
     */
    public int getStyle() {
        return style;
    }
    /**
     * 设置模块样式：1是Mis风格，2是Gis风格，3是自定义-必须要有相应的Url配合；注意：样式设置，只对第一级模块起作用
     * @param style 模块样式
     */
    public void setStyle(int style) {
        this.style = style;
    }

    /**
     * 得到模块图标图片的Url，这个参数和前台框架相对应，若不设置，则不显示图标
     * @return 模块图标图片的Url
     */
    public String getIcon() {
        return icon;
    }
    /**
     * 设置模块图标图片的Url：这个参数和前台框架相对应，若不设置，则不显示图标
     * @param icon 模块图标图片的Url
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * 得到模块的Url，注意：Url若不是完全的Url，则按照项对Url处理
     * @return 模块的Url
     */
    public String getUrl() {
        return url;
    }
    /**
     * 设置模块的Url
     * @param url 模块的Url
     */
    public void setUrl(String url) {
        this.url = url;
    }
}