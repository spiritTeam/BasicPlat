package com.gmteam.framework.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * 反射工具类
 * @author zhuhua wh
 */
public abstract class ReflectUtils {
    private final static Logger logger = Logger.getLogger(ReflectUtils.class);
    private final static String[] classList= {"java.lang.Integer", "java.lang.Short", "java.lang.Long"
        ,"java.lang.Double", "java.lang.Float", "java.lang.Byte", "java.lang.Character", "java.lang.Boolean"
        ,"java.lang.String", "java.util.Date", "java.util.Calendar", "java.lang.StringBuffer"
        ,"java.math.BigDecimal", "java.math.BigInteger"};
    private final static String[] baseClass= {"int", "short", "long", "double", "float", "byte", "char", "boolean"};
    
    /**
     * 可以绕过java的访问控制,为私有成员变量赋值
     * @param target 目标对象，这个类的一个实例
     * @param fname 变量名称
     * @param ftype 变量类型
     * @param fvalue 变量的值
     */
    public static void setFieldValue(Object target, String fname, Class<?> ftype, Object fvalue) {
        if (target==null || fname==null || "".equals(fname)
          ||(fvalue!=null&&!ftype.isAssignableFrom(fvalue.getClass())))  return;

        Class<?> clazz = target.getClass();
        try {
            Method method = clazz.getDeclaredMethod("set"+Character.toUpperCase(fname.charAt(0))+fname.substring(1), ftype);
            if (!Modifier.isPublic(method.getModifiers())) {
                method.setAccessible(true);
            }
            method.invoke(target, fvalue);
        } catch (Exception me) {
            logger.debug("赋值错误", me);
            try {
                Field field = clazz.getDeclaredField(fname);
                if (!Modifier.isPublic(field.getModifiers())) {
                    field.setAccessible(true);
                }
                field.set(target, fvalue);
            } catch (Exception e) {
                logger.debug(e);
            }
        }
    }

    /**
     * 得到类的指定属性的值,如果是继承自父类的属性,则递归调用得到父类的属性的值
     * @param clazz 目标类
     * @param property 目标属性
     * @param target 对象实例
     * @return Object 二元数组：Object[0]=值是一个Object；Object[0]=值是一个Object；
     */
    public static Object[] getFieldValue(Class<?> clazz, String property, Object target) {
        Object objValue[] = new Object[2];
        try {
            Field field = clazz.getDeclaredField(property);
            field.setAccessible(true);
            Class<?> propertyClass = field.getType();
            Object value = field.get(target);
            objValue[0] = value;
            objValue[1] = propertyClass;
        } catch (NoSuchFieldException e) {
            objValue = getFieldValue(clazz.getSuperclass(), property, target);
        } catch (IllegalArgumentException e) {
            logger.debug(e);
        } catch (IllegalAccessException e) {
            logger.debug(e);
        }
        return objValue;
    }

