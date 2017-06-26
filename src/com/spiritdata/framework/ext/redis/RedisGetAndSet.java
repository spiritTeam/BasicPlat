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

    protected abstract String getKey();

    protected abstract T convert(String s);

    protected abstract String convert(T item);
}