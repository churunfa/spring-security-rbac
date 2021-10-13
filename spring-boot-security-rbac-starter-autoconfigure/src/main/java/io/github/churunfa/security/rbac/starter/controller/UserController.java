package io.github.churunfa.security.rbac.starter.controller;

import io.github.churunfa.security.exception.PermissionException;
import io.github.churunfa.security.exception.rbac.PermissionNotFoundException;
import io.github.churunfa.security.rbac.starter.model.RestResult;
import io.github.churunfa.security.rbac.starter.model.RestResultUtils;
import io.github.churunfa.security.rbac.starter.service.rbac.RbacService;
import io.github.churunfa.security.rbac.starter.user.Permission;
import io.github.churunfa.security.rbac.starter.user.RbacUser;
import io.github.churunfa.security.rbac.starter.user.Role;
import io.github.churunfa.security.utils.PageBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author crf
 */
@RestController
@RequestMapping("/api/rbac/user")
public class UserController {

    private final RbacService rbacService;

    public UserController(RbacService rbacService) {
        this.rbacService = rbacService;
    }

    @GetMapping("info")
    public RestResult info(Authentication authentication) {
        authentication.getDetails();
        Object user = authentication.getPrincipal();
        return RestResultUtils.success(user);
    }

    @GetMapping("hasRole/{name}")
    public RestResult<Boolean> hasRole(@PathVariable("name") String name, Authentication authentication) {
        RbacUser user = (RbacUser) authentication.getPrincipal();
        if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_root"))) {
            return RestResultUtils.success(true);
        }
        return RestResultUtils.success(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + name)));
    }

    @GetMapping("hasPermission/{name}")
    public RestResult<Boolean> hasPermission(@PathVariable("name") String name, Authentication authentication) {
        RbacUser user = (RbacUser) authentication.getPrincipal();
        if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_root"))) {
            return RestResultUtils.success(true);
        }
        return RestResultUtils.success(user.getAuthorities().contains(new SimpleGrantedAuthority(name)));
    }

    @GetMapping("count")
    public RestResult<Integer> getUserCount() {
        return RestResultUtils.success(rbacService.getUserCount());
    }

    @GetMapping("role")
    public RestResult<List> getRoles() {
        return RestResultUtils.success(rbacService.getRoles());
    }

    @GetMapping("user")
    public RestResult<PageBean> getUser(@RequestParam("name") String name, @RequestParam("role") String role, @RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize) {
        PageBean<Object> pageBean = new PageBean<>();
        if (pageNo != null) {
            pageBean.setPageNo(pageNo);
        }

        if (pageSize != null) {
            pageBean.setPageSize(pageSize);
        }
        rbacService.listUser(pageBean, name, role);
        return RestResultUtils.success(pageBean);
    }

    @PutMapping("user")
    public RestResult updateUser(@RequestBody RbacUser user) {
        System.out.println(user);
        try {
            rbacService.updateUser(user.getId(), user.getUsername(), user.getPassword());
            return RestResultUtils.success();
        } catch (Exception e) {
            return RestResultUtils.failed(e.getMessage());
        }
    }

    @PutMapping("accountNonExpired")
    public RestResult updateAccountNonExpired(@RequestBody RbacUser rbacUser) {
        Integer id = rbacUser.getId();
        boolean accountNonExpired = rbacUser.isAccountNonExpired();
        try {
            rbacService.updateAccountNonExpired(id, accountNonExpired);
        } catch (Exception e) {
            return RestResultUtils.failed(e.getMessage());
        }
        return RestResultUtils.success();
    }

    @PutMapping("accountNonLocked")
    public RestResult updateAccountNonLocked(@RequestBody RbacUser rbacUser) {
        Integer id = rbacUser.getId();
        boolean accountNonLocked = rbacUser.isAccountNonLocked();
        try {
            rbacService.updateAccountNonLocked(id, accountNonLocked);
        } catch (Exception e) {
            return RestResultUtils.failed(e.getMessage());
        }
        return RestResultUtils.success();
    }

    @PutMapping("enabled")
    public RestResult updateEnabled(@RequestBody RbacUser rbacUser) {
        Integer id = rbacUser.getId();
        boolean enabled = rbacUser.isEnabled();
        try {
            rbacService.updateEnabled(id, enabled);
        } catch (Exception e) {
            return RestResultUtils.failed(e.getMessage());
        }
        return RestResultUtils.success();
    }

    @GetMapping("user/role/{id}")
    public RestResult<List> listUserRole(@PathVariable("id") int id) {
        return RestResultUtils.success(rbacService.getUserRole(id));
    }

    @PutMapping("grant/role/{id}")
    @Transactional
    public RestResult grantUserRole(@PathVariable("id") Integer uid, @RequestBody Integer[] roles) {
        for (Integer role : roles) {
            try {
                rbacService.grantUserRole(uid, role);
            } catch (PermissionException e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return RestResultUtils.failed(e.getMessage());
            }
        }
        return RestResultUtils.success();
    }

    @PutMapping("cancel/role/{id}")
    @Transactional
    public RestResult cancelUserRole(@PathVariable("id") Integer uid, @RequestBody Integer[] roles) {
        for (Integer role : roles) {
            try {
                rbacService.cancelUserRole(uid, role);
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return RestResultUtils.failed(e.getMessage());
            }
        }
        return RestResultUtils.success();
    }

    @PostMapping("user")
    public RestResult register(@RequestBody RbacUser user) {
        try {
            String username = user.getUsername();
            if (username == null || username.length() == 0) {
                return RestResultUtils.failed("用户名不能为空");
            }
            String password = user.getPassword();
            if (password == null || password.length() == 0) {
                return RestResultUtils.failed("密码不能为空");
            }
            return RestResultUtils.success(rbacService.register(username, password));
        } catch (Exception e) {
            return RestResultUtils.failed(e.getMessage());
        }
    }

    @PostMapping("role")
    public RestResult addRole(@RequestBody Role role) {
        try {
            rbacService.addRole(role);
            return RestResultUtils.success();
        } catch (Exception e) {
            return RestResultUtils.failed(e.getMessage());
        }
    }

    @PutMapping("role")
    public RestResult updateRole(@RequestBody Role role) {
        try {
            return RestResultUtils.success(rbacService.updateRole(role));
        } catch (Exception e) {
            return RestResultUtils.failed(e.getMessage());
        }
    }

    @DeleteMapping("role/{id}")
    public RestResult deleteRole(@PathVariable("id") Integer id) {
        try {
            rbacService.roleDelete(id);
            return RestResultUtils.success();
        } catch (Exception e) {
            return RestResultUtils.failed(e.getMessage());
        }
    }

    @GetMapping("permission")
    public RestResult<PageBean> getPermission(@RequestParam("name") String name, @RequestParam("role") String role, @RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize){
        PageBean<Object> pageBean = new PageBean<>();
        if (pageNo != null) {
            pageBean.setPageNo(pageNo);
        }

        if (pageSize != null) {
            pageBean.setPageSize(pageSize);
        }
        rbacService.listPermission(pageBean, name, role);
        return RestResultUtils.success(pageBean);
    }

    @PostMapping("permission")
    public RestResult addPermission(@RequestBody Permission permission) {
        try {
            rbacService.addPermission(permission);
            return RestResultUtils.success();
        } catch (Exception e) {
            return RestResultUtils.failed(e.getMessage());
        }
    }

    @PutMapping("permission/{id}")
    public RestResult updatePermission(@PathVariable("id") Integer id, @RequestBody Permission permission) {
        permission.setId(id);
        try {
            rbacService.updatePermission(permission);
            return RestResultUtils.success();
        } catch (PermissionException e) {
            return RestResultUtils.failed(e.getMessage());
        }
    }

    @DeleteMapping("permission/{id}")
    public RestResult deletePermission(@PathVariable Integer id) {
        try {
            rbacService.permissionDelete(id);
            return RestResultUtils.success();
        } catch (PermissionException e) {
            return RestResultUtils.failed(e.getMessage());
        }
    }

    @GetMapping("permission/{id}/roles")
    public RestResult<List> listPermissionAssociatedRole(@PathVariable Integer id) {
        return RestResultUtils.success(rbacService.listPermissionAssociatedRole(id));
    }

    @PutMapping("grant/permission/{id}")
    @Transactional
    public RestResult grantPermissionRole(@PathVariable("id") Integer pid, @RequestBody Integer[] roles) {
        for (Integer role : roles) {
            try {
                rbacService.grantRolePermission(role, pid);
            } catch (PermissionException e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return RestResultUtils.failed(e.getMessage());
            }
        }
        return RestResultUtils.success();
    }

    @PutMapping("cancel/permission/{id}")
    @Transactional
    public RestResult cancelPermissionRole(@PathVariable("id") Integer pid, @RequestBody Integer[] roles) {
        for (Integer role : roles) {
            try {
                rbacService.cancelRolePermission(role, pid);
            } catch (PermissionNotFoundException e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return RestResultUtils.failed(e.getMessage());
            }
        }
        return RestResultUtils.success();
    }
}
