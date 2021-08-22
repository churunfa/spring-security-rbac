package org.crf.security.rbac.starter.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.crf.security.rbac.starter.model.RestResult;
import org.crf.security.rbac.starter.model.RestResultUtils;
import org.crf.security.rbac.starter.service.rbac.RbacService;
import org.crf.security.rbac.starter.service.security.TokenManager;
import org.crf.security.rbac.starter.user.RbacUser;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @author crf
 */

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private TokenManager tokenManager;
    private RbacService rbacService;

    public LoginFilter() {
    }

    public LoginFilter(TokenManager tokenManager, RbacService rbacService) {
        super();
        this.tokenManager = tokenManager;
        this.rbacService = rbacService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!"POST".equals(request.getMethod())) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }
        if (MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(request.getContentType()) || MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(request.getContentType()) || MediaType.APPLICATION_JSON_UTF8_VALUE.equalsIgnoreCase(request.getContentType())) {
            try {
                Map<String, String> loginData = new ObjectMapper().readValue(request.getInputStream(), Map.class);
                String username = loginData.get(getUsernameParameter());
                String password = loginData.get(getPasswordParameter());
                if (username == null) {
                    username = "";
                }
                if (password == null) {
                    password = "";
                }
                username = username.trim();
                System.out.println(username);
                System.out.println(password);
                UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                        username, password);
                setDetails(request, authRequest);
                return this.getAuthenticationManager().authenticate(authRequest);
            } catch (IOException e) {
                throw new AuthenticationServiceException(
                        "Json解析失败：" + e.getMessage());
            }finally {

            }

        } else {
            return super.attemptAuthentication(request, response);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        RbacUser user = (RbacUser) authResult.getPrincipal();

        String jwt = tokenManager.creatToken(user.getUsername());
        rbacService.updateLoginTime(user.getUsername());

        String s = new ObjectMapper().writeValueAsString(RestResultUtils.success(jwt));
        out.write(s);
        out.flush();
        out.close();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        RestResult<String> restResult = RestResultUtils.failed();
        if (exception instanceof LockedException) {
            restResult.setMessage("账户被锁定，请联系管理员!");
        } else if (exception instanceof CredentialsExpiredException) {
            restResult.setMessage("密码过期，请联系管理员!");
        } else if (exception instanceof AccountExpiredException) {
            restResult.setMessage("账户过期，请联系管理员!");
        } else if (exception instanceof DisabledException) {
            restResult.setMessage("账户被禁用，请联系管理员!");
        } else if (exception instanceof BadCredentialsException || exception instanceof UsernameNotFoundException) {
            restResult.setMessage("用户名或者密码输入错误，请重新输入!");
        } else {
            restResult.setMessage(exception.getMessage());
        }
        out.write(new ObjectMapper().writeValueAsString(restResult));
        out.flush();
        out.close();
    }
}
