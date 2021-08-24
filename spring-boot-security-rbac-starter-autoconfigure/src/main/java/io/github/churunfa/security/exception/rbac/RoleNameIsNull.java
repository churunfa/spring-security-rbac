package io.github.churunfa.security.exception.rbac;

import io.github.churunfa.security.exception.PermissionException;

/**
 * @author crf
 */
public class RoleNameIsNull extends PermissionException {
    public RoleNameIsNull(String msg, Throwable cause) {
        super(msg, cause);
    }

    public RoleNameIsNull(String msg) {
        super(msg);
    }
}
