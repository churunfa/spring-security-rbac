package io.github.churunfa.security.exception.rbac;

import io.github.churunfa.security.exception.PermissionException;

/**
 * @author crf
 */
public class RoleNotFoundException extends PermissionException {
    public RoleNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public RoleNotFoundException(String msg) {
        super(msg);
    }
}
