package io.github.churunfa.security.exception.rbac;

import io.github.churunfa.security.exception.PermissionException;

/**
 * @author crf
 */
public class UserExist extends PermissionException {
    public UserExist(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UserExist(String msg) {
        super(msg);
    }
}
