package org.crf.security.rbac.starter.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.crf.security.rbac.starter.dao.RbacUserSecurityDao;
import org.crf.security.rbac.starter.model.RestResult;
import org.crf.security.rbac.starter.model.RestResultUtils;
import org.crf.security.rbac.starter.service.rbac.RbacService;
import org.crf.security.rbac.starter.service.security.TokenManager;
import org.crf.security.rbac.starter.user.RbacUser;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author crf
 */
public class TokenAuthFilter extends BasicAuthenticationFilter {

    private final TokenManager tokenManager;

    private final RbacUserSecurityDao rbacUserSecurityDao;

    private final RbacService rbacService;

    public TokenAuthFilter(AuthenticationManager authenticationManager, TokenManager tokenManager, RbacUserSecurityDao rbacUserSecurityDao, RbacService rbacService) {
        super(authenticationManager);
        this.tokenManager = tokenManager;
        this.rbacUserSecurityDao = rbacUserSecurityDao;
        this.rbacService = rbacService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        UsernamePasswordAuthenticationToken authRequest = null;

        try {
            authRequest = getAuthentication(request);
        } catch (Exception e) {
            onUnsuccessfulAuthentication(request, response, null);
            return;
        }

        if (authRequest != null) {
            SecurityContextHolder.getContext().setAuthentication(authRequest);
        }
        chain.doFilter(request, response);
    }

    @Override
    protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        RestResult<String> restResult = RestResultUtils.failed(40001,"token失效", "token失效");
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write(new ObjectMapper().writeValueAsString(restResult));
        out.flush();
        out.close();
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String jwt = request.getHeader(HttpHeaders.AUTHORIZATION);
        String username = "anonymousUser";
        if (jwt != null && jwt.length() != 0) {
            username = tokenManager.parseJwt(jwt);
        }
        RbacUser user = rbacService.getUserByUserName(username);
        user.setAuthorities(rbacService.getAuthorities(user.getId()));
        return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
    }
}
