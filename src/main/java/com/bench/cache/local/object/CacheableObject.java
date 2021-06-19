package com.bench.cache.local.object;

/**
 * 可cache的对象
 * 
 * @author cold
 *
 * @version $Id: CacheableObject.java, v 0.1 2018年8月9日 下午5:32:35 cold Exp $
 */
public interface CacheableObject<K> extends Cacheable {
	/**
	 * 返回cache的key
	 */
	public K returnCacheKey();

}
