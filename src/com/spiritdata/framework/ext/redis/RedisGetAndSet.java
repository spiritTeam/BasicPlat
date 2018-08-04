package com.spiritdata.framework.ext.redis;

import java.util.Map;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import com.spiritdata.framework.core.cache.GetAndSet;
import com.spiritdata.framework.ext.spring.redis.RedisOperService;
import com.spiritdata.framework.util.StringUtils;

/**
 * 从Redis缓存中得到单条业务数据
 * @author wh
 * @param <T> 业务数据的数据类型
 */
public abstract class RedisGetAndSet<T> extends GetAndSet<T> {
    protected RedisOperService roService=null;
    protected long expireTime=-1;
    private int dbIndex=0;

    public RedisGetAndSet(Map<String, Object> param) {
        super(param);
    }

    /**
     * 设置Redis的链接器
     * @param conn Redis连接器
     */
    public void setRedisConn(JedisConnectionFactory conn) {
        this.roService=new RedisOperService(conn, dbIndex);
    }
    /**
     * 设置参数的过期使时间
     * @param expire 过期时间，若为-1则不过期
     */
    public void setExpireTime(long expire) {
        this.expireTime=expire;
    }
    /**
     * 设置Redis数据库的数据库编号，若连接器是Redis集群，则这个设置无意义
     * @param dbIndex 数据库编号
     */
    public void setDbIndex(int dbIndex) {
        this.dbIndex=dbIndex;
    }

    @Override
    protected T getFromCache() {
        if (roService==null) throw new RuntimeException("Redis操作服务类为空");
        String s=roService.get(getKey());
        if (StringUtils.isNullOrEmptyOrSpace(s)) return null;
        T ret=convert(s);
        return ret;
    }

    @Override
    protected void flushCache(T newItem) {
        if (roService==null) throw new RuntimeException("Redis操作服务类为空");
        if (expireTime==-1) roService.set(getKey(), convert(newItem));
        else roService.set(getKey(), convert(newItem), expireTime);
    }

    /**
     * 得到Redis中的Key值
     * @return Key值
     */
    protected abstract String getKey();

    /**
     * 把从Redis中读出的字符串转换为对象
     * @param s 字符串Json格式的
     * @return 对象
     */
    protected abstract T convert(String s);

    /**
     * 把对象转换为字符串，为存入Redis做准备
     * @param item 对象
     * @return Json字符串
     */
    protected abstract String convert(T item);
}