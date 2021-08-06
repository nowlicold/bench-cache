package com.bench.cache.client.impl;

import com.bench.cache.client.InnerAppCacheClient;
import com.bench.cache.enums.AppCacheClientEnum;
import com.bench.common.cache.app.AppCacheAreaName;
import com.bench.lang.base.string.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @className InnerAppCacheByRedisClient
 * @autor cold
 * @DATE 2021/6/21 20:08
 **/
@Service
public class InnerAppCacheByRedisClientImpl implements InnerAppCacheClient {
    @Autowired
    public RedisTemplate redisTemplate;

    private String convertKey(AppCacheAreaName area, String key){
        return area.name()+StringUtils.COLON_SIGN+key;
    }

    @Override
    public boolean support(AppCacheClientEnum appCacheClient) {
        return appCacheClient == AppCacheClientEnum.REDIS_CLIENT;
    }

    @Override
    public <T> T getObjectWithExpire(AppCacheAreaName area, String key, int expire) {
        T data = getObject(area,key);
        expire(area,key,expire,TimeUnit.SECONDS);
        return data;
    }

    @Override
    public <T> boolean putObjectWithExpire(AppCacheAreaName area, String key, T data, int expire) {
        if(data == null){
            return false;
        }
        String convertedKey = convertKey(area,key);

        redisTemplate.opsForValue().set(convertedKey,data,expire,TimeUnit.SECONDS);
        return true;
    }

    @Override
    public <T> boolean putObjectWithExpire(AppCacheAreaName area, String key, T data, int expire, boolean updateExisted) {
        if(data == null){
            return false;
        }
        String convertedKey = convertKey(area,key);
        //如果已存在，切不用更新则直接返回
        if(exists(area,key) && !updateExisted){
            return true;
        }
        if (expire <= 0) {
            redisTemplate.opsForValue().set(convertedKey,data);
            return true;
        }else{
            return putObjectWithExpire(area, key, data, expire);
        }

    }

    @Override
    public boolean removeObject(AppCacheAreaName area, String key) {
        String convertedKey = convertKey(area,key);
        return redisTemplate.delete(convertedKey);
    }

    @Override
    public <T> T getObject(AppCacheAreaName area, String key) {
        String convertedKey = convertKey(area,key);
        ValueOperations<String, T> operation = redisTemplate.opsForValue();

        return operation.get(convertedKey);
    }

    @Override
    public <T> boolean putObject(AppCacheAreaName area, String key, T data) {
        if(data == null){
            return false;
        }
        String convertedKey = convertKey(area, key);
        redisTemplate.opsForValue().set(convertedKey,data);
        return true;
    }

    @Override
    public boolean expire(AppCacheAreaName area, String key, long timeout, TimeUnit unit) {
        String convertedKey = convertKey(area, key);
        return redisTemplate.expire(convertedKey,timeout,unit);
    }

    @Override
    public boolean exists(AppCacheAreaName area, String key) {
        String convertedKey = convertKey(area, key);
        return redisTemplate.hasKey(convertedKey);
    }

}
