package io.github.churunfa.security.rbac.starter.service.rbac;

import io.github.churunfa.security.exception.rbac.*;
import io.github.churunfa.security.rbac.starter.user.Permission;
import io.github.churunfa.security.rbac.starter.user.Role;
import io.github.churunfa.security.utils.PageBean;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

/**
 * @author crf
 */
public interface RbacService {

    /**
     * 根据用户名查询用户
     * @param username
     * @param <T>
     * @return
     */
    <T> T getUserByUserName(String username);


    /**
     * 获取用户所有权限（包括role权限）
     * @param uid 用户id
     * @return 权限列表
     */
    List<Permission> getUserPermission(int uid);

    /**
     * 获取角色的所有权限
     * @param rid 角色id
     * @return 权限列表
     */
    List<Permission> getRolePermission(int rid);

    /**
     * 获取用户角色列表
     * @param uid 用户id
     * @return 角色列表
     */
    List<Role> getUserRole(int uid);


    /**
     * 添加权限信息
     * @param permission 权限信息
     * @return 影响行数
     * @throws PermissionIsExist 权限已存在
     */
    int addPermission(Permission permission) throws PermissionIsExist;

    /**
     * 添加角色信息
     * @param role 角色信息
     * @return 是否成功
     * @throws RoleIsExist 角色已存在异常
     */
    int addRole(Role role) throws RoleIsExist, RoleNameIsNull;

    /**
     * 为用户授予Permission权限
     * @param uid 用户id
     * @param pid 权限id
     * @return 是否成功
     * @throws PermissionNotFoundException 权限不存在
     */
    int grantUserPermission(int uid, int pid) throws PermissionNotFoundException;

    /**
     * 为用户授予角色权限
     * @param uid 用户id
     * @param rid 角色id
     * @return 是否成功
     * @throws RoleNotFoundException 角色不存在
     * @throws RoleError 角色错误
     */
    int grantUserRole(int uid, int rid) throws RoleNotFoundException, RoleError;

    /**
     * 为角色授予权限
     * @param rid 角色id
     * @param pid 权限id
     * @return 是否成功
     * @throws PermissionNotFoundException 权限不存在
     * @throws RoleNotFoundException 角色不存在
     */
    int grantRolePermission(int rid, int pid) throws PermissionNotFoundException, RoleNotFoundException;

    /**
     * 为角色授予权限
     * @param rName 角色名
     * @param pName 权限名
     * @return 是否成功
     * @throws PermissionNotFoundException 权限不存在
     * @throws RoleNotFoundException 角色不存在
     */
    int grantRolePermissionByName(String rName, String pName) throws PermissionNotFoundException, RoleNotFoundException;

    /**
     * 取消用户的某项权限
     * @param uid 用户id
     * @param pid 权限id
     * @return 是否成功
     * @throws PermissionNotFoundException 权限不存在
     */
    int cancelUserPermission(int uid, int pid) throws PermissionNotFoundException;

    /**
     * 取消用户的某个角色
     * @param uid 用户id
     * @param rid 角色id
     * @return 是否成功
     * @throws RoleNotFoundException 角色不存在
     * @throws AnonymousRoleDeleteException 匿名用户不允许被删除
     */
    int cancelUserRole(int uid, int rid) throws RoleNotFoundException, AnonymousRoleDeleteException, RoleError;

    /**
     * 取消角色的某项权限
     * @param rid 角色id
     * @param pid 权限id
     * @return 是否成功
     * @throws PermissionNotFoundException 权限不存在
     */
    int cancelRolePermission(int rid, int pid) throws PermissionNotFoundException;

    /**
     * 删除角色，没有被用户引用时才会进行删除
     * @param rid 角色id
     * @return 是否删除成功
     * @throws RoleReferenceIsNotZero 角色被引用
     * @throws RoleNotFoundException 角色不存在
     * @throws RoleError 角色错误
     */
    int roleDelete(int rid) throws RoleReferenceIsNotZero, RoleNotFoundException, RoleError;

    /**
     * 删除权限
     * @param pid 权限id
     * @return 是否删除成功
     * @throws PermissionReferenceIsNotZero 权限被引用
     */
    int permissionDelete(int pid) throws PermissionReferenceIsNotZero;

