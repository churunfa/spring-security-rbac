package org.crf.security.rbac.starter.service.rbac.impl;

import com.alibaba.fastjson.JSON;
import org.crf.security.exception.PermissionException;
import org.crf.security.exception.rbac.*;
import org.crf.security.rbac.starter.dao.RbacUserSecurityDao;
import org.crf.security.rbac.starter.mapper.RbacPermissionSecurityMapper;
import org.crf.security.rbac.starter.mapper.RbacRoleSecurityMapper;
import org.crf.security.rbac.starter.properties.RbacProperties;
import org.crf.security.rbac.starter.service.rbac.RbacService;
import org.crf.security.rbac.starter.user.Permission;
import org.crf.security.rbac.starter.user.RbacUser;
import org.crf.security.rbac.starter.user.Role;
import org.crf.security.utils.JwtUtils;
import org.crf.security.utils.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author crf
 */
@Service
public class RbacServiceImpl implements RbacService {

    private final RbacRoleSecurityMapper roleMapper;

    private final RbacPermissionSecurityMapper permissionMapper;

    private final RbacUserSecurityDao rbacUserSecurityDao;

    private final RbacProperties rbacProperties;

    private final RedisTemplate redisTemplate;

    private final PasswordEncoder pe = new BCryptPasswordEncoder();

    @Autowired
    public RbacServiceImpl(RbacRoleSecurityMapper roleMapper, RbacPermissionSecurityMapper permissionMapper, RbacUserSecurityDao rbacUserSecurityDao, RbacProperties rbacProperties, RedisTemplate redisTemplate) {
        this.roleMapper = roleMapper;
        this.permissionMapper = permissionMapper;
        this.rbacUserSecurityDao = rbacUserSecurityDao;
        this.rbacProperties = rbacProperties;
        this.redisTemplate = redisTemplate;
    }

    private <T> T getUser(String json) {
        Class<T> userClass = rbacProperties.getRbacUserClass();
        return JSON.parseObject(json, userClass);
    }

    @Override
//    @Cacheable(value = "rbac::user", key = "#username",condition = "#result != null")
    public <T> T getUserByUserName(String username) {
        String key = "rbac::user::" + username;
        String json = (String) redisTemplate.opsForValue().get(key);
        T user = null;
        if (json != null) {
            user = getUser(json);
        }

        if (user == null) {
            user = rbacUserSecurityDao.getUserByUserName(username);
            String userJson = JSON.toJSONString(user);
            redisTemplate.opsForValue().set(key, userJson, 7, TimeUnit.DAYS);
        }
        return user;
    }

    @Override
    public List<Permission> getUserPermission(int uid) {
        List<Permission> userPermission = permissionMapper.listUserPermission(uid);
        HashSet<Permission> set = new HashSet<>(userPermission);

        List<Role> userRoles = this.getUserRole(uid);

        userRoles.forEach(role -> set.addAll(this.getRolePermission(role.getId())));
        return new ArrayList<>(set);
    }

    @Override
    public List<Permission> getRolePermission(int rid) {
        return permissionMapper.listRolePermission(rid);
    }

    @Override
    public List<Role> getUserRole(int uid) {
        return roleMapper.listUserRole(uid);
    }

    @Override
    public int addPermission(Permission permission) throws PermissionIsExist {
        Permission permissionByName = permissionMapper.getPermissionByName(permission.getName());
        if (permissionByName != null) {
            throw new PermissionIsExist("权限已存在，插入失败");
        }
        return permissionMapper.insertPermission(permission);
    }

    @Override
    public int addRole(Role role) throws RoleIsExist, RoleNameIsNull {
        if (isNull(role.getName())) {
            throw new RoleNameIsNull("角色名不能为空");
        }
        Role roleByName = roleMapper.getRoleByName(role.getName());
        if (roleByName != null) {
            throw new RoleIsExist("角色已存在，插入失败");
        }
         return roleMapper.insertRole(role);
    }

