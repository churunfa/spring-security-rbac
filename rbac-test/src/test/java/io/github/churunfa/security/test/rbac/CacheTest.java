package org.crf.security.test.rbac;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.crf.security.rbac.starter.service.rbac.RbacService;
import org.crf.security.rbac.starter.user.RbacUser;
import org.crf.security.test.rbac.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@SpringBootTest
public class CacheTest {

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void cacheSetTest() {
        RbacUser user = new RbacUser("root", "123456", true, true, true, true);
        redisTemplate.opsForValue().set("key", user);
    }

    @Test
    public void cacheGetTest() {
        Object key = redisTemplate.opsForValue().get("rbac::root");
        System.out.println(key);
    }

    @Autowired
    RbacService rbacService;

    @Test
    public void JsonTest() {
        Collection<? extends GrantedAuthority> authorities = rbacService.getAuthorities(19);
        System.out.println("====================");
        System.out.println(authorities);
    }

    @Test
    public void cacheTest() {
        RbacUser root = rbacService.getUserByUserName("root");
        System.out.println(root);
    }

    @Test
    public void cacheTest2() {
//        Collection<? extends GrantedAuthority> authorities = rbacService.getAuthorities(19);
//        System.out.println(authorities);
        User root = rbacService.getUserByUserName("root");
        System.out.println(root);
    }

    @Test
    public void JsonTest2() throws JsonProcessingException {
        RbacUser user = new RbacUser("root", "123456", true, true, true, true);
        ObjectMapper objectMapper = new ObjectMapper();
        // 将Java对象序列化为Json字符串

        String s = JSON.toJSONString(user);
        System.out.println(s);

        JSONObject jsonObject = JSON.parseObject(s);


//        String objectToJson = objectMapper.writeValueAsString(user);
//        System.out.println(objectToJson);
//        System.out.println(objectToJson);
        // 将Json字符串反序列化为Java对象
//        User user = objectMapper.readValue(objectToJson, User.class);
//        System.out.println(user);
    }

}
