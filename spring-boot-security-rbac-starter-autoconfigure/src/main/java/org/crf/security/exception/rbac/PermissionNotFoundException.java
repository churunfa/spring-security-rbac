package org.crf.security.exception.rbac;

import org.crf.security.exception.PermissionException;

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