    @Override
    public int grantUserPermission(int uid, int pid) throws PermissionNotFoundException {
        Permission permission = permissionMapper.getPermissionById(pid);

        if (permission == null) {
            throw new PermissionNotFoundException("权限不存在");
        }

        int i = permissionMapper.checkPermissionByUidAndPid(uid, permission.getId());
        if (i != 0) {
            return permissionMapper.updatePermissionInUser(uid, permission);
        }
        return permissionMapper.insertPermissionInUser(uid, permission);
    }

    @Override
    public int grantUserRole(int uid, int rid) throws RoleNotFoundException, RoleError {
        String key = "rbac::authorities::" + uid;
        redisTemplate.delete(key);

        Role role = roleMapper.getRoleById(rid);

        if (role == null) {
            throw new RoleNotFoundException("角色不存在");
        }

        RbacUser user = rbacUserSecurityDao.getUserById(uid);
        if (user == null || (!"ANONYMOUS".equals(role.getName()) && "anonymousUser".equals(user.getUsername()))) {
            throw new RoleError("匿名用户只允许拥有匿名权限");
        }

        int i = permissionMapper.checkRoleByUidAndRid(uid, role.getId());
        if (i != 0) {
            return permissionMapper.updateRoleInUser(uid, role);
        }
        return permissionMapper.insertRoleInUser(uid, role);
    }

    @Override
    public int grantRolePermission(int rid, int pid) throws PermissionNotFoundException, RoleNotFoundException {
        Permission permission = permissionMapper.getPermissionById(pid);

        Set<String> keys = redisTemplate.keys("rbac::authorities::" + "*");
        redisTemplate.delete(keys);

        if (permission == null) {
            throw new PermissionNotFoundException("权限不存在");
        }

        Role role = roleMapper.getRoleById(rid);

        if (role == null) {
            throw new RoleNotFoundException("角色不存在");
        }

        int i = permissionMapper.checkPermissionInRole(rid, permission.getId());
        if (i != 0) {
            return permissionMapper.updatePermissionInRole(rid, permission);
        }
        return permissionMapper.insertPermissionInRole(rid, permission);
    }

    @Override
    public int grantRolePermissionByName(String rName, String pName) throws PermissionNotFoundException, RoleNotFoundException {
        Permission permission = permissionMapper.getPermissionByName(pName);

        if (permission == null) {
            throw new PermissionNotFoundException("权限不存在");
        }

        Role role = roleMapper.getRoleByName(rName);

        if (role == null) {
            throw new RoleNotFoundException("角色不存在");
        }

        int i = permissionMapper.checkPermissionInRole(permission.getId(), permission.getId());
        if (i != 0) {
            return permissionMapper.updatePermissionInRole(role.getId(), permission);
        }
        return permissionMapper.insertPermissionInRole(permission.getId(), permission);
    }

    @Override
    public int cancelUserPermission(int uid, int pid) throws PermissionNotFoundException {
        Set<String> keys = redisTemplate.keys("rbac::authorities::" + "*");
        redisTemplate.delete(keys);
        Permission permission = permissionMapper.getPermissionById(pid);

        if (permission == null) {
            throw new PermissionNotFoundException("权限不存在");
        }

        return permissionMapper.deletePermissionInUser(uid, pid);
    }

    @Override
    public int cancelUserRole(int uid, int rid) throws RoleNotFoundException, AnonymousRoleDeleteException {
        String key = "rbac::authorities::" + uid;
        redisTemplate.delete(key);
        Role role = roleMapper.getRoleById(rid);
        if (role == null) {
            throw new RoleNotFoundException("角色不存在");
        }

        if ("ANONYMOUS".equals(role.getName())) {
            throw new AnonymousRoleDeleteException("匿名角色禁止被删除");
        }

        return permissionMapper.deleteRoleOnUser(uid, rid);
    }

    @Override
    public int cancelRolePermission(int rid, int pid) throws PermissionNotFoundException {
        Set<String> keys = redisTemplate.keys("rbac::authorities::" + "*");
        redisTemplate.delete(keys);
        Permission permission = permissionMapper.getPermissionById(pid);

        if (permission == null) {
            throw new PermissionNotFoundException("权限不存在");
        }
        return permissionMapper.deletePermissionInRole(rid, permission.getId());
    }

