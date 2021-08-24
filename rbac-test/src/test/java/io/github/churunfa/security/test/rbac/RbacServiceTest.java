package org.crf.security.test.rbac;

import org.crf.security.test.rbac.pojo.User;
import org.crf.security.utils.PageBean;
import org.junit.jupiter.api.Test;
import org.crf.security.exception.rbac.*;
import org.crf.security.rbac.starter.service.rbac.RbacService;
import org.crf.security.rbac.starter.user.Permission;
import org.crf.security.rbac.starter.user.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class RbacServiceTest {

    @Autowired
    RbacService rbacService;

    @Test
    void getUserPermission() {
        List<Permission> permissions = rbacService.getUserPermission(5);
        System.out.println(permissions);
    }

    @Test
    void addPermission() {
        try {
            int i = rbacService.addPermission(new Permission("lv1", null, null));
        } catch (PermissionIsExist e) {
            e.printStackTrace();
        }
    }

    @Test
    void deletePermission() throws PermissionReferenceIsNotZero {
        int i = rbacService.permissionDelete(7);
    }

    @Test
    void deletePermissionByName() throws PermissionNotFoundException, PermissionReferenceIsNotZero {
        int root = rbacService.permissionDelete("root");
    }

    @Test
    void addRoleTest() throws RoleIsExist, RoleNameIsNull {
        int i = rbacService.addRole(new Role("root", "all", "root角色"));
    }

    @Test
    void grantUserPermission() throws PermissionNotFoundException {
        int i = rbacService.grantUserPermission(5, 9);
        System.out.println(i);
    }

    @Test
    void grantUserRole() throws RoleNotFoundException, RoleError {
        rbacService.grantUserRole(5, 5);
    }

    @Test
    void grantRolePermission() throws RoleNotFoundException, PermissionNotFoundException {
        int i = rbacService.grantRolePermission(5, 10);
    }

    @Test
    void grantRolePermissionByName() throws RoleNotFoundException, PermissionNotFoundException {
        int i = rbacService.grantRolePermissionByName("root", "root");
        System.out.println(i);
    }

    @Test
    void cancelUserPermission() throws PermissionNotFoundException {
        rbacService.cancelUserPermission(5, 9);
    }

    @Test
    void cancelUserRole() throws RoleNotFoundException, AnonymousRoleDeleteException {
        rbacService.cancelUserRole(5, 5);
    }

    @Test
    void cancelRolePermission() throws PermissionNotFoundException {
        rbacService.cancelRolePermission(5, 10);
    }

    @Test
    void roleDelete() throws RoleReferenceIsNotZero, RoleError, RoleNotFoundException {
        rbacService.roleDelete(5);
    }

    @Test
    void permissionDelete() throws PermissionReferenceIsNotZero {
        rbacService.permissionDelete(9);
    }

    @Test
    void listUserTest() {
        PageBean<User> pageBean = new PageBean<>();
        pageBean.setPageSize(1);
        rbacService.listUser(pageBean, null, null);
        System.out.println(pageBean);
    }
}

