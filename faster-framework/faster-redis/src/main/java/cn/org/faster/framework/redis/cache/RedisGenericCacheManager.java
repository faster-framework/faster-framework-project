package cn.org.faster.framework.redis.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.lang.reflect.Type;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


/**
 * 自定义泛型cache manager
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RedisGenericCacheManager extends RedisCacheManager {
    private CacheProperties cacheProperties;
    private ObjectMapper objectMapper;

    /**
     * 存储泛型类型，key为cacheName，value为返回泛型
     */
    private Map<String, Type> genericCacheMap = new HashMap<>();
    private final RedisCacheWriter cacheWriter;
    private final RedisCacheConfiguration defaultCacheConfig;

    public RedisGenericCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfiguration;
    }

    public RedisGenericCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, String... initialCacheNames) {
        super(cacheWriter, defaultCacheConfiguration, initialCacheNames);
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfiguration;
    }

    public RedisGenericCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, boolean allowInFlightCacheCreation, String... initialCacheNames) {
        super(cacheWriter, defaultCacheConfiguration, allowInFlightCacheCreation, initialCacheNames);
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfiguration;
    }

    public RedisGenericCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, Map<String, RedisCacheConfiguration> initialCacheConfigurations) {
        super(cacheWriter, defaultCacheConfiguration, initialCacheConfigurations);
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfiguration;
    }

    public RedisGenericCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, Map<String, RedisCacheConfiguration> initialCacheConfigurations, boolean allowInFlightCacheCreation) {
        super(cacheWriter, defaultCacheConfiguration, initialCacheConfigurations, allowInFlightCacheCreation);
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfiguration;
    }


    /**
     * 放入泛型cache
     *
     * @param genericCacheMap genericCacheMap
     */
    public void initGenericCacheMap(Map<String, Type> genericCacheMap) {
        this.genericCacheMap = genericCacheMap;
    }

    /**
     * 重置超时时间，如果cacheName中存在超时设置，使用，否则使用默认
     *
     * @param cacheName
     * @return
     */
    private String resetExpirationAndName(String cacheName) {
        if (StringUtils.isEmpty(cacheName)) {
            return cacheName;
        }
        String[] values = cacheName.split("#");
        if (values.length > 1) {
            //存在超时设置
            cacheProperties.getRedis().setTimeToLive(Duration.ofSeconds(Long.parseLong(values[1])));
        }
        return values[0];
    }

    protected RedisCache createRedisCache(String name, @Nullable RedisCacheConfiguration cacheConfig) {
        //使用cacheName从genericCacheMap获取泛型类型，如果失败，使用默认方式创建cache。如果成功，创建泛型cache
        Type type = this.genericCacheMap.get(name);
        //重置超时时间和cacheName
        String cacheName = resetExpirationAndName(name);
        if (type == null) {
            return super.createRedisCache(cacheName, cacheConfig);
        }
        return new RedisGenericCache(cacheName, cacheWriter, determineConfiguration(type));
    }

    private <T> RedisCacheConfiguration determineConfiguration(
            Type type) {
        CacheProperties.Redis redisProperties = this.cacheProperties.getRedis();
        RedisCacheConfiguration config = RedisCacheConfiguration
                .defaultCacheConfig();
        Jackson2JsonRedisSerializer<T> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(TypeFactory.defaultInstance().constructType(type));
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        config = config.serializeValuesWith(RedisSerializationContext.SerializationPair
                .fromSerializer(jackson2JsonRedisSerializer));
        if (redisProperties.getTimeToLive() != null) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }
        if (redisProperties.getKeyPrefix() != null) {
            config = config.prefixKeysWith(redisProperties.getKeyPrefix());
        }
        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }
        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }
        return config;
    }
}
