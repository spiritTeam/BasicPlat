package com.spiritdata.framework.ext.redis;

import java.util.Map;

public abstract class GetBizData {
    private Map<String, Object> param; //参数，可以为空
    public GetBizData(Map<String, Object> param) {
        this.param=param;
    }
    public String getBizData() {
        return _getBizData(param);
    }
    public abstract String _getBizData(Map<String, Object> param);
}