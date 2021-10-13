package io.github.churunfa.security.rbac.starter.dao.impl;

import io.github.churunfa.security.rbac.starter.dao.RbacUserSecurityDao;
import io.github.churunfa.security.rbac.starter.properties.RbacProperties;
import io.github.churunfa.security.rbac.starter.user.RbacUser;
import io.github.churunfa.security.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * @author crf
 */
@Repository
public class RbacUserSecurityDaoImpl implements RbacUserSecurityDao {

    private final JdbcTemplate jdbcTemplate;

    private final RbacProperties rbacProperties;

    @Autowired
    public RbacUserSecurityDaoImpl(JdbcTemplate jdbcTemplate, RbacProperties rbacProperties) {
        this.jdbcTemplate = jdbcTemplate;
        this.rbacProperties = rbacProperties;
    }

    @Override
    public <T> T getUserById(Integer id) {
        String sql = String.format("select * from `%s` where id = ?", rbacProperties.getUserTableName());
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<T>(rbacProperties.getRbacUserClass()), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public <T> T getUserByUserName(String username) {
        String sql = String.format("select * from `%s` where username = ?", rbacProperties.getUserTableName());
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<T>(rbacProperties.getRbacUserClass()), username);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public int getUserCount() {
        String sql = String.format("select count(*) from `%s`", rbacProperties.getUserTableName());
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    @Override
    public int insert(RbacUser user) {
        String secret = JwtUtils.getSecret();
        user.setSecret(secret);
        String sql = String.format("insert into `%s`(username, password, account_non_expired, account_non_locked, credentials_non_expired, enabled, secret, gmt_create, gmt_modified) values(?, ?, ?, ?, ?, ?, ?, ?, ?)", rbacProperties.getUserTableName());
        KeyHolder keyHolder = new GeneratedKeyHolder();

        int cnt = jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setBoolean(3, user.isAccountNonExpired());
            ps.setBoolean(4, user.isAccountNonLocked());
            ps.setBoolean(5,  user.isCredentialsNonExpired());
            ps.setBoolean(6, user.isEnabled());
            ps.setString(7, user.getSecret());
            ps.setTimestamp(8, new Timestamp(user.getGmtCreate().getTime()));
            ps.setTimestamp(9, new Timestamp(user.getGmtModified().getTime()));
            return ps;
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());
        return cnt;
    }

    @Override
    public String getSecret(String username) {
        String sql = String.format("select secret from `%s` where username = ?", rbacProperties.getUserTableName());
        return jdbcTemplate.queryForObject(sql, String.class, username);
    }

    @Override
    public String getSecret(Integer id) {
        String sql = String.format("select secret from `%s` where id = ?", rbacProperties.getUserTableName());
        return jdbcTemplate.queryForObject(sql, String.class, id);
    }

    @Override
    public int updateSecret(String username, String secret) {
        String sql = String.format("update `%s` set `secret` = ?, `gmt_modified` = ? where username = ? ", rbacProperties.getUserTableName());
        return jdbcTemplate.update(sql, secret, new java.util.Date(), username);
    }

    @Override
    public int updateSecret(Integer id, String secret) {
        String sql = String.format("update `%s` set `secret` = ?, `gmt_modified` = ? where id = ? ", rbacProperties.getUserTableName());
        return jdbcTemplate.update(sql, secret, new java.util.Date(), id);
    }

    @Override
    public int updateLoginTime(String username) {
        String sql = String.format("update `%s` set `last_login` = ?  where username = ? ", rbacProperties.getUserTableName());
        return jdbcTemplate.update(sql, new java.util.Date(), username);
    }

    @Override
    public int updateLoginTime(Integer id) {
        String sql = String.format("update `%s` set `last_login` = ?  where id = ? ", rbacProperties.getUserTableName());
        return jdbcTemplate.update(sql, new java.util.Date(), id);
    }

    @Override
    public List listUser(int st, int limit) {
        String sql = String.format("select * from `%s` limit ?, ?", rbacProperties.getUserTableName());
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(rbacProperties.getRbacUserClass()) ,st, limit);
    }

    @Override
    public List listUserLikeName(String name, int st, int limit) {
        name += "%";
        String sql = String.format("select * from `%s` where username like ? limit ?, ?", rbacProperties.getUserTableName());
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(rbacProperties.getRbacUserClass()),name ,st, limit);
    }

    @Override
    public int countUserLikeName(String name) {
        name += "%";
        String sql = String.format("select count(*) from `%s` where username like ?", rbacProperties.getUserTableName());
        return jdbcTemplate.queryForObject(sql, Integer.class, name);
    }

    @Override
    public List listUserByRole(String roleName, int st, int limit) {
        String sql = String.format("select `user`.* from `%s` left join user_permission_role on (`user`.id = user_id) where type = 'role' and name = ? limit ?, ?", rbacProperties.getUserTableName());
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(rbacProperties.getRbacUserClass()),roleName ,st, limit);
    }

    @Override
    public int countUserByRole(String roleName) {
        String sql = String.format("select count(*) from `%s` left join user_permission_role on (`user`.id = user_id) where type = 'role' and name = ?", rbacProperties.getUserTableName());
        return jdbcTemplate.queryForObject(sql, Integer.class, roleName);
    }

    @Override
    public List listUserByUerNameAndRoleName(String userName, String roleName, int st, int limit) {
        userName += "%";
        String sql = String.format("select `%s`.* from `user` left join user_permission_role on (`%s`.id = user_id) where type = 'role' and name = ? and username like ? limit ?, ?", rbacProperties.getUserTableName(), rbacProperties.getUserTableName());
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(rbacProperties.getRbacUserClass()),roleName, userName, st, limit);
    }

    @Override
    public int countUserByUerNameAndRoleName(String userName, String roleName) {
        userName += "%";
        String sql = String.format("select count(*) from `%s` left join user_permission_role on (`%s`.id = user_id) where type = 'role' and name = ? and username like ?", rbacProperties.getUserTableName(), rbacProperties.getUserTableName());
        return jdbcTemplate.queryForObject(sql, Integer.class,roleName, userName);
    }

    @Override
    public int updateUserName(Integer id, String username) {
        String sql = String.format("update `%s` set username = ?, gmt_modified = ? where id = ?", rbacProperties.getUserTableName());
        return jdbcTemplate.update(sql, username, new Date(), id);
    }

    @Override
    public int updatePassword(Integer id, String password) {
        String sql = String.format("update `%s` set password = ?, gmt_modified = ? where id = ?", rbacProperties.getUserTableName());
        return jdbcTemplate.update(sql, password, new Date(), id);
    }

    @Override
    public int updateAccountNonExpired(Integer id, boolean accountNonExpired) {
        String sql = String.format("update `%s` set account_non_expired = ?, gmt_modified = ? where id = ?", rbacProperties.getUserTableName());
        return jdbcTemplate.update(sql, accountNonExpired, new Date(), id);
    }

    @Override
    public int updateAccountNonLocked(Integer id, boolean accountNonLocked) {
        String sql = String.format("update `%s` set account_non_locked = ?, gmt_modified = ? where id = ?", rbacProperties.getUserTableName());
        return jdbcTemplate.update(sql, accountNonLocked, new Date(), id);
    }

    @Override
    public int updateEnabled(Integer id, boolean enabled) {
        String sql = String.format("update `%s` set enabled = ?, gmt_modified = ? where id = ?", rbacProperties.getUserTableName());
        return jdbcTemplate.update(sql, enabled, new Date(), id);
    }

}
