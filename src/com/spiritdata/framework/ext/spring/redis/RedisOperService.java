package com.spiritdata.framework.ext.spring.redis;

import java.util.List;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.util.Assert;

import com.spiritdata.framework.ext.redis.GetBizData;
import com.spiritdata.framework.util.StringUtils;

import redis.clients.jedis.Jedis;

/**
 * 在Spring的Redis基础之上包的一层Redis操作方法服务类。
 * <pre>
 * 注意：
 * 1-实际操作用的是Jedis对象，这可能把Spring的事务机制破坏了
 * 2-这个服务的每个实例会从Spring的Jedis连接工厂获得一个操作对象jedis，并通过这个对象对jedis进行操作
 * 3-注意要关闭Redis操作
 * </pre>
 * @author wanghui
 */
public class RedisOperService {
    private Jedis jedis=null;

    /**
     * 构造函数，必须要设置连接
     * 并按照设定的数据库index连接数据库
     * @param conn
     */
    public RedisOperService(JedisConnectionFactory conn) {
        super();
        Assert.notNull(conn, "请设置Spring-Redis的连接类");
        this.jedis=conn.getShardInfo().createResource();
        this.jedis.select(conn.getDatabase());
    }
    /**
     * 构造函数，必须要设置连接
     * @param conn
     */
    public RedisOperService(JedisConnectionFactory conn, int dbIndex) {
        super();
        Assert.notNull(conn, "请设置Spring-Redis的连接类");
        this.jedis=conn.getShardInfo().createResource();
        this.jedis.select(dbIndex);//默认用第0个数据库
    }
    /**
     * 关闭此连接
     */
    public void close() {
        if (jedis!=null) jedis.close();
    }

    /**
     * 设置数据
     * @param key 数据的key
     * @param value 数据的值
     */
    public boolean set(String key, String value) {
        return (jedis.set(key, value)).equals("OK");
    }

    /**
     * 设置数据
     * @param key 数据的key
     * @param value 数据的值
     * @param expireTime 过期时间，只支持毫秒，若是-1则表示没有过期时间
     * @param optin 操作 只支持NX,XX其他都按无处理
     */
    public boolean set(String key, String value, String option, long expireTime) {
        String _sta="";
        String _option=(option==null?"":(option.equals("NX")||option.equals("XX")?option:"")); //处理操作

        if (_option.equals("")) {
            if (expireTime>0) return set(key, value, expireTime);
            else return set(key, value);
        } else {
            if (expireTime>0) _sta=jedis.set(key, value, _option, "PX", expireTime);
            else _sta=jedis.set(key, value, _option);
            if (_sta==null) return false;
            return _sta.equals("OK");
        }
    }

    /**
     * 设置数据
     * @param key 数据的key
     * @param value 数据的值
     * @param expireTime 过期时间，只支持毫秒，若是-1则表示没有过期时间
     * @param optin 操作 只支持NX,XX其他都按无处理
     */
    public boolean set(String key, String value, long expireTime) {
        String _sta=jedis.set(key, value);
        if (!_sta.equals("OK")) return false;
        return this.pExpire(key, expireTime);
    }

    /**
     * 字符串操作：根据Key值得到对应的Value
     * @param key 数据的key
     * @param value 数据的值
     * @param expireTime 过期时间，只支持毫秒，若是-1则表示没有过期时间
     * @param optin 操作 只支持NX,XX其他都按无处理
     */
    public String get(String key) {
        return jedis.get(key);
    }

    /**
     * 判断key值序列是否存在
     * @param keys key数组，字符串数组
     * @return 返回在参数key序列中，存在的key的个数
     */
    public long exists(String... keys) {
        return jedis.exists(keys);
    }

    /**
     * 判断key值是否存在
     * @param key 数据的key
     * @return 若存在返回true，否则返回false
     */
    public boolean exist(String key) {
        return jedis.exists(key);
    }
    
    /**
     * 设置Key的过期时间，单位为毫秒
     * @param key 数据的key
     * @param milliseconds 过期时间
     * @return 设置成功返回true，设置失败(key不存在，或[在redis2.1.3版本之前]已经有过期时间)返回false
     */
    public boolean pExpire(String key, long expireTime) {
        return jedis.pexpire(key, expireTime)==1;
    }

    /**
     * 向key中插入一组数据
     * @param key 数据的key
     * @param values 插入数据后，数组的长度
     */
    public void rPush(String key, String... values) {
        jedis.rpush(key, values);
    }

    /**
     * 得到key对应的数组的子数组，子数组从下标start到end，注意Redis的下标从0开始
     * @param key 数据的key
     * @param start 开始下标
     * @param end 结束下标
     * @return 子数组，以String为元素
     */
    public List<String> lRange(String key, long start, long end) {
        return jedis.lrange(key, start, end);
    }

    /**
     * 得到key对应的数组的长度
     * @param key 数据的key
     * @return 数组的长度
     */
    public Long lLen(String key) {
        return jedis.llen(key);
    }

    /**
     * 删除Key值，可以是key的数据组
     * @param keys key数组，字符串数组
     * @return 成功删除的key值数
     */
    public long del(String... keys) {
        return jedis.del(keys);
    }

    /**
     * 清除该库下的所有key
     */
    public void cleanDB() {
        jedis.flushDB();
    }

    /**
     * 获得并设置Key值
     * @param key key值
     * @param getData 获得业务数据的类
     * @param expiredTime 过期时间
     * @return 所获得的值
     */
    public String getAndSet(String key, GetBizData getData, long expiredTime) {
        String _ret=jedis.get(key);
        if (StringUtils.isNullOrEmptyOrSpace(_ret)) {
            _ret=getData.getBizData();
            if (!StringUtils.isNullOrEmptyOrSpace(_ret)) {
                set(key, _ret, expiredTime);
            }
        }
        return _ret;
    }
}