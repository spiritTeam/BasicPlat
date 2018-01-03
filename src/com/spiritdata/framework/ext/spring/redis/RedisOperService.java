package com.spiritdata.framework.ext.spring.redis;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.redis.connection.RedisStringCommands.SetOption;
import org.springframework.data.redis.connection.jedis.JedisClusterConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.util.Assert;

import redis.clients.jedis.Jedis;
import redis.clients.util.SafeEncoder;

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
    private JedisClusterConnection jedisCluster=null;
    private int __type=-1;//=0redis直接链接;//=1redis集群链接;//=-1不链接

    /**
     * 构造函数，必须要设置连接
     * 并按照设定的数据库index连接数据库
     * @param conn
     */
    public RedisOperService(JedisConnectionFactory conn) {
        super();
        Assert.notNull(conn, "请设置Spring-Redis的连接类");
        try {
            this.jedisCluster=(JedisClusterConnection)conn.getClusterConnection();
            this.__type=1;
        } catch(InvalidDataAccessApiUsageException e) {
            this.jedis=conn.getShardInfo().createResource();
            this.jedis.select(conn.getDatabase());
            this.__type=0;
        }
    }
    /**
     * 构造函数，必须要设置连接
     * @param conn
     */
    public RedisOperService(JedisConnectionFactory conn, int dbIndex) {
        super();
        Assert.notNull(conn, "请设置Spring-Redis的连接类");
        try {
            this.jedisCluster=(JedisClusterConnection)conn.getClusterConnection();
            this.__type=1;
        } catch(InvalidDataAccessApiUsageException e) {
            this.jedis=conn.getShardInfo().createResource();
            this.jedis.select(dbIndex);//默认用第0个数据库
            this.__type=0;
        }
    }

    /**
     * 构造函数，必须要设置连接
     * @param conn
     */
    public RedisOperService(JedisConnectionFactory conn, int dbIndex, int type) {
        super();
        Assert.notNull(conn, "请设置Spring-Redis的连接类");
        if (type==1) {
            this.jedisCluster=(JedisClusterConnection)conn.getClusterConnection();
            this.__type=1;
        } if (type==0) {
            this.jedis=conn.getShardInfo().createResource();
            this.jedis.select(dbIndex);//默认用第0个数据库
            this.__type=0;
        } else {
            try {
                this.jedisCluster=(JedisClusterConnection)conn.getClusterConnection();
                this.__type=1;
            } catch(InvalidDataAccessApiUsageException e) {
                this.jedis=conn.getShardInfo().createResource();
                this.jedis.select(dbIndex);//默认用第0个数据库
                this.__type=0;
            }
        }
    }
    /**
     * 关闭此连接
     */
    public void close() {
        if (__type==0&&jedis!=null) jedis.close();
        if (__type==1&&jedisCluster!=null) jedisCluster.close();
    }

    /**
     * 设置数据
     * @param key 数据的key
     * @param value 数据的值
     */
    public boolean set(String key, String value) {
        if (__type==0) return (jedis.set(key, value)).equals("OK");
        else {
            jedisCluster.set(key.getBytes(), value.getBytes());
            return true;
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
        if (__type==0) {
            String _sta=jedis.set(key, value);
            if (!_sta.equals("OK")) return false;
        } else
        if (__type==1) {
            jedisCluster.set(key.getBytes(), value.getBytes());
        } else return false;
        return this.pExpire(key, expireTime);
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
            if (expireTime>0) {
                return set(key, value, expireTime);
            } else {
                return set(key, value);
            }
        } else {
            if (__type==0) {
                if (expireTime>0) _sta=jedis.set(key, value, _option, "PX", expireTime);
                else _sta=jedis.set(key, value, _option);
                if (_sta==null) return false;
                return _sta.equals("OK");
            } else
            if (__type==1) {
                SetOption _so=SetOption.UPSERT;
                if (option.equals("NX")) _so=SetOption.SET_IF_ABSENT;
                if (option.equals("XX")) _so=SetOption.SET_IF_PRESENT;
                Expiration expire=Expiration.milliseconds(expireTime);
                if (expireTime<=0) expire=Expiration.persistent();
                jedisCluster.set(key.getBytes(), value.getBytes(), expire, _so);
                return true;
            } else return false;
        }
    }

    /**
     * 字符串操作：根据Key值得到对应的Value
     * @param key 数据的key
     * @param value 数据的值
     * @param expireTime 过期时间，只支持毫秒，若是-1则表示没有过期时间
     * @param optin 操作 只支持NX,XX其他都按无处理
     */
    public String get(String key) {
        if (__type==0) return jedis.get(key);
        else
        if (__type==1) {
            byte[] ba=jedisCluster.get(key.getBytes());
            if (ba==null) return null;
            return new String(ba);
        }
        return null;
    }

    /**
     * 判断key值序列是否存在
     * @param keys key数组，字符串数组
     * @return 返回在参数key序列中，存在的key的个数
     */
    public long exists(String... keys) {
        if (__type==0) return jedis.exists(keys);
        else
        if (__type==1) {
            long count=0;
            for (String k: keys) {
                if (jedisCluster.exists(k.getBytes())) count++;
            }
            return count;
        }
        return 0;
    }

    /**
     * 判断key值是否存在
     * @param key 数据的key
     * @return 若存在返回true，否则返回false
     */
    public boolean exist(String key) {
        if (__type==0) return jedis.exists(key);
        else
        if (__type==1) return jedisCluster.exists(key.getBytes());
        return false;
    }
    
    /**
     * 设置Key的过期时间，单位为毫秒
     * @param key 数据的key
     * @param milliseconds 过期时间
     * @return 设置成功返回true，设置失败(key不存在，或[在redis2.1.3版本之前]已经有过期时间)返回false
     */
    public boolean pExpire(String key, long expireTime) {
        if (__type==0) return jedis.pexpire(key, expireTime)==1;
        else
        if (__type==1) return jedisCluster.pExpire(key.getBytes(), expireTime);
        return false;
    }

    /**
     * 向key中插入一组数据
     * @param key 数组的key
     * @param values 插入数据后，数组的长度
     */
    public void rPush(String key, String... values) {
        if (__type==0) jedis.rpush(key, values);
        else
        if (__type==1) jedisCluster.rPush(key.getBytes(), SafeEncoder.encodeMany(values));
    }

    /**
     * 得到key对应的数组的子数组，子数组从下标start到end，注意Redis的下标从0开始
     * @param key 数组的key
     * @param start 开始下标
     * @param end 结束下标
     * @return 子数组，以String为元素
     */
    public List<String> lRange(String key, long start, long end) {
        if (__type==0) return jedis.lrange(key, start, end);
        else
        if (__type==1) {
            List<byte[]> lr=jedisCluster.lRange(key.getBytes(), start, end);
            if (lr==null||lr.isEmpty()) return null;
            List<String> r=new ArrayList<String>();
            for (byte[] ba: lr) r.add(new String(ba));
            return r;
        }
        return null;
    }

    /**
     * 得到key对应的数组的长度
     * @param key 数组的key
     * @return 数组的长度
     */
    public Long lLen(String key) {
        if (__type==0) return jedis.llen(key);
        else
        if (__type==1) return jedisCluster.lLen(key.getBytes());
        return 0l;
    }

    /**
     * 从key对应的数据中，删除值为value的元素
     * @param key 数组的key
     * @param value 数组中预删除的值
     * @return 删除掉的元素的个数
     */
    public Long lRemove(String key, String value) {
        if (__type==0) return jedis.lrem(key, 0, value);
        else
        if (__type==1) return jedisCluster.lRem(key.getBytes(), 0, value.getBytes());
        return 0l;
    }

    /**
     * 删除Key值，可以是key的数据组
     * @param keys key数组，字符串数组
     * @return 成功删除的key值数
     */
    public long del(String... keys) {
        if (__type==0) return jedis.del(keys);
        else
        if (__type==1) {
            jedisCluster.del(SafeEncoder.encodeMany(keys));
            return keys.length;
        }
        return 0;
    }

    /**
     * 清除该库下的所有key
     */
    public void cleanDB() {
        if (__type==0) jedis.flushDB();
        else
        if (__type==1) jedisCluster.flushDb();
    }
}