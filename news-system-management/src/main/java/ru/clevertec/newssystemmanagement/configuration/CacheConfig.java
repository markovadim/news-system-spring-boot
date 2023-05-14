package ru.clevertec.newssystemmanagement.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import ru.clevertec.newssystemmanagement.cache.BaseSystemCache;
import ru.clevertec.newssystemmanagement.cache.LFUCache;
import ru.clevertec.newssystemmanagement.cache.LRUCache;

@Configuration
public class CacheConfig {

    @Bean
    @Profile("!prod")
    public BaseSystemCache<?, ?> cacheLRU() {
        return new LRUCache<>();
    }

    @Bean
    @Profile("!prod")
    public BaseSystemCache<?, ?> cacheLFU() {
        return new LFUCache<>();
    }

    @Bean
    @Profile("prod")
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig();
    }
}
