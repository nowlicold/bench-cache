/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.bench.cache.local.object;

import java.util.Date;

/**
 * 以id属性为主键，基于修改时间的可刷新
 * 
 * @author cold
 *
 * @version $Id: IdGmtModifiedRefreshableCacheObject.java, v 0.1 2018年3月7日
 *          下午4:24:25 cold Exp $
 */
public interface IdGmtModifiedRefreshableCacheObject<K> extends GmtModifiedRefreshableCacheObject<K> {

	/**
	 * 返回修改时间
	 * 
	 * @return
	 */
	public Date getGmtModified();

	/**
	 * 返回ID
	 * 
	 * @return
	 */
	public K getId();

	public default Date returnCacheRefreshCompareValue() {
		// TODO Auto-generated method stub
		return this.getGmtModified();
	}

	@Override
	public default K returnCacheKey() {
		// TODO Auto-generated method stub
		return this.getId();
	}

}
