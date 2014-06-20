package com.gmteam.framework.component.module.pojo;

import com.gmteam.framework.UGA.UgaModule;

/**
 * 模块类。
 */
public class Module extends UgaModule{
    private static final long serialVersionUID = -5024436009679818215L;
    private int levels;
    private int moduleType;
    private String descn;

    public int getLevels() {
        return levels;
    }
    public void setLevels(int levels) {
        this.levels = levels;
    }

    public int getModuleType() {
        return moduleType;
    }
    public void setModuleType(int moduleType) {
        this.moduleType = moduleType;
    }

    public String getDescn() {
        return descn;
    }
    public void setDescn(String descn) {
        this.descn = descn;
    }
}