    /**
     * 从类实例转换为Map，但不对Collection这类的属性进行进一步处理
     * @param clazz 类对象
     * @param target 类的实例
     * @return 返回的Map对象，注意这个Map对象中，key是属性的名称，value是属性的值的字符串表现形式
     */
    public static Map<String, String> Object2Map(Class<?> clazz, Object target) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            Field[] fields = clazz.getDeclaredFields();
            for (Field f : fields) {
                f.setAccessible(true);
                String key = f.getName();
                Class<?> propertyClass = f.getType();
                Object propertyValue = f.get(target);
                String value = formatValue(propertyClass, propertyValue);
                map.put(key, value);
            }
            //如果父类不是Object则将递归遍历父类属性
            Class<?> superClass = clazz.getSuperclass();
            if (!superClass.getName().equals("java.lang.Object")) map.putAll(Object2Map(superClass,target));
        } catch (Exception e) {
            logger.info(e);
        }
        return map;
    }

    /**
     * 从类实例转换为Map，不包括属性为空的field。但不对Collection这类的属性进行进一步处理
     * @param clazz 类对象
     * @param target 类的实例
     * @return 返回的Map对象，注意这个Map对象中，key是属性的名称，value是属性的值的字符串表现形式
     */
    public static Map<String,Object> Object2MapWithoutNull(Class<?> clazz, Object target) {
        Map<String,Object>  map = new HashMap<String,Object>();
        try {
            Field[] fields = clazz.getDeclaredFields();
            for (Field f : fields) {
                f.setAccessible(true);
                String key = f.getName();
                Class<?> propertyClass = f.getType();
                Object propertyValue = f.get(target);
                if(propertyValue!= null) {
                    String value = formatValue(propertyClass, propertyValue);
                    map.put(key, value);
                }
            }
            //如果父类不是Object则将递归遍历父类属性
            Class<?> superClass = clazz.getSuperclass();
            if (!superClass.getName().equals("java.lang.Object")) map.putAll(Object2MapWithoutNull(superClass,target));
        } catch (Exception e) {
            logger.info(e);
        }
        return map;
    }

    /**
     * 从Bean实例转换为Map，不包括get或is为空的方法。但不对Collection这类的属性进行进一步处理
     * @param clazz 类对象
     * @param target 类的实例
     * @return 返回的Map对象，注意这个Map对象中，key是属性的名称，value是属性的值的字符串表现形式
     */
    public static Map<String,Object> Bean2MapWithoutNull(Class<?> clazz, Object target) {
        Map<String,Object> map = new HashMap<String,Object>();
        try {
            Method[] methods=clazz.getDeclaredMethods();
            for (Method m : methods) {
                m.setAccessible(true);
                String key = m.getName();
                if ((key.startsWith("get")&&key.length()>3)||(key.startsWith("is")&&key.length()>2)) {
                    Class<?> _type = m.getReturnType();
                    Object value = null;
                    try {
                        value = m.invoke(target);
                    } catch(Exception e) {
                        
                    }
                    if (ReflectUtils.isMyClass(_type, value)) {
                        if ((key.startsWith("get")&&key.length()>3)) {
                            key = key.substring(3);
                            key = key.substring(0, 1).toLowerCase()+key.substring(1);
                        }
                        map.put(key, formatValue(m.getReturnType(), value));
                    }
                }
            }
            //如果父类不是Object则将递归遍历父类属性
            Class<?> superClass = clazz.getSuperclass();
            if (!superClass.isInterface()&&!superClass.getName().equals("java.lang.Object")) map.putAll(Bean2MapWithoutNull(superClass, target));
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e);
        }
        return map;
    }

    /**
     * 将对象的属性值转化为格式化字符串
     * 注意:如果是复杂类型，将直接调用其toString方法返回
     * 暂时不支持复杂对象的处理
     * @param baseClass
     * @param object
     * @return
     */
    private static String formatValue(Class<?> baseClass, Object object) {
        String value = "";
        try {
            String typeName = baseClass.getName();
            if (object!=null) {
                if (typeName.equals("java.util.Date")) {
                    Date date = (Date) object;
                    value = DateUtils.convert2LongLocalStr(date);
                } else value = object.toString(); //复杂类型暂时不处理，调用默认toString方法。
            }
        } catch (Exception e) {
            logger.info( e);
            value = "";
        }
        return value;
    }

    private static boolean isMyClass(Class<?> c, Object o) {
        if (o==null) return false;
        //判断是否是基础类
        Class<?> _c = c;
        String cName = _c.getName();
        boolean isBaseClass = false;
        for (String s: baseClass) {
            if (cName.equals(s)) {
                isBaseClass = true;
                break;
            }
        }
        if (isBaseClass) return true;
        //判断返回类型
        boolean isMyClass = false;
        boolean isInterface = false;
        _c=o.getClass();
        cName = _c.getName();
        isInterface = _c.isInterface();
        while (!isInterface&&!cName.equals("java.lang.Object")) {
            for (String s: classList) {
                if (cName.equals(s)) {
                    isMyClass = true;
                    break;
                }
            }
            if (!isMyClass) {
                _c = _c.getSuperclass();
                cName = _c.getName();
                isInterface = _c.isInterface();
            } else break;
        }
        return isMyClass;
    }
}