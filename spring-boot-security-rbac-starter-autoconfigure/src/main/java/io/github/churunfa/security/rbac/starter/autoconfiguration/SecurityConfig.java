package io.github.churunfa.security.rbac.starter.autoconfiguration;

import io.github.churunfa.security.rbac.starter.dao.RbacUserSecurityDao;
import io.github.churunfa.security.rbac.starter.filter.TokenAuthFilter;
import io.github.churunfa.security.rbac.starter.service.rbac.RbacService;
import io.github.churunfa.security.rbac.starter.service.security.TokenManager;
import io.github.churunfa.security.rbac.starter.service.security.UnAuthEntryPoint;
import org.mybatis.spring.annotation.MapperScan;
import io.github.churunfa.security.rbac.starter.filter.LoginFilter;
import io.github.churunfa.security.rbac.starter.properties.RbacProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author crf
 */
@Configuration
@EnableCaching
@ComponentScan("io.github.churunfa.security.rbac.starter")
@MapperScan("io.github.churunfa.security.rbac.starter.mapper")
@EnableConfigurationProperties(RbacProperties.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final RbacService rbacService;
    private final TokenManager tokenManager;
    private final RbacUserSecurityDao rbacUserSecurityDao;
    private final RbacProperties rbacProperties;

    public SecurityConfig(RbacService rbacService, TokenManager tokenManager, RbacUserSecurityDao rbacUserSecurityDao, RbacProperties rbacProperties) {
        this.rbacService = rbacService;
        this.tokenManager = tokenManager;
        this.rbacUserSecurityDao = rbacUserSecurityDao;
        this.rbacProperties = rbacProperties;
    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
    }

    private static final String[] AUTH_WHITELIST = {
            // -- swagger ui
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v2/api-docs",
            "/webjars/**",
            "/v3/api-docs"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).disable();

        http.exceptionHandling().authenticationEntryPoint(new UnAuthEntryPoint());

        String matchUrl = rbacProperties.getUriPrefix();
        if (matchUrl.length() > 0) {
            if (matchUrl.endsWith("/")) {
                matchUrl += "**";
            } else {
                matchUrl += "/**";
            }
        }

        http.authorizeRequests()
                .antMatchers(rbacProperties.getAuthWhitelist()).permitAll()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers(matchUrl)
                .access("@defaultAuthRule.hasPermission(request, authentication)");

        http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilter(tokenAuthFilter())
                .httpBasic();
    }

    @Bean
    LoginFilter loginFilter() throws Exception {
        LoginFilter filter = new LoginFilter(tokenManager, rbacService);
        filter.setFilterProcessesUrl("/api/login");
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

    @Bean
    TokenAuthFilter tokenAuthFilter() throws Exception {
        return new TokenAuthFilter(authenticationManagerBean(), tokenManager, rbacUserSecurityDao, rbacService);
    }

}
