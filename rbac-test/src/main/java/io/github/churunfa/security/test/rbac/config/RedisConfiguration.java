package io.github.churunfa.security.test.rbac.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

//@Configuration
public class RedisConfiguration {

    RedisTemplate redisTemplate;

    public RedisConfiguration(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

//    @Bean
//    public RedisTemplate<String,Object> encodedRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
//        redisTemplate.setKeySerializer(stringSerializer);
//        redisTemplate.setValueSerializer(stringSerializer);
//        redisTemplate.setHashKeySerializer(stringSerializer);
//        redisTemplate.setHashValueSerializer(stringSerializer);
//        return redisTemplate;
//    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer<Object> ser = new Jackson2JsonRedisSerializer<>(Object.class);
        redisTemplate.setDefaultSerializer(ser);
        return redisTemplate;
    }

}
