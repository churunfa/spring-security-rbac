package io.github.churunfa.security.exception.rbac;

import io.github.churunfa.security.exception.PermissionException;

/**
 * @author crf
 */
public class PermissionIsError extends PermissionException {
    public PermissionIsError(String msg, Throwable cause) {
        super(msg, cause);
    }

    public PermissionIsError(String msg) {
        super(msg);
    }
}
