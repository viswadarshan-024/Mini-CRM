package com.viswa.crm.exception;

public class SystemException extends ApplicationException {

    public SystemException(String message, Throwable cause) {
        super(message);
        initCause(cause);
    }
}
