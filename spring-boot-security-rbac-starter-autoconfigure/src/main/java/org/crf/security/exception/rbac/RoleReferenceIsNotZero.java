package org.crf.security.exception.rbac;

import org.crf.security.exception.PermissionException;

/**
 * @author crf
 */
public class RoleReferenceIsNotZero extends PermissionException {
    public RoleReferenceIsNotZero(String msg, Throwable cause) {
        super(msg, cause);
    }

    public RoleReferenceIsNotZero(String msg) {
        super(msg);
    }
}
