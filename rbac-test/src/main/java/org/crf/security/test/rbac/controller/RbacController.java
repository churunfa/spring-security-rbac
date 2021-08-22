package org.crf.security.test.rbac.controller;

import org.crf.security.test.rbac.pojo.User;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author crf
 */
@RestController
public class RbacController {

    @GetMapping("test")
    public User test(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }
}
