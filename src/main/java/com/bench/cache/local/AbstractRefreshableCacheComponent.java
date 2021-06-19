/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.bench.cache.local;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.bench.cache.local.object.RefreshableCacheObject;
import com.bench.lang.base.error.enums.CommonErrorCodeEnum;
import com.bench.lang.base.exception.BenchRuntimeException;
import com.bench.lang.base.order.Ordered;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;

/**
 * 基于loader的修改时间全内存缓存<br>
 * 注意数据量不能太大，否则性能有问题
 * 
 * @author cold
 *
 * @version $Id: AbstractRefreshableCacheComponent.java, v 0.1 2015年9月16日 下午5:41:43 cold Exp $
 */
public abstract class AbstractRefreshableCacheComponent<T extends RefreshableCacheObject<K, V>, K, V extends Comparable<V>>
		implements RefreshableCacheComponent<T, K, V> {

	private static final int MAX_CACHE_SIZE = 200000;

	private V maxRefreshCompareValue;

	protected boolean initialized;

	// 线程安全
	protected Map<K, T> cacheMap = new ConcurrentHashMap<K, T>();

	/**
	 * 加载全部
	 * 
	 * @return
	 */
	protected abstract List<T> loadAll();


	/**
	 * 返回所有的key集合，如果为null，则说明该方法没实现<br>
	 * 防止用户直接删除数据库数据，无法检测到
	 * 
	 */
	protected List<K> loadAllKeys() {
		return null;
	}

	/**
	 * 加载比该修改时间大的
	 * 
	 * @return
	 */
	protected abstract List<T> loadLargeThan(V maxRefreshCompareValue);

	/**
	 * 加载一个对象，可能是首次加载，也可能是重载，当oldObject为空时，是首次加载，否则是重载
	 * 
	 * @param oldObject
	 * @param newObject
	 */
	protected void onLoad(T oldObject, T newObject) {

	}

	/**
	 * 加载一个对象前，可能是首次加载，也可能是重载，当oldObject为空时，是首次加载，否则是重载
	 *
	 * @param oldObject
	 * @param newObject
	 */
	protected void onBeforeLoad(T oldObject, T newObject) {

	}

	/**
	 * 首次加载
	 * 
	 * @param object
	 */
	protected void onFirstLoad(T object) {

	}

	/**
	 * 首次加载前
	 * 
	 * @param object
	 */
	protected void onBeforeFirstLoad(T object) {

	}

	/**
	 * 重载一个对象,一定是之前已经存在，后来又再次加载以覆盖之前的对象
	 *
	 * @param oldObject
	 * @param newObject
	 * */
	protected void onReload(T oldObject, T newObject) {

	}

	/**
	 * 重载一个对象前,一定是之前已经存在，后来又再次加载以覆盖之前的对象
	 *
	 * @param oldObject
	 * @param newObject
	 */
	protected void onBeforeReload(T oldObject, T newObject) {

	}

	/**
	 * 删除前
	 * 
	 * @param object
	 */
	protected void onBeforeDelete(T object) {

	}

	/**
	 * 删除后
	 * 
	 * @param object
	 */
	protected void onDelete(T object) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bench.platform.base.cache.refresh. GmtModifiedBasedCacheRefreshComponent #getObject(java.lang.Object)
	 */
	@Override
	public T getObject(K k) {
		// TODO Auto-generated method stub
		return cacheMap.get(k);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bench.platform.base.cache.refresh. GmtModifiedBasedCacheRefreshComponent #getAll()
	 */
	@Override
	public List<T> getAll() {
		// TODO Auto-generated method stub
		return new ArrayList<T>(this.cacheMap.values());
	}





	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bench.platform.scheduler.task.Task#execute()
	 */
	@Scheduled(fixedRate=5000)
	public void execute() {
		// TODO Auto-generated method stub
		refreshCache();
	}

	/**
	 * 刷新cache后触发，能识别db中删除的数据
	 * 
	 * @param loadedList
	 *            增量变更的数据
	 * @param removeKeys
	 *            移除的key集合
	 * @param allLoad
	 *            是否全量更新
	 */
	protected void onRefreshCacheFinish(List<T> loadedList, List<K> removeKeys, boolean allLoad) {

	}

	public synchronized int refreshCache() {
		List<T> objectList = null;
		boolean allLoad = false;
		boolean cacheChange = false;
		List<K> removeKeys = new ArrayList<K>();
		if (this.maxRefreshCompareValue == null) {
			// 第一次
			objectList = loadAll();
			allLoad = true;
		} else {
			objectList = loadLargeThan(maxRefreshCompareValue);
		}
		if (objectList == null) {
			objectList = new ArrayList<T>(0);
		}
		// 增量会偏大，先不管
		if (objectList.size() > MAX_CACHE_SIZE) {
			throw new BenchRuntimeException(CommonErrorCodeEnum.SYSTEM_ERROR, "缓存记录数过大，超过" + MAX_CACHE_SIZE + ",实际：" + objectList.size());
		}
		cacheChange = objectList.size() > 0;

		V currentMaxRefreshCompareValue = maxRefreshCompareValue;

		// 如果是全加载
		if (allLoad) {
			Map<K, T> allCacheMap = new HashMap<K, T>();
			for (T object : objectList) {
				K cacheKey = object.returnCacheKey();


				onBeforeLoad(null, object);


				onBeforeFirstLoad(object);
				allCacheMap.put(cacheKey, object);
				if (currentMaxRefreshCompareValue == null || currentMaxRefreshCompareValue.compareTo(object.returnCacheRefreshCompareValue()) < 0) {
					currentMaxRefreshCompareValue = object.returnCacheRefreshCompareValue();
				}
			}
			List<K> existedKeys = new ArrayList<K>(this.cacheMap.keySet());
			existedKeys.removeAll(allCacheMap.keySet());
			for (K removedKey : existedKeys) {
				onBeforeDelete(this.cacheMap.get(removedKey));
			}
			Map<K, T> oldCacheMap = this.cacheMap;
			this.cacheMap = allCacheMap;
			for (K removedKey : existedKeys) {
				T removeObject = oldCacheMap.get(removedKey);
				if (removeObject != null) {
					onDelete(removeObject);
				}
			}
		}
		// 如果是增量加载
		else {
			for (T object : objectList) {
				K cacheKey = object.returnCacheKey();
				boolean exists = this.cacheMap.containsKey(object.returnCacheKey());
				T oldObject = this.cacheMap.get(cacheKey);
				// 先触发事件，如果有异常会报错，保证一定读到数据放入缓存
				onBeforeLoad(oldObject, object);
				if (exists) {

					onBeforeReload(oldObject, object);
				} else {

					onBeforeFirstLoad(object);
				}
				// 更新cache
				this.cacheMap.put(cacheKey, object);
				if (currentMaxRefreshCompareValue == null || currentMaxRefreshCompareValue.compareTo(object.returnCacheRefreshCompareValue()) < 0) {
					currentMaxRefreshCompareValue = object.returnCacheRefreshCompareValue();
				}

				onLoad(oldObject, object);
				if (exists) {

					onReload(oldObject, object);
				} else {

					onFirstLoad(object);
				}
			}

			// 同步一次所有的key，可能数据库中被删除了
			List<K> allKeys = this.loadAllKeys();

			if (allKeys != null) {
				removeKeys = new ArrayList<K>(this.cacheMap.keySet());
				removeKeys.removeAll(allKeys);
				if (!cacheChange) {
					cacheChange = removeKeys.size() > 0;
				}
				for (K removedKey : removeKeys) {
					T removeObject = this.cacheMap.get(removedKey);
					if (removeObject != null) {

						onBeforeDelete(removeObject);
					}
				}
				for (K removedKey : removeKeys) {
					T removeObject = this.cacheMap.remove(removedKey);
					if (removeObject != null) {

						onDelete(removeObject);
					}
				}
			}
		}
		maxRefreshCompareValue = currentMaxRefreshCompareValue;
		onRefreshCacheFinish(objectList, removeKeys, allLoad);
		return objectList.size();
	}

	@PostConstruct
	public void initializing() {
		// TODO Auto-generated method stub
		refreshCache();
		initialized = true;
	}

	@Override
	public int refreshAll() {
		// TODO Auto-generated method stub
		maxRefreshCompareValue = null;
		return this.refreshCache();
	}

	public boolean isInitialized() {
		return initialized;
	}


	@Override
	public List<T> getAllEnabled() {
		// TODO Auto-generated method stub
		return this.getAll().stream().filter(p -> p.isEnabled()).collect(Collectors.toList());
	}
}
