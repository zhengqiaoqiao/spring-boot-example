package org.example.server.system.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("sys.cache")
public class CacheProperties {
	//是否开启缓存(0:关闭  1:开启)
	private int isCache;

	public int getIsCache() {
		return isCache;
	}
	public void setIsCache(int isCache) {
		this.isCache = isCache;
	}
}
