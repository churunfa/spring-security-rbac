package org.crf.security.rbac.starter.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.crf.security.rbac.starter.user.Permission;
import org.crf.security.rbac.starter.user.Role;

import java.util.List;


/**
 * @author crf
 */

@Mapper
public interface RbacPermissionSecurityMapper {

    /**
     * 通过id查询权限
     * @param pid 权限id
     * @return 权限信息
     */
    Permission getPermissionById(int pid);

    /**
     * 通过name查询权限
     * @param name 权限名
     * @return 权限信息
     */
    Permission getPermissionByName(String name);

    /**
     * 插入一条新的权限
     * @param permission 权限信息
     * @return 影响行数
     */
    int insertPermission(Permission permission);

    /**
     * 判断数据库中是否存在某权限的记录（包括enable为0的字段）
     * @param uid 用户id
     * @param pid 权限id
     * @return 1表示存在，0表示不存在
     */
    int checkPermissionByUidAndPid(int uid, int pid);

    /**
     * 给User插入一条权限
     * @param uid 用户id
     * @param permission 权限信息
     * @return 影响行数
     */
    int insertPermissionInUser(@Param("uid") int uid, @Param("permission") Permission permission);

    /**
     * 更新user的权限信息
     * @param uid 用户id
     * @param permission 权限信息
     * @return 影响行数
     */
    int updatePermissionInUser(@Param("uid") int uid, @Param("permission") Permission permission);

    /**
     * 判断数据库中是否存在角色记录（包括enable为0的字段）
     * @param uid 用户id
     * @param rid 角色id
     * @return 1表示存在，0表示不存在
     */
    int checkRoleByUidAndRid(int uid, int rid);

    /**
     * 给User插入一个角色
     * @param uid 用户id
     * @param role 角色信息
     * @return 影响行数
     */
    int insertRoleInUser(@Param("uid") int uid, @Param("role") Role role);

    /**
     * 更新user的权限信息
     * @param uid 用户id
     * @param role 权限信息
     * @return 影响行数
     */
    int updateRoleInUser(@Param("uid") int uid, @Param("role") Role role);


    /**
     * 删除用户的某项权限
     * @param uid 用户id
     * @param pid 权限id
     * @return 影响行数
     */
    int deletePermissionInUser(@Param("uid") int uid, @Param("pid") int pid);

    /**
     * 判断数据库中是否存在某角色的记录（包括enable为0的字段）
     * @param uid 用户id
     * @param rid 角色id
     * @return 1表示存在，0表示不存在
     */
    int checkPermissionByUidAndRid(int uid, int rid);

    /**
     * 判断角色是否存在某条权限记录
     * @param rid 角色id
     * @param pid 权限id
     * @return 影响行数
     */
    int checkPermissionInRole(int rid, int pid);

    /**
     * 给Role插入一条权限
     * @param rid 角色id
     * @param permission 权限信息
     * @return 影响行数
     */
    int insertPermissionInRole(int rid, Permission permission);

    /**
     * 更新user的权限信息
     * @param rid 用户id
     * @param permission 权限信息
     * @return 影响行数
     */
    int updatePermissionInRole(int rid, Permission permission);

    /**
     * 删除角色的某项权限
     * @param rid 角色id
     * @param pid 权限id
     * @return 影响行数
     */
    int deletePermissionInRole(@Param("rid") int rid, @Param("pid") int pid);

    /**
     * 更新权限信息
     * @param permission 权限信息
     * @return 影响行数
     */
    int updatePermission(Permission permission);

    /**
     * 获取权限数
     * @return 权限数
     */
    int countPermission();

    /**
     * 根据权限名获取权限数量
     * @param name 权限名
     * @return 权限数量
     */
    int countPermissionLikeName(String name);

    /**
     * 获取拥有某权限的用户数
     * @param pid 权限id
     * @return 用户数
     */
    int countPermissionOnUser(int pid);

    /**
     * 获取拥有某权限的角色数
     * @param pid 权限id
     * @return 角色数
     */
    int countPermissionOnRole(int pid);

    /**
     * 获取某角色的权限数
     * @param name 角色名
     * @return 数量
     */
    int countPermissionLikeRole(String name);

    /**
     * 删除权限
     * @param pid 权限id
     * @return 影响行数
     */
    int deletePermission(int pid);

    /**
     * 删除权限
     * @param name 权限名
     * @return 影响行数
     */
    int deletePermissionByName(String name);

    /**
     * 删除用户的某个角色
     * @param uid 用户id
     * @param rid 角色id
     * @return 影响行数
     */
    int deleteRoleOnUser(int uid, int rid);

    /**
     * 获取用户的所有权限
     * @param uid 用户id
     * @return 权限列表
     */
    List<Permission> listUserPermission(int uid);

    /**
     * 获取角色的所有权限
     * @param rid 角色id
     * @return 权限列表
     */
    List<Permission> listRolePermission(int rid);

    /**
     * 获取权限列表
     * @param st limit参数
     * @param limit limit参数
     * @return 权限列表
     */
    List<Permission> listPermission(int st, int limit);

    /**
     * 根据角色名查询权限
     * @param name 角色名
     * @param st limit参数
     * @param limit limit参数
     * @return 权限列表
     */
    List<Permission> listPermissionLikeName(String name, int st, int limit);

    /**
     * 根据角色名查询
     * @param role 角色名
     * @param st limit参数
     * @param limit limit参数
     * @return 权限列表
     */
    List<Permission> listPermissionLikeRole(String role, int st, int limit);

}
