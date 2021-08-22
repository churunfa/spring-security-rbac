package org.crf.security.rbac.starter.service;

import org.crf.security.rbac.starter.dao.RbacUserSecurityDao;
import org.crf.security.rbac.starter.service.rbac.RbacService;
import org.crf.security.rbac.starter.user.Permission;
import org.crf.security.rbac.starter.user.RbacUser;
import org.crf.security.rbac.starter.user.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @author crf
 */
@Service
public class RbacUserServiceImpl implements UserDetailsService {

    private final PasswordEncoder pw;

    private final RbacUserSecurityDao rbacUserSecurityDao;

    private final RbacService rbacService;

    @Autowired
    public RbacUserServiceImpl(PasswordEncoder pw, RbacUserSecurityDao rbacUserSecurityDao, RbacService rbacService) {
        this.pw = pw;
        this.rbacUserSecurityDao = rbacUserSecurityDao;
        this.rbacService = rbacService;
    }

    @Override
    public RbacUser loadUserByUsername(String username) throws UsernameNotFoundException {
        RbacUser user = rbacService.getUserByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("");

        }

        Collection<? extends GrantedAuthority> authorities = rbacService.getAuthorities(user.getId());
        user.setAuthorities(authorities);

        return user;
    }
}
