package com.gmteam.framework.component.cache;

import javax.annotation.Resource;

import com.gmteam.framework.component.module.service.ModuleService;
import com.gmteam.framework.core.cache.AbstractCacheLifecycleUnit;

public class FrameworkCacheLifecycleUnit extends AbstractCacheLifecycleUnit {

    @Resource
    private ModuleService moduleService;

    @Override
    public void init() {
        System.out.println("ABC");
    }

    @Override
    public void refresh(String key) {
        
    }
}