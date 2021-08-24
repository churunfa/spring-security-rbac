package io.github.churunfa.security.rbac.starter.service.security;

import io.jsonwebtoken.*;
import io.github.churunfa.security.rbac.starter.dao.RbacUserSecurityDao;
import io.github.churunfa.security.rbac.starter.properties.RbacProperties;
import io.github.churunfa.security.rbac.starter.service.rbac.RbacService;
import io.github.churunfa.security.utils.JwtUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author crf
 */
@Service
public class TokenManager {
    private final RbacProperties rbacProperties;

    private final RbacUserSecurityDao rbacUserSecurityDao;

    private final RbacService rbacService;

    public TokenManager(RbacProperties rbacProperties, RbacUserSecurityDao rbacUserSecurityDao, RbacService rbacService) {
        this.rbacProperties = rbacProperties;
        this.rbacUserSecurityDao = rbacUserSecurityDao;
        this.rbacService = rbacService;
    }

    public String creatToken(String username) {
        return creatToken(username, rbacProperties.getTokenExpiration());
    }

    public String creatToken(String username, Long exp) {
        String secret = rbacUserSecurityDao.getSecret(username);
        if (secret == null || "".equals(secret)) {
            secret = JwtUtils.getSecret();
            rbacUserSecurityDao.updateSecret(username, secret);
        }
        String jwt = Jwts.builder().setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + exp))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        return jwt;
    }

    public String parseJwt(String jwt, String username) {
        String secret = rbacUserSecurityDao.getSecret(username);
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(jwt).getBody().getSubject();
    }

    public String parseJwt(String jwt) {
        String username = JwtUtils.getUsername(jwt);
        return parseJwt(jwt, username);
    }

    public void removeToken(String username) {
        rbacService.updateSecret(username);
    }

}
