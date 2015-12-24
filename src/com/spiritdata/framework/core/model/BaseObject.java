package com.spiritdata.framework.core.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.log4j.Logger;
import com.spiritdata.framework.util.ReflectUtils;

/**
 * 所有pojo的基类
 * 实现序列化接口,提供bean2map的转换
 * @author zhuhua
 */
@SuppressWarnings("serial")
public abstract class BaseObject implements Serializable {
    transient protected Logger log = Logger.getLogger(this.getClass());

    /**
     * 将java中不为空的属性放入HashMap
     * @return Map
     */
    public Map<String, Object> toHashMap() {
        Map<String, Object> propertiesMap = new HashMap<String, Object>();
        try {
            Class<? extends BaseObject> clazz = this.getClass();
            propertiesMap = ReflectUtils.Object2MapWithoutNull(clazz, this);
        } catch(Exception e) {
            log.info("转换类"+this.getClass().getName()+"实例为HASHMAP失败",e);
            propertiesMap = null;
        }
        return propertiesMap;
    }

    /**
     * 将java中不为空的属性放入HashMap
     * @return Map
     */
    public Map<String, Object> toHashMapAsBean() {
        Map<String, Object> propertiesMap = new HashMap<String, Object>();
        try {
            Class<? extends BaseObject> clazz = this.getClass();
            propertiesMap = ReflectUtils.Bean2MapWithoutNull(clazz, this);
        } catch(Exception e) {
            log.info("转换类"+this.getClass().getName()+"实例为HASHMAP失败",e);
            propertiesMap = null;
        }
        return propertiesMap;
    }

    /**
     * 以Map为数据源，设置类实例的各属性
     * @param propertiesMap Map源数据
     */
    public void fromHashMap(Map<String, Object> propertiesMap) {
        try {
            if(propertiesMap == null || propertiesMap.size() == 0) return;
            Class<? extends Object> clazz = this.getClass();
            Field[]  fields;
            Iterator<String> it;
            String propertyName;
            while (!clazz.getName().equals(BaseObject.class.getName())&&!clazz.getName().equals(Object.class.getName())) {
                fields =clazz.getDeclaredFields();
                it =  propertiesMap.keySet().iterator();
                while (it.hasNext()) {
                    propertyName = it.next();
                    for (Field f: fields) {
                        if(propertyName.equalsIgnoreCase(f.getName())) {
                            f.setAccessible(true);
                            //此处有可能因变量的类型不一致导致set值错误，需要对异常作捕捉，以不影响其他属性
                            try {
                                if (f.getType().getName().equals("int")) {
                                    f.set(this, Integer.parseInt(""+propertiesMap.get(propertyName)));
                                } else {
                                    f.set(this, propertiesMap.get(propertyName));
                                }
                            } catch(Exception e) {
                              log.error("转换类"+this.getClass().getName()+"实例的"+propertyName+"属性出现类型不匹配错误！", e);
                            }
                        }
                    }
                }
                clazz=clazz.getSuperclass();
            }
        }  catch(Exception e) {
            log.info("转换HASHMAP"+this.getClass().getName()+"为类实例失败",e);
        }
    }
}