package com.albo.comics.config;

import brave.Tracer;
import com.albo.comics.adapter.rest.exception.NotFoundRestClientException;
import com.albo.comics.adapter.rest.exception.RestClientGenericException;
import com.albo.comics.adapter.rest.exception.TimeoutRestClientException;
import com.albo.comics.config.exception.BadRequestException;
import com.albo.comics.config.exception.NotFoundException;
import com.albo.comics.config.exception.NotPresentException;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Slf4j
@ControllerAdvice
public class ErrorHandler {
    private static final String X_B3_TRACE_ID = "X-B3-TraceId";
    private static final String X_B3_SPAN_ID = "X-B3-SpanId";
    private final HttpServletRequest httpServletRequest;
    private final Tracer tracer;

    public ErrorHandler(final HttpServletRequest httpServletRequest, final Tracer tracer) {
        this.httpServletRequest = httpServletRequest;
        this.tracer = tracer;
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiErrorResponse> handle(Throwable ex) {
        log.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex);
        return buildResponseError(HttpStatus.INTERNAL_SERVER_ERROR, ex, ErrorCode.INTERNAL_ERROR);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class})
    public ResponseEntity<ApiErrorResponse> handle(MethodArgumentNotValidException ex) {
        log.error(HttpStatus.BAD_REQUEST.getReasonPhrase(), ex);
        return buildResponseError(HttpStatus.BAD_REQUEST, ex, ErrorCode.BAD_REQUEST);
    }

    @ExceptionHandler({
            NotFoundRestClientException.class,
            NotFoundException.class
    })
    public ResponseEntity<ApiErrorResponse> handle(NotFoundRestClientException ex) {
        log.error(HttpStatus.NOT_FOUND.getReasonPhrase(), ex);
        return buildResponseError(HttpStatus.NOT_FOUND, ex, ex.getCode());
    }

    @ExceptionHandler(TimeoutRestClientException.class)
    public ResponseEntity<ApiErrorResponse> handle(TimeoutRestClientException ex) {
        log.error(HttpStatus.REQUEST_TIMEOUT.getReasonPhrase(), ex);
        return buildResponseError(HttpStatus.REQUEST_TIMEOUT, ex, ex.getCode());
    }

    @ExceptionHandler(RestClientGenericException.class)
    public ResponseEntity<ApiErrorResponse> handle(RestClientGenericException ex) {
        log.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex);
        return buildResponseError(HttpStatus.INTERNAL_SERVER_ERROR, ex, ex.getCode());
    }

    @ExceptionHandler(NotPresentException.class)
    public ResponseEntity<ApiErrorResponse> handle(NotPresentException ex) {
        log.error(HttpStatus.NOT_FOUND.getReasonPhrase(), ex);
        return buildResponseError(HttpStatus.NOT_FOUND, ex, ex.getCode());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> handle(BadRequestException ex) {
        log.error(HttpStatus.BAD_REQUEST.getReasonPhrase(), ex);
        return buildResponseError(HttpStatus.BAD_REQUEST, ex, ErrorCode.BAD_REQUEST);
    }

    private ResponseEntity<ApiErrorResponse> buildResponseError(HttpStatus httpStatus, Throwable ex,
                                                                ErrorCode errorCode) {

        final var traceId = Optional.ofNullable(this.tracer.currentSpan())
                .map(span -> span.context().traceIdString())
                .orElse(Strings.EMPTY);

        final var spandId = Optional.ofNullable(this.tracer.currentSpan())
                .map(span -> span.context().spanIdString())
                .orElse(Strings.EMPTY);

        final var queryString = Optional.ofNullable(httpServletRequest.getQueryString())
                .orElse(Strings.EMPTY);

        final var metaData = Map.of(X_B3_TRACE_ID, traceId,
                X_B3_SPAN_ID, spandId,
                "query_string", queryString);

        final var apiErrorResponse = ApiErrorResponse
                .builder()
                .timestamp(LocalDateTime.now())
                .name(httpStatus.getReasonPhrase())
                .status(httpStatus.value())
                .code(errorCode.value())
                .id(Strings.EMPTY)
                .resource(httpServletRequest.getRequestURI())
                .metadata(metaData)
                .build();

        return new ResponseEntity<>(apiErrorResponse, httpStatus);
    }

    @Builder
    @NonNull
    private static class ApiErrorResponse {

        private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss[.SSSSSSSSS]['Z']";

        @JsonProperty
        private String name;
        @JsonProperty
        private Integer status;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
        private LocalDateTime timestamp;
        @JsonProperty
        private Integer code;
        @JsonProperty
        private String resource;
        @JsonProperty
        private String id;
        @JsonProperty
        private String detail;
        @JsonProperty
        private Map<String, String> metadata;
    }
}

