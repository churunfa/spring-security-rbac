package io.github.churunfa.security.exception.rbac;

import io.github.churunfa.security.exception.PermissionException;

/**
 * @author crf
 */
public class PermissionIsExist extends PermissionException {
    public PermissionIsExist(String msg, Throwable cause) {
        super(msg, cause);
    }

    public PermissionIsExist(String msg) {
        super(msg);
    }
}
