package org.crf.security.exception.rbac;

import org.crf.security.exception.PermissionException;

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
