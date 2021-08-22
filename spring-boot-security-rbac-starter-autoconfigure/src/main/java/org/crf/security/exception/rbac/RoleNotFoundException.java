package org.crf.security.exception.rbac;

import org.crf.security.exception.PermissionException;

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
