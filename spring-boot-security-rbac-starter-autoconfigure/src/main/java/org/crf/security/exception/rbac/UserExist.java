package org.crf.security.exception.rbac;

import org.crf.security.exception.PermissionException;

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