    /**
     * 删除角色，没有被用户引用时才会进行删除
     * @param roleName 角色名
     * @return 是否删除成功
     * @throws RoleNotFoundException 角色不存在
     * @throws RoleReferenceIsNotZero 角色被引用
     */
    int roleDelete(String roleName) throws RoleNotFoundException, RoleReferenceIsNotZero, RoleError;

    /**
     * 删除权限
     * @param permissionName 权限名
     * @return 是否删除成功
     * @throws PermissionNotFoundException 权限不存在
     * @throws PermissionReferenceIsNotZero 权限被引用
     */
    int permissionDelete(String permissionName) throws PermissionNotFoundException, PermissionReferenceIsNotZero;

    /**
     * 获取权限集合
     * @param id 用户id
     * @return 集合
     */
    Collection<? extends GrantedAuthority> getAuthorities(int id);

    /**
     * 更新最后登陆时间
     * @param username 用户名
     * @return 影响行数
     */
    int updateLoginTime(String username);

    /**
     * 最后登陆时间
     * @param id 用户id
     * @return 影响行数
     */
    int updateLoginTime(Integer id);

    /**
     * 查询用户总数
     * @return 用户总数
     */
    int getUserCount();

    /**
     * 获取所有角色
     * @return 角色列表
     */
    List<Role> getRoles();


    /**
     * 获取用户列表
     * @param pageBean 分页信息
     * @param name 用户名
     * @param role 角色名
     * @return 用户列表
     */
    PageBean<Object> listUser(PageBean pageBean, String name, String role);

    /**
     * 更新用户信息
     * @param id 用户id
     * @param username 用户名
     * @param password 密码
     */
    void updateUser(Integer id, String username, String password);

    /**
     * 更新凭证
     * @param username 用户名
     * @return 影响行数
     */
    int updateSecret(String username);

    /**
     * 更新账户过期信息
     * @param id 用户id
     * @param accountNonExpired 是否过期
     * @return 影响行数
     */
    int updateAccountNonExpired(Integer id, boolean accountNonExpired);

    /**
     * 更新账户是否锁定
     * @param id 用户id
     * @param accountNonLocked 是否锁定
     * @return 影响行数
     */
    int updateAccountNonLocked(Integer id, boolean accountNonLocked);

    /**
     * 更新账户是否启用
     * @param id 用户id
     * @param enabled 是否启用
     * @return 影响行数
     */
    int updateEnabled(Integer id, boolean enabled);

    /**
     * 注册用户
     * @param username 用户名
     * @param password 密码
     * @param <T> 用户
     * @return 用户信息
     * @throws UserExist 用户已存在
     */
    <T> T register(String username, String password) throws UserExist;

    /**
     * 获取匿名角色
     * @return 角色
     */
    Role getAnonymousRole();

    /**
     * 更新角色信息
     * @param role 角色信息
     * @return 更新行数
     * @throws RoleNotFoundException 角色不存在
     * @throws RoleReferenceIsNotZero 角色引用不为空
     * @throws RoleNameIsNull 角色名为空
     * @throws RoleIsExist 角色名已存在
     * @throws RoleError 角色错误
     */
    int updateRole(Role role) throws RoleNotFoundException, RoleReferenceIsNotZero, RoleError, RoleNameIsNull, RoleIsExist;

    /**
     * 分页查询权限列表
     * @param pageBean 分页信息
     * @param name 权限名
     * @param role 角色名
     * @return  分页信息
     */
    PageBean<Object> listPermission(PageBean<Object> pageBean, String name, String role);

    /**
     * 更新权限信息
     * @param permission 权限信息
     * @return 影响行数
     * @throws PermissionReferenceIsNotZero 权限引用不为0，更新失败
     * @throws PermissionIsExist 权限名已存在
     */
    int updatePermission(Permission permission) throws PermissionReferenceIsNotZero, PermissionIsExist;

    /**
     * 获取权限关联的角色id
     * @param id 权限id
     * @return 角色列表
     */
    List<Integer> listPermissionAssociatedRole(Integer id);
}
