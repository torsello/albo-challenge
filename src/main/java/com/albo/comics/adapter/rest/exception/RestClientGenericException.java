package com.albo.comics.adapter.rest.exception;

import com.albo.comics.config.ErrorCode;
import com.albo.comics.config.exception.GenericException;

public final class RestClientGenericException extends GenericException {

    public RestClientGenericException(ErrorCode errorCode) {
        super(errorCode);
    }

}
