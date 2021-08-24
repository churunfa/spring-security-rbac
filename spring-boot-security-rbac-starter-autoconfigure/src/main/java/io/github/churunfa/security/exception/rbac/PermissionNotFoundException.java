package io.github.churunfa.security.exception.rbac;

import io.github.churunfa.security.exception.PermissionException;

/**
 * @author crf
 */
public class PermissionNotFoundException extends PermissionException {
    public PermissionNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public PermissionNotFoundException(String msg) {
        super(msg);
    }
}
