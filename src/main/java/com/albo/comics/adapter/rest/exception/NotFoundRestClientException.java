package com.albo.comics.adapter.rest.exception;

import com.albo.comics.config.ErrorCode;
import com.albo.comics.config.exception.GenericException;

public final class NotFoundRestClientException extends GenericException {

    public NotFoundRestClientException(ErrorCode errorCode) {
        super(errorCode);
    }
}
