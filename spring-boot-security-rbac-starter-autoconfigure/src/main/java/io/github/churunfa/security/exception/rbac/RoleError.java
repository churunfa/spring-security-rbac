package io.github.churunfa.security.exception.rbac;

import io.github.churunfa.security.exception.PermissionException;

/**
 * @author crf
 */
public class RoleError extends PermissionException {
    public RoleError(String msg, Throwable cause) {
        super(msg, cause);
    }

    public RoleError(String msg) {
        super(msg);
    }
}
