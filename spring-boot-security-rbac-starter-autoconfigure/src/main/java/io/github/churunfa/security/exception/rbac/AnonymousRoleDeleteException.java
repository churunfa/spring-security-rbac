package io.github.churunfa.security.exception.rbac;

import io.github.churunfa.security.exception.PermissionException;


/**
 * @author crf
 */
public class AnonymousRoleDeleteException extends PermissionException {
    public AnonymousRoleDeleteException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public AnonymousRoleDeleteException(String msg) {
        super(msg);
    }
}
