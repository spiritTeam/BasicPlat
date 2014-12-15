package com.spiritdata.framework.component.module.pojo;

import com.spiritdata.framework.UGA.UgaModule;

/**
 * 模块类。
 */
public class Module extends UgaModule{
    private static final long serialVersionUID = -5024436009679818215L;
    private int levels;
    private int moduleType;

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
}