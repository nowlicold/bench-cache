package com.bench.cache;

import java.util.concurrent.TimeUnit;

/**
 * @className AppCache
 * @autor cold
 * @DATE 2021/6/21 19:49
 * App缓存 隔离实现方式，默认为redis,需要配置spring.redis的相关配置
 **/
public interface AppCacheComponent {
    public static final int DEFAULT_EXPIRE_SECONDS = 30 * 24 * 3600;

    /**
     * /**
     * <p>
     * 通过key获取某个value,并且将过期时间更新为,当前时间+expire
     * </p>
     *
     * @param key
     * @param expire (单位为秒)
     * @return
     */
    public <T> T getObjectWithExpire(AppCacheAreaName area, String key, int expire);

    /**
     * 把key/value放到cache中
     *
     * @param key
     * @param data
     * @param expire (有效期时间,0的话不过期)单位为秒
     * @return
     */
    public <T> boolean putObjectWithExpire(AppCacheAreaName area, String key, T data, int expire);

    /**
     * 把key/value放到cache中
     *
     * @param area
     * @param key
     * @param data
     * @param expire
     * @param updateExisted
     * @return
     */
    public <T> boolean putObjectWithExpire(AppCacheAreaName area, final String key, final T data, final int expire, boolean updateExisted);

    /**
     * 在Cache中删除某个key(Object)
     *
     * @param key
     * @return 删除结果状态 EXIT_FAILURE:失败 EXIT_SUCCESS:成功,具体常量值参看<a>TDBMConst</a>
     */
    public boolean removeObject(AppCacheAreaName area, String key);

    /**
     * 通过key获取某个value
     *
     * @param key
     * @return
     */
    public <T> T getObject(AppCacheAreaName area, String key);

    /**
     * 通过key set 某个value
     *
     * @param key
     * @param data
     * @return
     */
    public <T> boolean putObject(AppCacheAreaName area, String key, T data);


    /**
     * 设置有效期
     *
     * @param key
     * @param timeout
     * @param unit
     * @return
     */
    public boolean expire(AppCacheAreaName area, String key, long timeout, TimeUnit unit);

    /**
     * 是否存在
     *
     * @param area
     * @param key
     * @return
     */
    public boolean exists(AppCacheAreaName area, String key);
}
