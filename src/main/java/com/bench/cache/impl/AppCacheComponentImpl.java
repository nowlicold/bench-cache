package com.bench.cache.impl;

import com.bench.cache.AppCacheAreaName;
import com.bench.cache.AppCacheComponent;
import com.bench.cache.client.InnerAppCacheClient;
import com.bench.cache.enums.AppCacheClientEnum;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @className AppCacheComponentImpl
 * @autor cold
 * @DATE 2021/6/21 20:03
 **/
@Service
public class AppCacheComponentImpl implements AppCacheComponent {
    /**
     * 默认是redis，如有其它通道，在配置文件中设置该值
     */
    @Value("appCache.appCacheCode")
    private AppCacheClientEnum appCacheClient = AppCacheClientEnum.REDIS_CLIENT;
    @Autowired
    private List<InnerAppCacheClient> innerAppCacheClientList;

    @SneakyThrows
    private InnerAppCacheClient getInnerAppCacheClient() {
        InnerAppCacheClient innerAppCacheClient = null;
        for (InnerAppCacheClient innerAppCacheClientTemp : innerAppCacheClientList){
            if(innerAppCacheClientTemp.support(appCacheClient)){
                innerAppCacheClient = innerAppCacheClientTemp;
            }
        }
        //如果为空，则抛异常，宁可报错
        if(innerAppCacheClient == null){
            throw new Exception("未找到appCache中的client");
        }
        return innerAppCacheClient;
    }
    @Override
    public <T> T getObjectWithExpire(AppCacheAreaName area, String key, int expire) {
        return getInnerAppCacheClient().getObjectWithExpire(area, key, expire);
    }

    @Override
    public <T> boolean putObjectWithExpire(AppCacheAreaName area, String key, T data, int expire) {
        return getInnerAppCacheClient().putObjectWithExpire(area,key,data,expire);
    }

    @Override
    public <T> boolean putObjectWithExpire(AppCacheAreaName area, String key, T data, int expire, boolean updateExisted) {
        return getInnerAppCacheClient().putObjectWithExpire(area,key,data,expire,updateExisted);
    }

    @Override
    public boolean removeObject(AppCacheAreaName area, String key) {
        return getInnerAppCacheClient().removeObject(area,key);
    }

    @Override
    public <T> T getObject(AppCacheAreaName area, String key) {
        return getInnerAppCacheClient().getObject(area,key);
    }

    @Override
    public <T> boolean putObject(AppCacheAreaName area, String key, T data) {
        return getInnerAppCacheClient().putObject(area,key,data);
    }

    @Override
    public boolean expire(AppCacheAreaName area, String key, long timeout, TimeUnit unit) {
        return getInnerAppCacheClient().expire(area, key, timeout, unit);
    }

    @Override
    public boolean exists(AppCacheAreaName area, String key) {
        return getInnerAppCacheClient().exists(area, key);
    }
}
