/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.bench.cache.local.object;

/**
 * 可刷新的cache对象
 * 
 * @author cold
 *
 * @version $Id: RefreshableCacheObject.java, v 0.1 2015年9月16日 下午5:36:24 cold
 *          Exp $
 */
public interface RefreshableCacheObject<K, V extends Comparable<V>> extends CacheableObject<K> {

	/**
	 * 返回cache的key
	 */
	public K returnCacheKey();

	/**
	 * 
	 * 返回刷新的比较值，这个是用于比较对象是否刷新,比如返回修改时间
	 * 
	 * @return
	 */
	public V returnCacheRefreshCompareValue();

	/**
	 * 数据是否有效，默认是有效的
	 * 
	 * @return
	 */
	public default boolean isEnabled() {
		return true;
	}

}