    @Override
    public int roleDelete(int rid) throws RoleReferenceIsNotZero, RoleNotFoundException, RoleError {

        Role roleById = roleMapper.getRoleById(rid);

        if (roleById == null) {
            throw new RoleNotFoundException("角色不存在");
        }

        if ("root".equals(roleById.getName())) {
            throw new RoleError("root角色不允许被删除");
        }

        if ("ANONYMOUS".equals(roleById.getName())) {
            throw new RoleError("匿名角色不允许被删除");
        }

        int countRoleOnUser = roleMapper.countRoleOnUser(rid);

        if (countRoleOnUser != 0) {
            throw new RoleReferenceIsNotZero("删除失败，有用户正在引用该角色");
        }

        int countRoleOnPermission = roleMapper.countRoleOnPermission(rid);
        if (countRoleOnPermission != 0) {
            throw new RoleReferenceIsNotZero("删除失败，改角色的权限列表不为空");
        }
        return roleMapper.deleteRole(rid);
    }

    @Override
    public int permissionDelete(int pid) throws PermissionReferenceIsNotZero {

        int countPermissionOnRole = permissionMapper.countPermissionOnRole(pid);

        if (countPermissionOnRole != 0) {
            throw new PermissionReferenceIsNotZero("删除失败，有角色正在引用该权限");
        }

        int countPermissionOnUser = permissionMapper.countPermissionOnUser(pid);
        if (countPermissionOnUser != 0) {
            throw new PermissionReferenceIsNotZero("删除失败，有用户正在引用该权限");
        }

        return permissionMapper.deletePermission(pid);
    }

    @Override
    public int roleDelete(String roleName) throws RoleNotFoundException, RoleReferenceIsNotZero, RoleError {
        Role roleByName = roleMapper.getRoleByName(roleName);

        if (roleByName == null) {
            throw new RoleNotFoundException("角色不存在");
        }

        return roleDelete(roleByName.getId());
    }

    @Override
    public int permissionDelete(String permissionName) throws PermissionNotFoundException, PermissionReferenceIsNotZero {
        Permission permission = permissionMapper.getPermissionByName(permissionName);

        if (permission == null) {
            throw new PermissionNotFoundException("权限不存在");
        }

        return permissionDelete(permission.getId());
    }

    @Override
//    @Cacheable(value = "rbac::authorities", condition = "#id > 0")
    public Collection<? extends GrantedAuthority> getAuthorities(int id) {
        String key = "rbac::authorities::" + id;
        String resJson = (String) redisTemplate.opsForValue().get(key);
        if (resJson != null) {
            List<Map> list = JSON.parseObject(resJson, List.class);

            List<SimpleGrantedAuthority> res = new ArrayList<>();
            for (Map map : list) {
                res.add(new SimpleGrantedAuthority((String) map.get("authority")));
            }
            return res;
        }

        List<Permission> permissions = this.getUserPermission(id);
        List<Role> roles = this.getUserRole(id);

        List<String> pList = new ArrayList<>();

        permissions.forEach(permission -> pList.add(permission.getName()));
        roles.forEach(role -> pList.add("ROLE_" + role.getName()));

        String[] pArr = new String[pList.size()];
        pList.toArray(pArr);

        List<GrantedAuthority> res = AuthorityUtils.createAuthorityList(pArr);
        redisTemplate.opsForValue().set(key, JSON.toJSONString(res), 7, TimeUnit.DAYS);
        return res;
    }

    @Override
    public int updateLoginTime(String username) {
        return rbacUserSecurityDao.updateLoginTime(username);
    }

    @Override
    public int updateLoginTime(Integer id) {
        return rbacUserSecurityDao.updateLoginTime(id);
    }

    @Override
    public int getUserCount() {
        return rbacUserSecurityDao.getUserCount();
    }

