package io.github.churunfa.security.test.rbac;

import org.junit.jupiter.api.Test;
import io.github.churunfa.security.rbac.starter.dao.RbacUserSecurityDao;
import io.github.churunfa.security.rbac.starter.mapper.RbacPermissionSecurityMapper;
import io.github.churunfa.security.rbac.starter.mapper.RbacRoleSecurityMapper;
import io.github.churunfa.security.rbac.starter.user.Permission;
import io.github.churunfa.security.rbac.starter.user.Role;
import io.github.churunfa.security.test.rbac.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest
class RbacTestApplicationTests {
    @Autowired
    RbacUserSecurityDao rbacUserSecurityDao;

    @Test
    void rbacUserRbacUserSecurityDaoTest() {
        User crf = rbacUserSecurityDao.getUserByUserName("crf");
        System.out.println(crf);
    }
    @Test
    void rbacUserRbacUserSecurityDaoCountTest() {
        int userCount = rbacUserSecurityDao.getUserCount();
        System.out.println(userCount);
    }

    @Test
    void insertTest() {
        User user = new User("crf", "123456", true, true, true, true);
        int insert = rbacUserSecurityDao.insert(user);
        System.out.println(insert);
    }

    @Autowired
    RbacPermissionSecurityMapper rbacPermissionSecurityMapper;

    @Test
    void getPermissionByIdTest() {
        Permission permission = rbacPermissionSecurityMapper.getPermissionById(1);
        System.out.println(permission);
    }

    @Test
    void insertPermissionTest() {
        Permission root = new Permission("addUser", null, "添加用户权限");
        int i = rbacPermissionSecurityMapper.insertPermission(root);
        System.out.println(i);
        System.out.println(root);
    }

    @Test
    void getPermissionTest() {
        Permission delUser = rbacPermissionSecurityMapper.getPermissionByName("delUser");
        System.out.println(delUser);
    }

    @Test
    void updatePermissionTest() {
        Permission permission = new Permission(2, "test1", "test1", "测试");
        int i = rbacPermissionSecurityMapper.updatePermission(permission);
        System.out.println(i);
    }

    @Test
    void countPermissionOnUserTest() {
        int addUser = rbacPermissionSecurityMapper.countPermissionOnUser(1);
        System.out.println(addUser);
    }

    @Test
    void countPermissionOnRoleTest() {
        int addUser = rbacPermissionSecurityMapper.countPermissionOnRole(1);
        System.out.println(addUser);
    }

    @Test
    void permissionDeleteTest() {
        int i = rbacPermissionSecurityMapper.deletePermission(2);
        System.out.println("res:" + i);
    }

    @Test
    void listUserPermissionTest() {
        List<Permission> permissions = rbacPermissionSecurityMapper.listUserPermission(5);
        System.out.println(permissions);
    }

    @Test
    void listRolePermissionTest() {
        List<Permission> permissions = rbacPermissionSecurityMapper.listRolePermission(1);
        System.out.println(permissions);
    }

    @Autowired
    RbacRoleSecurityMapper rbacRoleSecurityMapper;

    @Test
    void getRoleByIdTest() {
        Role role = rbacRoleSecurityMapper.getRoleById(1);
        System.out.println(role);
    }

    @Test
    void getRoleByNameTest() {
        Role vip1 = rbacRoleSecurityMapper.getRoleByName("vip1");
        System.out.println(vip1);
    }

    @Test
    void insertRoleTest() {
//        Role role = new Role("root", "all", "root用户拥有所有权限");
        Role role = new Role(1, "lv1", null, null);
        int i = rbacRoleSecurityMapper.insertRole(role);
        System.out.println(role);
        System.out.println(i);
    }

    @Test
    void updateRoleTest() {
        Role role = new Role(1, "lv1", null, null);
        int i = rbacRoleSecurityMapper.updateRole(role);
        System.out.println(i);
    }

