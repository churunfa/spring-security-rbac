package io.github.churunfa.security.rbac.starter.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import io.github.churunfa.security.exception.rbac.*;
import io.github.churunfa.security.rbac.starter.dao.RbacUserSecurityDao;
import io.github.churunfa.security.rbac.starter.properties.RbacProperties;
import io.github.churunfa.security.rbac.starter.service.rbac.RbacService;
import io.github.churunfa.security.rbac.starter.user.Permission;
import io.github.churunfa.security.rbac.starter.user.RbacUser;
import io.github.churunfa.security.rbac.starter.user.Role;
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
                    throw new RuntimeException("root??????????????????");
                }

                if (count2 == 0) {
                    throw new RuntimeException("anonymousUser??????????????????");
                }

                Role role = new Role("root", "all", "root????????????????????????");
                Role anonymousRole = new Role("ANONYMOUS", "????????????", "????????????");
                rbacService.addRole(role);
                rbacService.addRole(anonymousRole);
                rbacService.grantUserRole(root.getId(), role.getId());
                rbacService.grantUserRole(root.getId(), anonymousRole.getId());
                rbacService.grantUserRole(anonymousUser.getId(), anonymousRole.getId());

                Permission permission1 = new Permission("/rbac/**", "??????????????????", "rbac????????????");
                Permission permission2 = new Permission("/api/rbac/user/hasRole/**", "??????????????????", "rbac????????????");

                rbacService.addPermission(permission1);
                rbacService.addPermission(permission2);
                rbacService.grantRolePermission(anonymousRole.getId(), permission1.getId());
                rbacService.grantRolePermission(anonymousRole.getId(), permission2.getId());

                logger.info("root??????????????????????????????????????????123456???????????????????????????????????????");
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
