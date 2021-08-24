package io.github.churunfa.security.exception.rbac;

import io.github.churunfa.security.exception.PermissionException;

/**
 * @author crf
 */
public class RoleIsExist extends PermissionException {
    public RoleIsExist(String msg, Throwable cause) {
        super(msg, cause);
    }

    public RoleIsExist(String msg) {
        super(msg);
    }
}
