package site.dbin.fileswitch.util;

import com.github.benmanes.caffeine.cache.*;

import java.util.concurrent.TimeUnit;

/**
 * Caffeine cache 工厂
 * @author dbin
 */
public class CaffeineFactory {
    /**
     * 手动加载缓存
     * @param expireTime 过期时间 单位 s
     * @param maxSize 最大容量
     * @return 缓存对象示例
     */
    public static  <K extends Object,V extends Object> Cache<K, V> getCache(long expireTime, long maxSize){
        if(maxSize<=0){
            maxSize = 50L;
        }
        if(expireTime<=0){
            expireTime = 100L;
        }
        return Caffeine.newBuilder()
                .expireAfterWrite(expireTime, TimeUnit.SECONDS)
                .maximumSize(maxSize)
                .build();
    }

    /**
     * 同步加载缓存
     * @param expireTime 过期时间
     * @param maxSize 最大容量
     * @param cacheLoader 加载器
     * @return 缓存对象示例
     */
    public static  <K extends Object,V extends Object> LoadingCache<K,V> getLoadingCache(long expireTime,
                                                                                         long maxSize,
                                                                                         CacheLoader<K,V> cacheLoader) {
        if(maxSize<=0){
            maxSize = 50L;
        }
        if(expireTime<=0){
            expireTime = 100L;
        }
        return Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expireTime, TimeUnit.SECONDS)
                .build(cacheLoader);
    }

    /**
     * 异步加载缓存
     * @param expireTime 过期时间
     * @param maxSize 最大容量
     * @param cacheLoader 加载器
     * @return 缓存对象示例
     */
    public static  <K extends Object,V extends Object> AsyncLoadingCache<K, V> getAsyncLoadingCache(long expireTime,
                                                                                                    long maxSize,
                                                                                                    CacheLoader<K,V> cacheLoader){
        if(maxSize<=0){
            maxSize = 50L;
        }
        if(expireTime<=0){
            expireTime = 100L;
        }
        return Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expireTime, TimeUnit.SECONDS)
                .buildAsync(cacheLoader);
    }
}