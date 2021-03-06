package com.spiritdata.framework.core.model;

/**
 * 概念/逻辑对象 和 持久化对象 相互转化的接口<br/>
 * 前列建议实现此接口用以实现两层对象之间的转化，若是：<br/>
 * 规定：此接口应在Model类中实现，也就是放在Model层。<br/>
 * 这样会保持"持久化层对象"的干净，之所以这样规定，主要是考虑到持久化对象可能用于传输和存储在内存中。<br/>
 * 注意：逻辑对象不一定能和持久化对象一对一的转化，应视逻辑层和持久化层的设计确定是否要实现这一接口
 * @author wh
 */
public interface ModelSwapPo {
    /**
     * 将逻辑对象转换为持久化对象<br/>
     * 一般情况：<br/>
     * 逻辑对象要继承Serializable, Model2Po接口；<br/>
     * 持久化对象要继承BaseObject类。
     * @return 对应的持久化对象
     */
    public Object convert2Po(); 

    /**
     * 根据持久化对象构建逻辑对象，构建结果存储在本身中(this中)<br/>
     * 一般情况：<br/>
     * 逻辑对象要继承Serializable, Model2Po接口，若需要也可继承ModelFromPo接口；<br/>
     * 持久化对象要继承BaseObject类<br/>
     * <p>由于逻辑对象和持久化对象不一定一一对应(包括类及类中的属性)，若使用此方法构建逻辑对象，可能只能给某些属性赋值。
     * <p>用例：<br/>
     * <pre>
     *   Model m = new Model();
     *   m.buildFromPo(po);
     * </pre>
     * @param po 持久化对象，或由持久化对象组成的一个结构数据，比如一个Map结构
     */
    public void buildFromPo(Object po);
}