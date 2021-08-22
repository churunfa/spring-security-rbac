package org.crf.security.exception;

/**
 * @author crf
 */
public abstract class PermissionException extends Exception{
    public PermissionException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public PermissionException(String msg) {
        super(msg);
    }
}
