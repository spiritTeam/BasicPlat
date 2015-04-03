package com.spiritdata.framework.core.model;

/**
 * 概念/逻辑对象 和 持久化对象 相互转化的接口<br/>
 * 前列建议实现此接口用以实现两层对象之间的转化，若是：<br/>
 * 规定：此接口应在Model类中实现，也就是放在Model层。<br/>
 * 这样会保持"持久化层对象"的干净，之所以这样规定，主要是考虑到持久化对象可能用于传输和存储在内存中。
 * @author wh
 */
public interface ModelSwapPo {
    /**
     * 将逻辑对象转换为持久化对象<br/>
     * 一般情况：<br/>
     * 逻辑对象要继承Serializable, Model2Po接口；<br/>
     * 持久化对象要继承BaseObject类
     * @return 对应的持久化对象
     */
    public Object convert2Po(); 

    /**
     * 从持久化对象得到逻辑对象<br/>
     * 一般情况：<br/>
     * 逻辑对象要继承Serializable, Model2Po接口，若需要也可继承ModelFromPo接口；<br/>
     * 持久化对象要继承BaseObject类
     * @param po 持久化对象
     * @return 逻辑对象
     */
    public Object getFromPo(Object po);
}