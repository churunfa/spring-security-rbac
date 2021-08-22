package org.crf.security.rbac.starter.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.crf.security.rbac.starter.user.Role;

import java.util.List;

/**
 * @author crf
 */
@Mapper
public interface RbacRoleSecurityMapper {

    /**
     * 根据id查询role
     * @param rid 角色名
     * @return 角色
     */
    Role getRoleById(int rid);

    /**
     * 根据role名查询role
     * @param name 角色名
     * @return 角色
     */
    Role getRoleByName(String name);

    /**
     * 插入一个新的角色
     * @param role 角色信息
     * @return 影响行数
     */
    int insertRole(Role role);

    /**
     * 更新角色信息
     * @param role 角色信息
     * @return 影响行数
     */
    int updateRole(Role role);

    /**
     * 获取拥有某角色的用户数
     * @param rid 角色id
     * @return 用户数
     */
    int countRoleOnUser(int rid);

    /**
     * 获取拥有某角色的权限数目
     * @param rid 角色id
     * @return 用户数
     */
    int countRoleOnPermission(int rid);

    /**
     * 删除角色
     * @param rid 角色id
     * @return 影响行数
     */
    int deleteRole(int rid);

    /**
     * 删除角色
     * @param name 角色名
     * @return 影响行数
     */
    int deleteRoleByName(String name);

    /**
     * 获取用户的所有角色
     * @param uid 用户id
     * @return 角色列表
     */
    List<Role> listUserRole(int uid);

    /**
     * 获取所有角色
     * @return 角色列表
     */
    List<Role> listRole();

    /**
     * 获取权限关联的所有角色
     * @param id 权限id
     * @return 角色列表
     */
    List<Integer> listRidByPid(Integer id);
}
