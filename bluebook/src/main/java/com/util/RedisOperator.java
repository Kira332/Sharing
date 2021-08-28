package com.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisOperator {

    @Autowired
    private RedisTemplate redisTemplate;


    public boolean hasKey(String key){
        return redisTemplate.hasKey(key);
    }


    public long ttl(String key) {
        return redisTemplate.getExpire(key);
    }

    public void expire(String key, long timeout) {
        redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    public long incr(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }


    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }


    public void del(String key) {
        redisTemplate.delete(key);
    }


    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, String value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Long size(String key) {
        return redisTemplate.opsForValue().size(key);
    }

    public boolean hasHkey(String key, Object field){
        return redisTemplate.opsForHash().hasKey(key, field);
    }

    public void hset(String key, Object field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    public Long hsize(String key){
        return 	redisTemplate.opsForHash().size(key);
    }

    public Object hget(String key, Object field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    public void hdel(String key, Object... fields) {
        redisTemplate.opsForHash().delete(key, fields);
    }

    public Object hgetall(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public long lpush(String key, Object value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }


    public long lremove(String key, long count, Object value){
        return redisTemplate.opsForList().remove(key, 0, value);
    }

    public Object lpop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    public long rpush(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    public Object range(String key, long start, long stop){
        return redisTemplate.opsForList().range(key, start, stop);
    }




}
