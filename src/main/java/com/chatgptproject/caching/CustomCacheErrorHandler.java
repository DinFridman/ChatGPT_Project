package com.chatgptproject.caching;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

@Log4j2
public class CustomCacheErrorHandler implements CacheErrorHandler {

    @Override
    public void handleCacheGetError(@NotNull RuntimeException exception, Cache cache, @NotNull Object key) {
        log.error("[Caching] Error getting key " + key + " from cache " + cache.getName(), exception);
    }

    @Override
    public void handleCachePutError(@NotNull RuntimeException exception, Cache cache, @NotNull Object key, Object value) {
        log.error("[Caching] Error putting key " + key + " in cache " + cache.getName(), exception);
    }

    @Override
    public void handleCacheEvictError(@NotNull RuntimeException exception, Cache cache, @NotNull Object key) {
        log.error("[Caching] Error evicting key " + key + " from cache " + cache.getName(), exception);
    }

    @Override
    public void handleCacheClearError(@NotNull RuntimeException exception, Cache cache) {
        log.error("[Caching] Error clearing cache " + cache.getName(), exception);
    }
}
