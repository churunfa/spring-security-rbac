package org.crf.security.exception.rbac;

import org.crf.security.exception.PermissionException;

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
