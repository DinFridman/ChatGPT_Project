package com.example.chatgptproject.caching;

import lombok.extern.log4j.Log4j2;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

@Log4j2
public class CustomCacheErrorHandler implements CacheErrorHandler {

    @Override
    public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
        log.error("[Caching] Error getting key " + key + " from cache " + cache.getName(), exception);
    }

    @Override
    public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
        log.error("[Caching] Error putting key " + key + " in cache " + cache.getName(), exception);
    }

    @Override
    public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
        log.error("[Caching] Error evicting key " + key + " from cache " + cache.getName(), exception);
    }

    @Override
    public void handleCacheClearError(RuntimeException exception, Cache cache) {
        log.error("[Caching] Error clearing cache " + cache.getName(), exception);
    }
}
