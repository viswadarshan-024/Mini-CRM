package com.viswa.crm.exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;

public class ApplicationException extends RuntimeException {

    public ApplicationException(String message) {
        super(message);
    }
}
