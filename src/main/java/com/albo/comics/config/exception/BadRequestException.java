package com.albo.comics.config.exception;


import com.albo.comics.config.ErrorCode;

public class BadRequestException extends GenericException {
    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
