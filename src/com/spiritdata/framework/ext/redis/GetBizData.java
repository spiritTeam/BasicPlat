package com.spiritdata.framework.ext.redis;

import java.util.Map;

public abstract class GetBizData {
    protected Map<String, Object> param; //参数，可以为空
    public GetBizData(Map<String, Object> param) {
        this.param=param;
    }
    public abstract Object getBizData();
}