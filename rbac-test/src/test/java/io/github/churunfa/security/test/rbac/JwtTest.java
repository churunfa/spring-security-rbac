package org.crf.security.test.rbac;

import io.jsonwebtoken.*;
import org.crf.security.rbac.starter.dao.RbacUserSecurityDao;
import org.crf.security.rbac.starter.service.security.TokenManager;
import org.crf.security.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class JwtTest {

    @Autowired
    RbacUserSecurityDao rbacUserSecurityDao;

    @Autowired
    TokenManager tokenManager;

    @Test
    void getSecretTest() {
        String secret = rbacUserSecurityDao.getSecret(14);
        String root = rbacUserSecurityDao.getSecret("root");
        System.out.println(secret);
        System.out.println(root);
    }

    @Test
    void updateSecretTest() {
        String secret = JwtUtils.getSecret();
        rbacUserSecurityDao.updateSecret("root", secret);
    }

    @Test
    void createJwtTest() {
        String jwt = tokenManager.creatToken("root");
        System.out.println(jwt);
    }

    @Test
    void parseJwtTest() {
        String jwt = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyb290IiwiZXhwIjoxNjMwMzY5ODYzfQ.nD-O-eIqFmq6C7bhNdj25VrWIx4yRmmtZf8HsbhrQ54wfZewkRpWwTcz_ct9PeItmfVn6SxfP9dbhBnIFmbnhg";
        String root = tokenManager.parseJwt(jwt, "root");
        System.out.println(root);
    }

    @Test
    void parseJwtTest2() {
        String jwt = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyb290IiwiZXhwIjoxNjMwMTQ1NDIyfQ.Pyfl_TU7iASboJLs2kIBCeYTvKov4o9dXAs-iq-n3ltYWchCTCeiAF3W-5N8wIeG5W5RXppPzs2Ebx51yO4uYA";
        String key = "2D2w6slf/Huy5n9O6lv9A2ov01uoWoYD4pUOdAZRz17aA4ALEaHY3A==";
//        String s = tokenManager.parseJwt(jwt);
//        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt);
//        System.out.println(claimsJws.getBody().getSubject());
        String username = JwtUtils.getUsername(jwt);
        System.out.println(username);

    }

    @Test
    void removeTokenTest() {
//        tokenManager.removeToken("root");
        String pwd = "1234567";
        String ep = "$2a$10$tdQXj2gFSYYZZL0PIi.hauE.3JblyEAu5gV7bW0yZ26WCee5yOZ4S";
//        String encode = new BCryptPasswordEncoder().encode(pwd);
        boolean matches = new BCryptPasswordEncoder().matches("1234567", ep);
        System.out.println(matches);
//        System.out.println(encode);
    }
}
