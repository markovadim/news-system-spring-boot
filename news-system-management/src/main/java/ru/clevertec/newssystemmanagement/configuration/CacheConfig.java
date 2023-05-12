package ru.clevertec.newssystemmanagement.configuration;


import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.clevertec.newssystemmanagement.cache.BaseSystemCache;
import ru.clevertec.newssystemmanagement.cache.LFUCache;
import ru.clevertec.newssystemmanagement.cache.LRUCache;

@Configuration
public class CacheConfig {

    @Bean
    @ConditionalOnProperty(value = "cache", havingValue = "LRU", matchIfMissing = true)
    public BaseSystemCache<?, ?> cacheLRU() {
        return new LRUCache<>();
    }

    @Bean
    @ConditionalOnProperty(value = "cache", havingValue = "LFU", matchIfMissing = true)
    public BaseSystemCache<?, ?> cacheLFU() {
        return new LFUCache<>();
    }
}
