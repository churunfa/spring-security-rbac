package org.crf.security.rbac.starter.dao;

import org.crf.security.rbac.starter.user.RbacUser;

import java.util.List;

/**
 * @author crf
 */
public interface RbacUserSecurityDao {

    /**
     * 根据用户名查询用户
     * @param id 用户id
     * @param <T> 用户类型
     * @return 用户信息
     */
    <T> T getUserById(Integer id);

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @param <T> 用户类型
     * @return 用户信息
     */
    <T> T getUserByUserName(String username);

    /**
     * 获取用户数量
     * @return 用于数量
     */
    int getUserCount();

    /**
     * 插入用户
     * @param user
     * @return 影响行数
     */
    int insert(RbacUser user);

    /**
     * 查询用户jwt加密密钥
     * @param username 用户名
     * @return jwt
     */
    String getSecret(String username);

    /**
     * 查询用户jwt加密密钥
     * @param id 用户id
     * @return jwt
     */
    String getSecret(Integer id);

    /**
     * 更新用户密钥
     * @param username 用户名
     * @param secret 密钥
     * @return 影响行数
     */
    int updateSecret(String username, String secret);

    /**
     * 更新用户密钥
     * @param id 用户id
     * @param secret 密钥
     * @return 影响行数
     */
    int updateSecret(Integer id, String secret);

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
     * 获取用户列表
     * @param st limit参数1
     * @param limit limit参数2
     * @return 用户列表
     */
    List listUser(int st, int limit);


    /**
     * 根据用户名匹配用户
     * @param name 用户名
     * @param st limit参数
     * @param limit limit参数
     * @return 用户列表
     */
    List listUserLikeName(String name, int st, int limit);

    /**
     * 查询匹配用户名的用户数
     * @param name 用户名
     * @return 用户数
     */
    int countUserLikeName(String name);

    /**
     * 根据角色查询用户
     * @param roleName 角色名
     * @param st limit参数
     * @param limit limit参数
     * @return 用户列表
     */
    List listUserByRole(String roleName, int st, int limit);

    /**
     * 根据角色查询用户
     * @param roleName 角色名
     * @return 用户数量
     */
    int countUserByRole(String roleName);

    /**
     * 根据角色名和权限名查询用户
     * @param userName 用户名
     * @param roleName 角色名
     * @param st limit参数
     * @param limit limit参数
     * @return 用户列表
     */
    List listUserByUerNameAndRoleName(String userName, String roleName, int st, int limit);

    /**
     * 根据角色名和权限名查询用户数量
     * @param userName 用户名
     * @param roleName 角色名
     * @return 用户数
     */
    int countUserByUerNameAndRoleName(String userName, String roleName);

    /**
     * 更新用户名
     * @param id 用户id
     * @param username 新用户名
     * @return 影响行数
     */
    int updateUserName(Integer id, String username);

    /**
     * 更新用户密码
     * @param id 用户id
     * @param password 用户新密码
     * @return 影响行数
     */
    int updatePassword(Integer id, String password);

    /**
     *
     * 更新账户过期信息
     * @param id 用户id
     * @param accountNonExpired 是否过期
     * @return 影响行数
     */
    int updateAccountNonExpired(Integer id, boolean accountNonExpired);

    /**
     *
     * 更新账户锁定信息
     * @param id 用户id
     * @param accountNonLocked 是否锁定
     * @return 影响行数
     */
    int updateAccountNonLocked(Integer id, boolean accountNonLocked);

    /**
     *
     * 更新账户是否启用
     * @param id 用户id
     * @param enabled 是否启用
     * @return 影响行数
     */
    int updateEnabled(Integer id, boolean enabled);
}
