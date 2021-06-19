package com.bench.cache.local;

import com.yuan.common.cache.local.RefreshableCacheObject;

import java.util.List;

/**
 * 基于修改时间的可刷新组件
 * 
 * @author cold
 *
 * @version $Id: CacheRefreshComponent.java, v 0.1 2015年9月16日 下午5:33:39 cold Exp $
 */
public interface RefreshableCacheComponent<T extends RefreshableCacheObject<K, V>, K, V extends Comparable<V>> {

	/**
	 * 获取对象
	 * 
	 * @param k
	 * @return
	 */
	public T getObject(K k);

	/**
	 * 获取所有的对象
	 * 
	 * @return
	 */
	public List<T> getAll();

	/**
	 * 获取所有的对象
	 * 
	 * @return
	 */
	public List<T> getAllEnabled();

	/**
	 * 刷新cache
	 * 
	 * @return
	 */
	public int refreshCache();

	/**
	 * 是否支持全刷新，默认都是支持的
	 * 
	 * @return
	 */
	public default boolean isSupportRefreshAll() {
		return true;
	}

	/**
	 * 刷新cache
	 * 
	 * @return
	 */
	public int refreshAll();



}
