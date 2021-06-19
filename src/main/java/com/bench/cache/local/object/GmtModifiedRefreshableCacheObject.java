/**
 * BenchCode.com Inc.
 * Copyright (c) 2005-2009 All Rights Reserved.
 */
package com.bench.cache.local.object;

import java.util.Date;

/**
 * 基于修改时间的可刷新
 * 
 * @author cold
 *
 * @version $Id: GmtModifiedRefreshableCacheObject.java, v 0.1 2018年3月7日
 *          下午4:24:25 cold Exp $
 */
public interface GmtModifiedRefreshableCacheObject<K> extends RefreshableCacheObject<K, Date> {

	/**
	 * 返回修改时间
	 * 
	 * @return
	 */
	public Date getGmtModified();

	public default Date returnCacheRefreshCompareValue() {
		// TODO Auto-generated method stub
		return this.getGmtModified();
	}

}
