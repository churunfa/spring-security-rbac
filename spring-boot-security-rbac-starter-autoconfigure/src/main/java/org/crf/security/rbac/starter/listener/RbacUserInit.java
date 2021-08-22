package org.crf.security.rbac.starter.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.crf.security.exception.rbac.*;
import org.crf.security.rbac.starter.dao.RbacUserSecurityDao;
import org.crf.security.rbac.starter.properties.RbacProperties;
import org.crf.security.rbac.starter.service.rbac.RbacService;
import org.crf.security.rbac.starter.user.Permission;
import org.crf.security.rbac.starter.user.RbacUser;
import org.crf.security.rbac.starter.user.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;

/**
 * @author crf
 */
@Component
public class RbacUserInit implements ApplicationRunner {

    private final PasswordEncoder pe;

    private final RbacUserSecurityDao rbacUserSecurityDao;

    private final RbacProperties userProperties;

    private final TransactionTemplate transactionTemplate;

    private final RbacService rbacService;

    private static final Log logger = LogFactory.getLog(User.class);

    @Autowired
    public RbacUserInit(PasswordEncoder pe, RbacUserSecurityDao rbacUserSecurityDao, RbacProperties userProperties, TransactionTemplate transactionTemplate, RbacService rbacService) {
        this.pe = pe;
        this.rbacUserSecurityDao = rbacUserSecurityDao;
        this.userProperties = userProperties;
        this.transactionTemplate = transactionTemplate;
        this.rbacService = rbacService;
    }


    @Override
    public void run(ApplicationArguments args) {

        transactionTemplate.execute(transactionStatus -> {
            try {
                if (!userProperties.isAutoCreateRootAccount()) {
                    return null;
                }
                int userCount = rbacUserSecurityDao.getUserCount();
                if (userCount != 0) {
                    return null;
                }
                RbacUser root = new RbacUser("root", pe.encode("123456"), true, true, true, true, null, new Date(), new Date(), null);
                RbacUser anonymousUser = new RbacUser("anonymousUser", pe.encode("123456"), true, true, true, true, null, new Date(), new Date(), null);
                int count = rbacUserSecurityDao.insert(root);
                int count2 = rbacUserSecurityDao.insert(anonymousUser);


                if (count == 0) {
                    throw new RuntimeException("root用户创建失败");
                }

                if (count2 == 0) {
                    throw new RuntimeException("anonymousUser用户创建失败");
                }

                Role role = new Role("root", "all", "root用户拥有所有权限");
                Role anonymousRole = new Role("ANONYMOUS", "公开资源", "匿名角色");
                rbacService.addRole(role);
                rbacService.addRole(anonymousRole);
                rbacService.grantUserRole(root.getId(), role.getId());
                rbacService.grantUserRole(root.getId(), anonymousRole.getId());
                rbacService.grantUserRole(anonymousUser.getId(), anonymousRole.getId());

                Permission permission1 = new Permission("/rbac/**", "后台管理页面", "rbac系统资源");
                Permission permission2 = new Permission("/api/rbac/user/hasRole/**", "权限检验接口", "rbac系统权限");

                rbacService.addPermission(permission1);
                rbacService.addPermission(permission2);
                rbacService.grantRolePermission(anonymousRole.getId(), permission1.getId());
                rbacService.grantRolePermission(anonymousRole.getId(), permission2.getId());

                logger.info("root用户初始化成功，默认密码为：123456，请及时登陆管理页修改密码");
                return null;
            } catch (RoleIsExist | RoleNotFoundException | RoleNameIsNull roleIsExist) {
                roleIsExist.printStackTrace();
            } catch (RoleError roleError) {
                roleError.printStackTrace();
            } catch (PermissionNotFoundException e) {
                e.printStackTrace();
            } catch (PermissionIsExist permissionIsExist) {
                permissionIsExist.printStackTrace();
            } finally {
                transactionStatus.isRollbackOnly();
            }
            return null;
        });
    }
}
