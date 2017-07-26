package com.spiritdata.framework.ext.redis;

import java.util.Map;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import com.spiritdata.framework.core.cache.GetAndSet;
import com.spiritdata.framework.ext.spring.redis.RedisOperService;
import com.spiritdata.framework.util.StringUtils;

public abstract class RedisGetAndSet<T> extends GetAndSet<T> {
    protected RedisOperService roService=null;
    protected long expireTime=-1;

    public RedisGetAndSet(Map<String, Object> param) {
        super(param);
    }

    public void setRedisConn(JedisConnectionFactory conn) {
        this.roService=new RedisOperService(conn, 1);
    }
    public void setExpireTime(long expire) {
        this.expireTime=expire;
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
        if (expireTime==-1) {
            roService.set(getKey(), convert(newItem));
        } else {
            roService.set(getKey(), convert(newItem), expireTime);
        }
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