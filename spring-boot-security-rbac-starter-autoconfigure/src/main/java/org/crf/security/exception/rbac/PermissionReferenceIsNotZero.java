package org.crf.security.exception.rbac;

import org.crf.security.exception.PermissionException;

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
