package com.albo.comics.config.exception;

import com.albo.comics.config.ErrorCode;

public class NotFoundException extends GenericException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