    @Test
    void countRoleOnUserTest() {
        int i = rbacRoleSecurityMapper.countRoleOnUser(1);
        System.out.println(i);
    }

    @Test
    void deleteRoleTest() {
        int i = rbacRoleSecurityMapper.deleteRole(3);
        System.out.println(i);
    }

    @Test
    void listUserRoleTest() {
        List<Role> roles = rbacRoleSecurityMapper.listUserRole(5);
        System.out.println(roles);
    }

    @Test
    void checkPermissionByUidAndPid() {
        int i = rbacPermissionSecurityMapper.checkPermissionByUidAndPid(5, 5);
        System.out.println(i);
    }

    @Test
    void checkPermissionByUidAndRidTest() {
        int i = rbacPermissionSecurityMapper.checkPermissionByUidAndRid(5, 1);
        System.out.println(i);
    }

    @Test
    void insertPermissionInUser() {
        int i = rbacPermissionSecurityMapper.insertPermissionInUser(5, new Permission(1, "delUser", null, "删除用户权限"));
        System.out.println(i);
    }

    @Test
    void updatePermissionInUserTest() {
        int test = rbacPermissionSecurityMapper.updatePermissionInUser(5, new Permission(5, "test", null, null));
        System.out.println(test);
    }

    @Test
    void insertPermissionInRoleTest() {
        int i = rbacPermissionSecurityMapper.insertPermissionInRole(1, new Permission(1, "delUser", null, "删除用户权限"));
        System.out.println(i);
    }

    @Test
    void updatePermissionInRole() {
        int test = rbacPermissionSecurityMapper.updatePermissionInRole(1, new Permission(4, "test", null, null));
        System.out.println(test);
    }

    @Test
    void deletePermissionInUser() {
        int i = rbacPermissionSecurityMapper.deletePermissionInUser(5, 5);
        System.out.println(i);
    }

    @Test
    void deletePermissionInRoleTest() {
        int i = rbacPermissionSecurityMapper.deletePermissionInRole(1, 4);
        System.out.println(i);
    }

    @Test
    void checkRoleByUidAndPid() {
        int i = rbacPermissionSecurityMapper.checkRoleByUidAndRid(5, 2);
        System.out.println(i);
    }

    @Test
    void insertRoleInUserTest() {
        int i = rbacPermissionSecurityMapper.insertRoleInUser(5, new Role(2, "root", "all", "root用户拥有所有权限"));
        System.out.println(i);
    }

    @Test
    void updateRoleInUserTest() {
        int vip = rbacPermissionSecurityMapper.updateRoleInUser(5, new Role(1, "vip", null, null));
        System.out.println(vip);
    }

    @Test
    void checkPermissionInRoleTest() {
        int i = rbacPermissionSecurityMapper.checkPermissionInRole(1, 4);
        System.out.println(i);
    }

    @Test
    void deleteRoleOnUserTest() {
        int i = rbacPermissionSecurityMapper.deleteRoleOnUser(5, 1);
        System.out.println(i);
    }

    @Test
    void deletePermissionByNameTest() {
        int test2 = rbacPermissionSecurityMapper.deletePermissionByName("test2");
        System.out.println(test2);
    }

    @Test
    void deleteRoleByNameTest() {
        int admin = rbacRoleSecurityMapper.deleteRoleByName("admin");
        System.out.println(admin);
    }

    @Test
    void countRoleOnPermissionTest() {
        int i = rbacRoleSecurityMapper.countRoleOnPermission(1);
        System.out.println(i);
    }

    @Test
    void RandomTest() {
        List<Permission> users = new ArrayList<>();
        Permission permission1 = new Permission();
        permission1.setId(1);
        Permission permission2 = new Permission();
        permission1.setId(2);
        users.add(permission1);
        users.add(permission2);

        for (Permission user : users) {
            Permission permission = new Permission();
            permission.setId(3);
            user = permission;
        }

        for (Permission user : users) {
            System.out.println(user);
        }
    }

}
