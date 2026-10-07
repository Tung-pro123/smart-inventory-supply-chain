package com.tung.inventory.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class CacheService {

    private final CacheManager cacheManager;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Evict all cache entries for a specific cache
     */
    @CacheEvict(value = "products", allEntries = true)
    public void evictProductsCache() {
        log.info("Evicting products cache");
    }

    @CacheEvict(value = "warehouses", allEntries = true)
    public void evictWarehousesCache() {
        log.info("Evicting warehouses cache");
    }

    @CacheEvict(value = "low-stock", allEntries = true)
    public void evictLowStockCache() {
        log.info("Evicting low-stock cache");
    }

    @CacheEvict(value = "dashboard", allEntries = true)
    public void evictDashboardCache() {
        log.info("Evicting dashboard cache");
    }

    /**
     * Evict inventory snapshot cache for specific product and warehouse
     */
    public void evictInventorySnapshotCache(Long productId, Long warehouseId) {
        String cacheKey = "inventory-snapshots::" + productId + "-" + warehouseId;
        try {
            if (redisTemplate.hasKey(cacheKey)) {
                redisTemplate.delete(cacheKey);
                log.info("Evicted cache key: {}", cacheKey);
            }
        } catch (Exception e) {
            log.warn("Failed to evict cache key {}: {}", cacheKey, e.getMessage());
        }
    }

    /**
     * Evict inventory snapshot cache by pattern (for batch operations)
     */
    public void evictInventorySnapshotCacheByPattern(String pattern) {
        try {
            Set<String> keys = redisTemplate.keys("inventory-snapshots::" + pattern);
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
                log.info("Evicted {} cache keys matching pattern: {}", keys.size(), pattern);
            }
        } catch (Exception e) {
            log.warn("Failed to evict cache by pattern {}: {}", pattern, e.getMessage());
        }
    }

    /**
     * Evict all caches
     */
    public void evictAllCaches() {
        Collection<String> cacheNames = cacheManager.getCacheNames();
        for (String cacheName : cacheNames) {
            var cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                cache.clear();
                log.info("Cleared cache: {}", cacheName);
            }
        }
    }

    /**
     * Get cache statistics (for monitoring)
     */
    public CacheStats getCacheStats() {
        CacheStats stats = new CacheStats();

        try {
            Long dbSize = redisTemplate.getConnectionFactory()
                    .getConnection().dbSize();
            stats.setRedisKeys(dbSize.intValue());
        } catch (Exception e) {
            log.warn("Failed to get Redis stats: {}", e.getMessage());
        }

        for (String cacheName : cacheManager.getCacheNames()) {
            var cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                try {
                    // Try to get cache statistics from Redis
                    String statsKey = "spring:stats:" + cacheName;
                    Object stats = redisTemplate.opsForValue().get(statsKey);
                    if (stats != null) {
                        stats.getCacheStats().put(cacheName, stats.toString());
                    }
                } catch (Exception e) {
                    // Cache stats not available
                }
            }
        }

        return stats;
    }

    /**
     * Warm up cache (preload frequently accessed data)
     */
    public void warmUpCache() {
        log.info("Cache warm-up started");

        // Warm up products cache
        evictProductsCache();

        // Warm up warehouses cache
        evictWarehousesCache();

        // Warm up low-stock cache
        evictLowStockCache();

        log.info("Cache warm-up completed");
    }

    /**
     * DTO for cache statistics
     */
    @lombok.Data
    public static class CacheStats {
        private int redisKeys;
        private java.util.Map<String, String> cacheStats = new java.util.HashMap<>();
    }
}