    @Override
    public List<Role> getRoles() {
        return roleMapper.listRole();
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void updateUser(Integer id, String username, String password) {
        RbacUser user = rbacUserSecurityDao.getUserById(id);

        if (isNull(password) && isNull(username)) {
            return;
        }

        if (isNull(password) && username.equals(user.getUsername())) {
            return;
        }

        if ("anonymousUser".equals(user.getUsername())) {
            throw new RuntimeException("anonymousUser为匿名用户，不允许修改。");
        }

        updateSecret(username);
        if (!isNull(username)) {
            rbacUserSecurityDao.updateUserName(id, username);
        }
        if (!isNull(password)) {
            password = pe.encode(password);
            rbacUserSecurityDao.updatePassword(id, password);
        }
    }

    @Override
    public int updateSecret(String username) {
        String key = "rbac::user::" + username;
        redisTemplate.delete(key);
        String secret = JwtUtils.getSecret();
        return rbacUserSecurityDao.updateSecret(username, secret);
    }

    @Override
    public int updateAccountNonExpired(Integer id, boolean accountNonExpired) {
        RbacUser user = rbacUserSecurityDao.getUserById(id);
        if ("anonymousUser".equals(user.getUsername())) {
            throw new RuntimeException("anonymousUser为匿名用户，不允许修改。");
        }
        return rbacUserSecurityDao.updateAccountNonExpired(id, accountNonExpired);
    }

    @Override
    public int updateAccountNonLocked(Integer id, boolean accountNonLocked) {
        RbacUser user = rbacUserSecurityDao.getUserById(id);
        if ("anonymousUser".equals(user.getUsername())) {
            throw new RuntimeException("anonymousUser为匿名用户，不允许修改。");
        }
        return rbacUserSecurityDao.updateAccountNonLocked(id, accountNonLocked);
    }

    @Override
    public int updateEnabled(Integer id, boolean enabled) {
        RbacUser user = rbacUserSecurityDao.getUserById(id);
        if ("anonymousUser".equals(user.getUsername())) {
            throw new RuntimeException("anonymousUser为匿名用户，不允许修改。");
        }
        return rbacUserSecurityDao.updateEnabled(id, enabled);
    }

    @Override
    public <T> T register(String username, String password) throws UserExist {

        RbacUser userByUserName = getUserByUserName(username);
        if (userByUserName != null) {
            throw new UserExist("用户名已存在");
        }

        RbacUser user = new RbacUser(username, pe.encode(password), true, true, true, true, null, new Date(), new Date(), null);
        rbacUserSecurityDao.insert(user);
        Role anonymousRole = getAnonymousRole();

        try {
            grantUserRole(user.getId(), anonymousRole.getId());
        } catch (PermissionException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
        }


        return (T) user;
    }

    @Override
    public Role getAnonymousRole() {
        return roleMapper.getRoleByName("ANONYMOUS");
    }

    @Override
    public int updateRole(Role role) throws RoleNotFoundException, RoleReferenceIsNotZero, RoleError, RoleNameIsNull, RoleIsExist {
        if (role.getId() == null) {
            throw new RoleNotFoundException("角色id为空");
        }

        if (isNull(role.getName())) {
            throw new RoleNameIsNull("角色名不能为空");
        }

        Role roleById = roleMapper.getRoleById(role.getId());

        Role roleByName = roleMapper.getRoleByName(role.getName());

        if (roleByName != null && !roleById.getId().equals(roleByName.getId())) {
            throw new RoleIsExist("角色名已存在");
        }

        if (roleById == null) {
            throw new RoleNotFoundException("角色不存在");
        }

        if ("root".equals(roleById.getName())) {
            throw new RoleError("root角色不允许修改");
        }

        if ("ANONYMOUS".equals(role.getName())) {
            throw new RoleError("匿名角色不允许修改");
        }

        int count1 = roleMapper.countRoleOnUser(role.getId());
        if (count1 != 0) {
            throw new RoleReferenceIsNotZero("有用户正在引用改角色，删除失败");
        }

        int count2 = roleMapper.countRoleOnPermission(role.getId());
        if (count2 != 0) {
            throw new RoleReferenceIsNotZero("角色权限列表不为空，删除失败");
        }

        return roleMapper.updateRole(role);
    }

    @Override
    public PageBean<Object> listUser(PageBean pageBean, String name, String role) {
        int st = (pageBean.getPageNo() - 1) * pageBean.getPageSize();

        List list = null;
        if (isNull(name) && isNull(role)) {
            pageBean.setRecordCount(getUserCount());
            pageBean.init();
            list = rbacUserSecurityDao.listUser(st, pageBean.getPageSize());
            pageBean.setPageData(list);
            return pageBean;
        }

        if (!isNull(name) && isNull(role)) {
            int count = rbacUserSecurityDao.countUserLikeName(name);
            pageBean.setRecordCount(count);
            list = rbacUserSecurityDao.listUserLikeName(name, st, pageBean.getPageSize());
            pageBean.setPageData(list);
            return pageBean;
        }

        if (isNull(name) && !isNull(role)) {
            int count = rbacUserSecurityDao.countUserByRole(role);
            pageBean.setRecordCount(count);
            list = rbacUserSecurityDao.listUserByRole(role, st, pageBean.getPageSize());
            pageBean.setPageData(list);
            return pageBean;
        }

        int count = rbacUserSecurityDao.countUserByUerNameAndRoleName(name, role);
        pageBean.setRecordCount(count);
        list = rbacUserSecurityDao.listUserByUerNameAndRoleName(name, role, st, pageBean.getPageSize());
        pageBean.setPageData(list);
        return pageBean;
    }

    @Override
    public PageBean<Object> listPermission(PageBean<Object> pageBean, String name, String role) {
        int st = (pageBean.getPageNo() - 1) * pageBean.getPageSize();
        List list = null;
        if (isNull(name) && isNull(role)) {
            pageBean.setRecordCount(permissionMapper.countPermission());
            pageBean.init();
            list = permissionMapper.listPermission(st, pageBean.getPageSize());
            pageBean.setPageData(list);
            return pageBean;
        }

        if (isNull(role)) {
            int count = permissionMapper.countPermissionLikeName(name);
            pageBean.setRecordCount(count);
            pageBean.init();
            list = permissionMapper.listPermissionLikeName(name, st, pageBean.getPageSize());
            pageBean.setPageData(list);
            return pageBean;
        }

        int count = permissionMapper.countPermissionLikeRole(role);

        pageBean.setRecordCount(count);
        pageBean.init();
        list = permissionMapper.listPermissionLikeRole(role, st, pageBean.getPageSize());

        for (Object o : list) {
            Permission permission = (Permission) o;
            Permission permissionById = permissionMapper.getPermissionById(permission.getId());
            ((Permission) o).setName(permissionById.getName());
            ((Permission) o).setDescribe(permissionById.getDescribe());
            ((Permission) o).setResource(permissionById.getResource());

            Role roleByName = roleMapper.getRoleByName(role);
            permissionMapper.updatePermissionInRole(roleByName.getId(), permissionById);
        }

        pageBean.setPageData(list);
        return pageBean;
    }

    @Override
    public int updatePermission(Permission permission) throws PermissionReferenceIsNotZero, PermissionIsExist {
        int count = permissionMapper.countPermissionOnRole(permission.getId());
        if (count != 0) {
            throw new PermissionReferenceIsNotZero("有角色正在引用该权限，更新失败");
        }
        count = permissionMapper.countPermissionOnUser(permission.getId());
        if (count != 0) {
            throw new PermissionReferenceIsNotZero("有用户正在引用该权限，更新失败");
        }

        Permission permissionByName = permissionMapper.getPermissionByName(permission.getName());
        if (permissionByName != null) {
            throw new PermissionIsExist("权限名已存在，更新失败");
        }

        return permissionMapper.updatePermission(permission);
    }

    @Override
    public List<Integer> listPermissionAssociatedRole(Integer id) {
        return roleMapper.listRidByPid(id);
    }

    private boolean isNull(String x) {
        return x == null ||x.length() == 0;
    }

}
