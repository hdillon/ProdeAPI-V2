package com.solution.prode.configuration

import com.solution.prode.constants.RedisTtl.DAY
import java.time.Duration
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CachingConfigurerSupport
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.interceptor.CacheErrorHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import redis.clients.jedis.JedisPoolConfig

@Configuration
@EnableCaching
class CacheConfiguration : CachingConfigurerSupport() {

    private val logger = LoggerFactory.getLogger(CacheConfiguration::class.java)

    @Value("\${REDIS_HOST}")
    private val redisHost: String = ""

    @Value("\${REDIS_PORT}")
    private val redisPort: Int = 0

    @Value("\${REDIS_MAX_CONNECTIONS}")
    private val maxConnections: Int = 10

    @Value("\${REDIS_MIN_IDLE}")
    private val minIdle: Int = 2

    @Value("\${REDIS_MAX_IDLE}")
    private val maxIdle: Int = 4

    @Value("\${REDIS_CONNECTION_TIME_OUT}")
    private val connectionTimeOut: Long = 10

    @Value("\${REDIS_DEFAULT_EXPIRE_TIME_SEG}")
    private val defaultExpireTime: Long = 300

    @Value("\${REDIS_MAX_WAIT_MILLIS}")
    private val redisMaxWait: Long = 5000

    @Value("\${REDIS_MIN_EVICTABLE_TABLE_TIME_MILLIS}")
    private val redisMinEvictableIdleTimeMillis: Long = 300000

    fun jedisPoolConfig(): JedisPoolConfig {

        val poolConfiguration = JedisPoolConfig()

        poolConfiguration.maxTotal = maxConnections
        poolConfiguration.minIdle = minIdle
        poolConfiguration.maxIdle = maxIdle
        poolConfiguration.maxWaitMillis = redisMaxWait
        poolConfiguration.minEvictableIdleTimeMillis = redisMinEvictableIdleTimeMillis
        poolConfiguration.blockWhenExhausted = true

        return poolConfiguration
    }

    fun redisConnectionFactory(): RedisConnectionFactory {

        val jedisClientConfiguration = JedisClientConfiguration.builder()
            .connectTimeout(Duration.ofSeconds(connectionTimeOut))
            .usePooling()
            .poolConfig(this.jedisPoolConfig())
            .build()

        return JedisConnectionFactory(RedisStandaloneConfiguration(redisHost, redisPort), jedisClientConfiguration)
    }

    fun redisCacheConfiguration(): RedisCacheConfiguration {

        val serializationPair = RedisSerializationContext.SerializationPair.fromSerializer(
            JdkSerializationRedisSerializer()
        )

        return RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofSeconds(defaultExpireTime))
            .serializeValuesWith(serializationPair)
            .disableCachingNullValues()
    }

    @Bean
    override fun cacheManager(): CacheManager {
        return RedisCacheManager
            .builder(this.redisConnectionFactory())
            .cacheDefaults(redisCacheConfiguration())
            .transactionAware()
            .build()
    }

    fun getCacheManagerByTtl(ttl: Long): RedisCacheManager {
        val configuration = this.redisCacheConfiguration().entryTtl(Duration.ofSeconds(ttl))

        return RedisCacheManager.builder(this.redisConnectionFactory())
            .cacheDefaults(configuration)
            .build()
    }

    @Bean
    fun cacheManagerOneDay(): CacheManager = getCacheManagerByTtl(DAY)

    @Bean
    override fun errorHandler(): CacheErrorHandler {
        return object : CacheErrorHandler {
            override fun handleCacheClearError(exception: java.lang.RuntimeException, cache: Cache) {
                logger.warn("Error on clear cache: '{}'", exception.message)
            }

            override fun handleCachePutError(exception: java.lang.RuntimeException, cache: Cache, key: Any, value: Any?) {
                logger.warn("Error on put value in cache: '{}'", exception.message)
            }

            override fun handleCacheEvictError(exception: java.lang.RuntimeException, cache: Cache, key: Any) {
                logger.warn("Error on evict cache value: '{}'", exception.message)
            }

            override fun handleCacheGetError(exception: RuntimeException, cache: Cache, key: Any) {
                logger.warn("Error on get cache value: '{}'", exception.message)
            }
        }
    }
}
