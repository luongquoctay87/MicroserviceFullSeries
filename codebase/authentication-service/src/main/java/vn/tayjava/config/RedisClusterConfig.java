package vn.tayjava.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.net.InetAddress;
import java.util.List;

@Configuration
@EnableRedisRepositories
@Profile({"prod"})
@Slf4j(topic = "REDIS-CLUSTER")
public class RedisClusterConfig {

    @Value("${redis.host}")
    private String redisHost;
    @Value("${redis.port}")
    private int redisPort;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        log.info("Connecting to redis: endpoint={} port={}", redisHost, redisPort);

        String ipAddress = "";
        try {
            InetAddress inetAddress = InetAddress.getByName(redisHost);
            ipAddress = inetAddress.getHostAddress();
        } catch (Exception e) {
            log.info("Error get host address from Amazon endpoint");
        }
        log.info("Ip address : {} ", ipAddress);
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
        List<RedisNode> redisNodes = List.of(new RedisNode(ipAddress, redisPort));
        redisClusterConfiguration.setClusterNodes(redisNodes);
        redisClusterConfiguration.setMaxRedirects(3);
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(20);
        jedisPoolConfig.setMinIdle(5);
        jedisPoolConfig.setMaxTotal(50);
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisClusterConfiguration, jedisPoolConfig);
        jedisConnectionFactory.afterPropertiesSet();

        return jedisConnectionFactory;
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate() {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        // Self defined string Serializer and fastjson Serializer
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // jackson Serializer
        GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        // kv serialize
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(jsonRedisSerializer);
        // hash serialize
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(jsonRedisSerializer);
        redisTemplate.afterPropertiesSet();

        log.info("Redis connected !");
        return redisTemplate;
    }
}
