/**
 * benchcode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.bench.cache.client;

import com.bench.cache.enums.AppCacheClientEnum;
import com.bench.common.cache.app.AppCacheAreaName;

import java.util.concurrent.TimeUnit;

/**
 * 内部cache客户端
 * 
 * @author cold
 * 
 * @version $Id: InnerAppCacheClient.java, v 0.1 2010-5-29 下午04:31:34 cold Exp $
 */
public interface InnerAppCacheClient {
	public boolean support(AppCacheClientEnum appCacheClient);

	/**
	 * <p>
	 * 通过key获取某个value,并且将过期时间更新为,当前时间+expire
	 * </p>
	 * 
	 * @param key
	 * @param expire
	 *            (单位为秒)
	 * @return
	 */
	public<T> T getObjectWithExpire(AppCacheAreaName area, String key, int expire);

	/**
	 * 把key/value放到cache中
	 * 
	 * @param key
	 * @param data
	 * @param expire
	 *            (有效期时间,0的话不过期)单位为秒
	 * @return
	 */
	public<T> boolean putObjectWithExpire(AppCacheAreaName area, String key, T data, int expire);

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
	public<T> boolean putObjectWithExpire(AppCacheAreaName area, final String key, final T data, final int expire, boolean updateExisted);

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
	public<T> T getObject(AppCacheAreaName area, String key);

	/**
	 * 通过key set 某个value
	 * 
	 * @param key
	 * @param data
	 * @return
	 */
	public<T> boolean putObject(AppCacheAreaName area, String key, T data);


	/**
	 * 设置有效期
	 * @param key
	 * @param timeout
	 * @param unit
	 * @return
	 */
	public boolean expire(AppCacheAreaName area, String key,  long timeout,  TimeUnit unit);

	/**
	 * 是否存在
	 * @param area
	 * @param key
	 * @return
	 */
	public boolean exists(AppCacheAreaName area, String key);


}

