package com.albo.comics.adapter.rest.exception;

import com.albo.comics.config.ErrorCode;
import com.albo.comics.config.exception.GenericException;

public final class TimeoutRestClientException extends GenericException {

    public TimeoutRestClientException(ErrorCode errorCode) {
        super(errorCode);
    }

}
