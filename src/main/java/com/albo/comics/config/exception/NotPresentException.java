package com.albo.comics.config.exception;

import com.albo.comics.config.ErrorCode;

public class NotPresentException extends GenericException {
    public NotPresentException(ErrorCode errorCode) {
        super(errorCode);
    }
}
