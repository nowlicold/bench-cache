/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.bench.cache.local;

import com.yuan.common.cache.local.GmtModifiedRefreshableCacheObject;

import java.util.Date;

/**
 * 基于修改时间的cache对象
 * 
 * @author cold
 *
 * @version $Id: AbstractGmtModifiedRefreshableCacheComponent.java, v 0.1 2015年9月16日 下午5:41:43 cold Exp $
 */
public abstract class AbstractGmtModifiedRefreshableCacheComponent<T extends GmtModifiedRefreshableCacheObject<K>, K>
		extends AbstractRefreshableCacheComponent<T, K, Date>{


	/**
	 * 返回缓存数据的最大修改时间
	 * 
	 * @return
	 */
	public Date getMaxGmtModified() {
		Date maxGmtModified = null;
		for (T object : this.cacheMap.values()) {
			if (maxGmtModified == null || maxGmtModified.before(object.getGmtModified())) {
				maxGmtModified = object.getGmtModified();
			}
		}
		return maxGmtModified;
	}

}
