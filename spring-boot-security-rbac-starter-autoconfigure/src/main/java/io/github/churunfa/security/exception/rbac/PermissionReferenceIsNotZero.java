package io.github.churunfa.security.exception.rbac;

import io.github.churunfa.security.exception.PermissionException;

/**
 * @author crf
 */
public class PermissionReferenceIsNotZero extends PermissionException {
    public PermissionReferenceIsNotZero(String msg, Throwable cause) {
        super(msg, cause);
    }

    public PermissionReferenceIsNotZero(String msg) {
        super(msg);
    }
}
