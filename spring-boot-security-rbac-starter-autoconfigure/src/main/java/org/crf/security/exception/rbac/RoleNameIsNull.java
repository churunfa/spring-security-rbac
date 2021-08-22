package org.crf.security.exception.rbac;

import org.crf.security.exception.PermissionException;

/**
 * @author crf
 */
public class RoleNameIsNull extends PermissionException {
    public RoleNameIsNull(String msg, Throwable cause) {
        super(msg, cause);
    }

    public RoleNameIsNull(String msg) {
        super(msg);
    }
}
